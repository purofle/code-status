"""Provides a Textual welcome screen."""

from rich.markdown import Markdown
from textual.app import ComposeResult
from textual.containers import Container
from textual.screen import Screen
from textual.widgets import Static, Button

from .github_login import Login
from ..i18n import WELCOME_MD


class Welcome(Screen):

    DEFAULT_CSS = """
        Welcome {
            width: 100%;
            height: 100%;
            background: $surface;
        }

        Welcome Container {
            padding: 1;
            background: $panel;
            color: $text;
        }

        Welcome #text {
            margin:  0 1;
        }

        Welcome #close {
            dock: bottom;
            width: 100%;
        }
    """

    def compose(self) -> ComposeResult:
        yield Container(Static(Markdown(WELCOME_MD), id="text"), id="md")
        yield Button("OK", id="close", variant="success")

    def on_button_pressed(self):
        self.app.push_screen(Login())
