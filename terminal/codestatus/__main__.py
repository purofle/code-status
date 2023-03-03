from textual.app import App

from .screen.Welcome_login import Welcome


class TerminalApp(App):
    SCREENS = {
        "welcome": Welcome()
    }

    def on_mount(self):
        self.push_screen("welcome")


def cli():
    app = TerminalApp()
    app.run()


if __name__ == "__main__":
    cli()
