import os
import locale
import gettext

#  The translation files will be under 
#  @LOCALE_DIR@/@LANGUAGE@/LC_MESSAGES/@APP_NAME@.mo
APP_NAME = "novabill"

APP_DIR = '/home/risto/novadart/novadart-novabill/novabill-report/build'
LOCALE_DIR = os.path.join(APP_DIR, 'locale')


# Now we need to choose the language. We will provide a list, and gettext
# will use the first translation available in the list
DEFAULT_LANGUAGES = os.environ.get('LANG', '').split(':')
DEFAULT_LANGUAGES += ['en_US', 'it']

# Try to get the languages from the default locale
languages = []
lc, encoding = locale.getdefaultlocale()
if lc:
    languages = [lc]

# Concat all languages (env + default locale), 
#  and here we have the languages and location of the translations
#
languages += DEFAULT_LANGUAGES
mo_location = LOCALE_DIR

# Lets tell those details to gettext
#  (nothing to change here for you)
gettext.install (True)
gettext.bindtextdomain (APP_NAME, mo_location)
gettext.textdomain (APP_NAME)
language = gettext.translation (APP_NAME, mo_location, languages = languages, fallback = True)