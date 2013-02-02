import os
import gettext

#  The translation files will be under 
#  @LOCALE_DIR@/@LANGUAGE@/LC_MESSAGES/@APP_NAME@.mo
APP_NAME = "novabill"

APP_DIR = '/home/gio/GIT/novadart-novabill/novabill-report/build'
LOCALE_DIR = os.path.join(APP_DIR, 'locale')

mo_location = LOCALE_DIR

# Lets tell those details to gettext
#  (nothing to change here for you)
gettext.install (True)
gettext.bindtextdomain (APP_NAME, mo_location)
gettext.textdomain (APP_NAME)

def instantiate_translator(languages):
    return gettext.translation (APP_NAME, mo_location, languages = languages, fallback = True).ugettext