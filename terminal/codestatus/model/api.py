from pydantic import BaseModel


class DeviceCode(BaseModel):
    device_code: str
    user_code: str
    verification_uri: str
    expires_in: int
    interval: int


class CheckAuth(BaseModel):
    access_token: str
    token_type: str
    scope: str
