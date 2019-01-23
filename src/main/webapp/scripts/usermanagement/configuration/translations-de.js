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
            'success': '<strong>Ihr Benutzer wurde aktiviert.</strong>',
            'error': '<strong>Ihr Benutzer konnte nicht aktiviert werden.</strong> Bitte benutzen Sie die Registrierungsmaske, um sich zu registrieren.',
            'wait-for-role': 'Sie werden benachrichtigt sobald Sie einem Projekt zugewiesen wurden.'
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
            'forgot': 'Sie haben Ihr Passwort vergessen?'
          },
          'registration': {
            'register': 'Registrieren Sie sich'
          }
        },
        'password': {
          'title': 'Passwort für <b>{{username}}</b> ändern',
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
          'title': 'Kontoeigenschaften für Benutzer <b>{{username}}</b>',
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
              'fail': '<strong>Es ist ein Fehler aufgetreten!</strong> Die Kontoänderungen konnten nicht gespeichert werden.',
              'emailexists': '<strong>E-mail wird bereits verwendet!</strong> Bitte wählen Sie eine andere aus.'
            },
            'success': '<strong>Kontoänderungen wurden gespeichert!</strong>',
            'validate': {
              'firstname': {
                'minlength': 'Ihr Vorname muss mindestens 1 Zeichen lang sein',
                'maxlength': 'Ihr Vorname darf nicht länger als 50 Zeichen sein'
              },
              'lastname': {
                'minlength': 'Ihr Nachname muss mindestens 1 Zeichen lang sein',
                'maxlength': 'Ihr Nachname darf nicht länger als 50 Zeichen sein'
              }
            }
          }
        },
        'user-messages': {
          'create-title': 'Nachricht an alle Benutzer (gerade online) verfassen',
          'new-message-title': 'Neue Nachricht von {{sender}}',
          'message-de-label': 'Nachricht (auf Deutsch)',
          'message-en-label': 'Nachricht (auf Englisch)',
          'dialog-tooltip': {
            'close': 'Klicken, um das Senden der Nachricht abzubrechen.',
            'send': 'Klicken, um die Nachricht an alle Benutzer zu senden.',
            'open-create-dialog': 'Klicken, um eine Nachricht an alle Benutzer (gerade online) zu verfassen'
          },
          'buttons': {
            'send': 'Senden'
          }
        },
        'welcome-dialog': {
          'title': 'Herzlich Willkommen',
          'text-body': '<p>Liebe(r) {{username}},</p><p>schön, dass Sie sich dazu entschieden haben, Ihre Forschungsdaten anderen Forschern über unser FDZ zur Verfügung zu stellen. Dieses System dient dazu, alle Informationen rund um Ihre Forschungsdaten (sogenannte Metadaten) von Ihnen als Datengeber(in) zu erfassen und zu veröffentlichen.</p><p>In dem Navigationsmenü auf der linken Seite finden Sie eine Liste aller Datenaufbereitungsprojekte, denen Sie als Datengeber(in) zugewiesen wurden:</p><img src="/assets/images/welcome-dialog-project-chooser-{{language}}.png" class="fdz-welcome-dialog-image"/><p style="margin:10px 0px 0px 0px;">Wählen Sie dort einfach das Projekt aus, für das Sie Metadaten eingeben möchten und klicken Sie anschließend auf die orangene Schaltfläche "Projekt-Cockpit" <img src="/assets/images/welcome-dialog-project-cockpit-button.png"/> unter dem ausgewählten Projekt.</p><p>Das Projekt kann entweder den Publishern (FDZ-Mitarbeitern) oder Ihnen als Datengebern zur Bearbeitung zugewiesen sein. Sobald das Projekt <img src="/assets/images/welcome-dialog-assigned-{{language}}.png" alt="Zugewiesen an Datengeber"/> ist, werden Sie benachrichtigt und Sie können anfangen Metadaten einzugeben.</p><p>Eine ausführliche Benutzerdokumentation für Datengeber finden Sie hier: <a href="https://metadatamanagement.readthedocs.io/de/stable/metadatenabgabe.html" target="_blank">Dokumentation</a>.</p>',
          'do-not-show-again': 'Nicht mehr anzeigen'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
