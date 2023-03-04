import os

from pydantic import BaseModel


# 从系统 config 读取配置文件，比如 ~/.config/codestatus/config.json
def get_config_path() -> str:
    match os.name:
        case "posix":
            config_path = os.path.join(os.environ["HOME"], ".config", "codestatus", "config.json")
        case "nt":
            config_path = os.path.join(os.environ["APPDATA"], "codestatus", "config.json")
        case _:
            raise NotImplementedError(f"Unsupported OS: {os.name}")
    return config_path


def get_config() -> "AppConfig":
    """
    从配置文件中读取配置
    :return:  配置
    """
    config_path = get_config_path()
    if not os.path.exists(config_path):
        raise FileNotFoundError(f"Config file not found: {config_path}")
    AppConfig.parse_file(config_path)


def save_config(config: "AppConfig"):
    """
    保存配置到配置文件
    :param config: 配置
    :return:
    """
    config_path = get_config_path()
    os.makedirs(os.path.dirname(config_path), exist_ok=True)

    with open(config_path, "w", encoding="utf-8") as f:
        f.write(config.json(exclude_none=True, indent=2, by_alias=True, ensure_ascii=False))


class AppConfig(BaseModel):
    access_token: str
