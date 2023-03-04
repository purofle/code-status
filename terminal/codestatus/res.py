import gettext
import locale

lang, encoding = locale.getdefaultlocale()
i18n_dir = "locale"

gettext.bindtextdomain('codestatus', 'locale')
gettext.textdomain('codestatus')

_ = gettext.gettext

config_not_found = _("[red]Config file not found, please run [bold green]codestatus init[/bold green] first.")
description = _("A simple CLI tool to check the status of your code")
init_help = _("Initialize the config file")
get_help = _("Get commit status")
login_success = _("[green]Login success! config file saved.")
login = _("Please visit [bold green]{0}[/bold green] and enter [bold green]{1}[/bold green] to login.")
