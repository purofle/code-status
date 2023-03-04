import argparse
import asyncio

from pydantic import ValidationError
from rich.console import Console

import res
from api import API
from config import save_config, AppConfig
from model.api import DeviceCode, CheckAuth

console = Console()
api = API()


async def check_login(data: DeviceCode):
    while True:
        check = await api.check_login(data.device_code)
        await asyncio.sleep(5)
        try:
            auth = CheckAuth.parse_raw(check)
        except ValidationError:
            pass
        else:
            save_config(AppConfig(access_token=auth.access_token))
            break


async def login():
    data = await api.get_device_code()
    console.print(res.login.format(data.verification_uri, data.user_code))
    await asyncio.create_task(check_login(data))
    console.print(res.login_success)


def init_config():
    asyncio.run(login())


def get_status():
    pass


def cli():
    parser = argparse.ArgumentParser(
        prog="codestatus",
        description=res.description,
    )

    subparser = parser.add_subparsers(title="subcommands", help="subcommand help")

    init_parse = subparser.add_parser(name="init", help=res.init_help)
    get_parse = subparser.add_parser(name="get", help=res.get_help)

    init_parse.set_defaults(func=init_config)
    get_parse.set_defaults(func=get_status)
    args = parser.parse_args()
    args.func()

    # try:
    #     get_config()
    # except FileNotFoundError:
    #     console.print(res.config_not_found)


if __name__ == "__main__":
    cli()
