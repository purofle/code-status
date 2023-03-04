from rich.console import Console

import res
from config import get_config

console = Console()


def cli():
    try:
        get_config()
    except FileNotFoundError:
        console.print(res.config_not_found)


if __name__ == "__main__":
    cli()
