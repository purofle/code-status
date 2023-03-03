from pydantic import BaseConfig


class AppConfig(BaseConfig):
    access_token: str
