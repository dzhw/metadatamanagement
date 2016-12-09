'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'user-management': {
        'home': {
          'title': 'Benutzer verwalten',
          'createLabel': 'Neuen Benutzer erstellen',
          'createOrEditLabel': 'Benutzer erstellen oder bearbeiten'
        },
        'delete': {
          'question': 'Sind Sie sicher, dass Sie den Benutzer {{ login }} löschen möchten?'
        },
        'detail': {
          'title': 'Benutzer'
        },
        'first-name': 'Vorname',
        'last-name': 'Nachname',
        'email': 'E-Mail',
        'activated': 'Aktiv',
        'deactivated': 'Deaktiviert',
        'profiles': 'Profile',
        'langKey': 'Sprache',
        'createdBy': 'Erstellt von',
        'createdDate': 'Erstellt am',
        'lastModifiedBy': 'Bearbeitet von',
        'lastModifiedDate': 'Zuletzt bearbeitet',
        'error': {
          'authority': {
            'name': {
              'not-null': 'Der Name der Authorisierung darf nicht leer sein!'
            }
          },
          'user': {
            'login': {
              'not-null': 'Das Loginfeld darf nicht leer sein!'
            },
            'password': {
              'not-null': 'Der Passwort darf nicht leer sein!'
            }
          }
        },
        'activate': {
          'title': 'Aktivierung',
          'messages': {
            'success': '<strong>Ihr Benutzer wurde aktiviert.</strong> Bitte ',
            'error': '<strong>Ihr Benutzer konnte nicht aktiviert werden.</strong> Bitte benutzen Sie die Registrierungsmaske, um sich zu registrieren.'
          }
        },
        'login': {
          'login': 'Anmelden',
          'title': 'Anmeldung',
          'form': {
            'password': 'Passwort',
            'password-placeholder': 'Ihr Passwort',
            'rememberme': 'Automatische Anmeldung',
            'button': 'Anmelden'
          },
          'messages': {
            'error': {
              'authentication': '<strong>Anmeldung fehlgeschlagen!</strong> Überprüfen Sie bitte Ihre Angaben und versuchen Sie es erneut.'
            }
          },
          'password': {
            'forgot': 'Sie haben ihr Passwort vergessen?'
          },
          'registration': {
            'register': 'Registrieren Sie sich'
          }
        },
        'password': {
          'title': 'Passwort für [<b>{{username}}</b>]',
          'form': {
            'button': 'Speichern'
          },
          'messages': {
            'error': '<strong>Es ist ein Fehler aufgetreten!</strong> Das Passwort konnte nicht geändert werden.',
            'success': '<strong>Passwort wurde geändert!</strong>'
          }
        },
        'register': {
          'title': 'Registrierung',
          'form': {
            'button': 'Registrieren'
          },
          'messages': {
            'validate': {
              'login': {
                'required': 'Ihr Benutzername wird benötigt.',
                'minlength': 'Ihr Benutzername muss mindestens 1 Zeichen lang sein',
                'maxlength': 'Ihr Benutzername darf nicht länger als 50 Zeichen sein',
                'pattern': 'Ihr Benutzername darf nur Kleinbuchstaben und Ziffern enthalten'
              }
            },
            'success': '<strong>Registrierung gespeichert!</strong> Bitte überprüfen Sie ihre E-Mails für die Bestätigung.',
            'error': {
              'fail': '<strong>Registrierung fehlgeschlagen!</strong> Bitte versuchen Sie es später nochmal.',
              'userexists': '<strong>Benutzername bereits vergeben!</strong> Bitte wählen Sie einen anderen aus.',
              'emailexists': '<strong>E-mail wird bereits verwendet!</strong> Bitte wählen Sie eine andere aus.'
            }
          }
        },
        'reset': {
          'request': {
            'title': 'Passwort zurücksetzen',
            'form': {
              'button': 'Passwort zurücksetzen'
            },
            'messages': {
              'info': 'Geben Sie die E-Mail Adresse ein, welche Sie bei der Registrierung verwendet haben.',
              'success': 'Eine E-Mail mit weiteren Instruktionen für das Zurücksetzen des Passworts wurde gesendet.',
              'notfound': '<strong>Diese E-Mail Adresse existiert nicht!</strong> Überprüfen Sie ihre E-Mail Adresse und versuchen Sie es nochmal.'
            }
          },
          'finish': {
            'title': 'Passwort zurücksetzen',
            'form': {
              'button': 'Neues Passwort setzen'
            },
            'messages': {
              'info': 'Wählen Sie ein neues Passwort',
              'success': '<strong>Ihr Passwort wurde zurückgesetzt.</strong> Bitte ',
              'keymissing': 'Der Reset Schlüssel fehlt.',
              'error': 'Ihr Passwort konnte nicht zurückgesetzt werden. Zur Erinnerung ihre Anfrage ist nur 24 Stunden gültig.'
            }
          }
        },
        'settings': {
          'title': 'Einstellungen für Benutzer [<b>{{username}}</b>]',
          'form': {
            'firstname': 'Vorname',
            'firstname.placeholder': 'Ihr Vorname',
            'lastname': 'Nachname',
            'lastname.placeholder': 'Ihr Nachname',
            'language': 'Sprache',
            'button': 'Speichern'
          },
          'messages': {
            'error': {
              'fail': '<strong>Es ist ein Fehler aufgetreten!</strong> Die Einstellungen konnten nicht gespeichert werden.',
              'emailexists': '<strong>E-mail wird bereits verwendet!</strong> Bitte wählen Sie eine andere aus.'
            },
            'success': '<strong>Einstellungen wurden gespeichert!</strong>',
            'validate': {
              'firstname': {
                'required': 'Ihr Vorname wird benötigt.',
                'minlength': 'Ihr Vorname muss mindestens 1 Zeichen lang sein',
                'maxlength': 'Ihr Vorname darf nicht länger als 50 Zeichen sein'
              },
              'lastname': {
                'required': 'Ihr Nachname wird benötigt.',
                'minlength': 'Ihr Nachname muss mindestens 1 Zeichen lang sein',
                'maxlength': 'Ihr Nachname darf nicht länger als 50 Zeichen sein'
              }
            }
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
