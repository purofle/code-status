import os
import sys
from enum import Enum

from pydantic import BaseConfig, BaseModel


# 从系统 config 读取配置文件，比如 ~/.config/codestatus/config.json
def read_config() -> str:
    match os.name:
        case "posix":
            config_path = os.path.join(os.environ["HOME"], ".config", "codestatus", "config.json")
        case "nt":
            config_path = os.path.join(os.environ["APPDATA"], "codestatus", "config.json")
        case _:
            raise NotImplementedError(f"Unsupported OS: {os.name}")
    return config_path


class AppConfig(BaseModel):
    access_token: str
