from textual.app import ComposeResult
from textual.containers import Container
from textual.screen import Screen
from textual.widgets import Static


class Login(Screen):
    def compose(self) -> ComposeResult:
        yield Container(Static("1"))

