import gettext
import locale

lang, encoding = locale.getdefaultlocale()
i18n_dir = "locale"

gettext.bindtextdomain('codestatus', 'locale')
gettext.textdomain('codestatus')

_ = gettext.gettext

config_not_found = _("[red]Config file not found, please run [bold green]codestatus init[/bold green] first.")
