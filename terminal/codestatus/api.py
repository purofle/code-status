import aiohttp

from model.api import DeviceCode


def request(method: str, url: str, **kwargs):
    return aiohttp.request(method, url, headers={"Accept": "application/json"}, **kwargs)


class API:
    def __init__(self, client_id: str = "Iv1.29e4c7ea296a593f"):
        self.client_id = client_id

    async def get_device_code(self) -> "DeviceCode":
        async with request("POST", "https://github.com/login/device/code", data={
            "client_id": self.client_id,
        }) as r:
            return DeviceCode.parse_raw(await r.text())

    async def check_login(self, device_code: str) -> "str":
        async with request("POST", "https://github.com/login/oauth/access_token", data={
            "client_id": self.client_id,
            "device_code": device_code,
            "grant_type": "urn:ietf:params:oauth:grant-type:device_code",
        }) as r:
            return await r.text()
