#!/usr/bin/env bash
xgettext ../codestatus/res.py -o codestatus.pot
msgmerge --previous --update zh_CN/LC_MESSAGES/codestatus.po codestatus.pot

poedit zh_CN/LC_MESSAGES/codestatus.po

msgfmt zh_CN/LC_MESSAGES/codestatus.po -o zh_CN/LC_MESSAGES/codestatus.mo
