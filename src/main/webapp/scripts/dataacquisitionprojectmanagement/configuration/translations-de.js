'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'data-acquisition-project-management': {
        'name': 'Name des Datenaufbereitungsprojektes',
        'release': {
          'version': 'Version des Datenaufbereitungsprojektes'
        },
        'home': {
          'title': 'Datenaufbereitungsprojekte',
          'createLabel': 'Neues Datenaufbereitungsprojekt anlegen',
          'releaseLabel': 'Das Datenaufbereitungsprojekt "{{ id }}" freigeben',
          'dialog-tooltip': {
            'create-ok': 'Klicken, um das Datenaufbereitungsprojekt zu erzeugen',
            'create-cancel': 'Klicken, um den Dialog zu schließen ohne ein Projekt anzulegen',
            'release-ok': 'Klicken, um das Projekt freizugeben',
            'release-cancel': 'Klicken, um den Dialog zu schließen ohne das Projekt freizugeben'
          }
        },
        'delete': {
          'question': 'Sind Sie sicher, dass Sie das Datenaufbereitungsprojekt "{{ name }}" löschen möchten?'
        },
        'log-messages': {
          'data-acquisition-project': {
            'saved': 'Datenaufbereitungsprojekt "{{ id }}" wurde erfolgreich gespeichert!',
            'server-error': 'Ein Fehler ist auf dem Server aufgetreten: ',
            'delete-title': 'Projekt "{{ id }}" löschen?',
            'delete': 'Möchten Sie wirklich das Projekt "{{ id }}" löschen? Das Projekt kann hiernach nicht wieder hergestellt werden.',
            'deleted-successfully-project': 'Das Datenaufbereitungsprojekt "{{ id }}" wurde erfolgreich gelöscht.',
            'deleted-not-successfully-project': 'Das Datenaufbereitungsprojekt "{{ id }}" konnte nicht gelöscht werden!',
            'released-successfully': 'Die Metadaten des Projektes wurden bei da|ra gespeichert und die Daten des Projektes "{{ id }}" werden in ca. 10 Minuten für alle Benutzer sichtbar sein.',
            'released-beta-successfully': 'Die Daten des Projektes "{{ id }}" werden in ca. 10 Minuten für alle Benutzer sichtbar sein. Es wurde keine Metadaten zu da|ra gesendet.',
            'dara-released-not-successfully': 'Die Daten des Projektes "{{ id }}" können nicht veröffentlicht werden. Es trat ein Fehler beim Senden der Metadaten zu da|ra auf.',
            'unreleased-successfully': 'Die Daten des Projektes "{{ id }}" werden in ca. 10 Minuten nur noch für Datengeber sichtbar sein.',
            'unrelease-title': 'Freigabe für Projekt "{{ id }}" zurücknehmen?',
            'unrelease': 'Möchten Sie wirklich, dass das Projekt "{{ id }}" nur noch für Datengeber sichtbar ist?',
            'release-not-possible-title': 'Projekt "{{ id }}" kann nicht freigegeben werden!',
            'release-not-possible': 'Das Projekt "{{ id }}" kann nicht freigegeben werden, weil bei der Post-Validierung Fehler aufgetreten sind!'
          }
        },
        'error': {
          'data-acquisition-project': {
            'assignee-group': {
              'not-null': 'Die zuständige Bearbeitergruppe (Publisher oder Datengeber) darf nicht leer sein.',
              'not-assigned': 'Die Zuständigkeit für das Projekt kann nicht geändert werden, weil es Publishern zugewiesen ist.'
            },
            'configuration': {
              'not-null': 'Die Projektkonfiguration darf nicht leer sein'
            },
            'create': {
              'unauthorized': 'Projekte dürfen nur durch Publisher angelegt werden.'
            },
            'id': {
              'not-empty': 'Der Name des Datenaufbereitungsprojekts darf nicht leer sein!',
              'pattern': 'Der Name eines Projektes darf nur aus Zahlen und kleinen Buchstaben (a-z) bestehen.',
              'size': 'Die Maximallänge des Names ist 32 Zeichen.',
              'exists': 'Es gibt bereits ein Datenaufbereitungsprojekt mit diesem Namen.'
            },
            'has-been-released-before': {
              'not-null': 'Es muss angegeben sein, ob ein des Datenaufbereitungsprojekts schon einmal veröffentlicht wurde oder nicht.'
            }
          },
          'configuration': {
            'data-providers': {
              'update-not-allowed': 'Es muss mindestens 1 Datengeber eingetragen sein.'
            },
            'publishers': {
              'not-empty': 'Es muss mindestens ein Publisher eingetragen sein.',
              'unauthorized': 'Publisher dürfen nur durch andere Publisher gesetzt werden.'
            },
            'requirements': {
              'unauthorized': 'Pflichtfelder dürfen nur durch Publishers dieses Projekts geändert werden.'
            }
          },
          'last-assignee-group-message': {
            'size': 'Die Nachricht darf nicht länger als 2048 Zeichen sein.',
            'not-empty':'Bei einem Gruppenwechsel muss eine Nachricht angegeben werden.'
          },
          'release': {
            'version': {
              'not-empty': 'Die Version darf nicht leer sein.',
              'pattern': 'Die Version muss von der Form "major.minor.patch" (z.B. "1.0.0") sein.',
              'not-parsable-or-not-incremented': 'Die Versionsnummer muss mindestens so hoch sein wie die letzte Version. Die letzte Version war "{{lastVersion}}".',
              'size': 'Die Version darf nicht länger als 32 Zeichen sein.'
            }
          },
          'post-validation': {
            'project-has-no-study': 'Das Projekt mit der FDZID {{ id }} enthält keine Studie.'
          }
        },
        'project-cockpit': {
          'title': 'Projekt-Cockpit',
          'search': {
            'placeholder': 'Suchen Sie Benutzer...',
            'header-data-provider': 'Datengeber dieses Projekts',
            'header-publisher': 'Publisher dieses Projekts'
          },
          'tooltip': {
            'not-assigned': 'Das Projekt ist aktuell der anderen Benutzergruppe zugewiesen.',
            'not-in-group': 'Sie gehören nicht dieser Benutzergruppe an.',
            'not-in-publishers': 'Sie sind diesem Projekt nicht als Publisher zugewiesen'
          },
          'alert': {
            'title': 'Achtung',
            'noproject': 'Kein Projekt ausgewählt.',
            'close': 'Okay'
          },
          'label': {
            'ROLE_USER': 'User',
            'ROLE_ADMIN': 'Admin',
            'ROLE_DATA_PROVIDER': 'Datengeber',
            'ROLE_PUBLISHER': 'Publisher'
          },
          'button': {
            'save': 'Klicken, um die Anpassungen zu speichern.',
            'save-assign': 'Klicken, um die Anpassungen zu speichern und das Projekt zuzuweisen.',
            'remove-user': 'Nutzer entfernen'
          },
          'list': {
            'empty-data-provider': 'Keine Datengeber sind diesem Projekt zugeteilt.',
            'empty-publisher': 'Keine Publisher sind diesem Projekt zugeteilt.'
          },
          'tabs': {
            'status': 'Status',
            'config': 'Config'
          },
          'requirements': {
            'header': 'Erwartete Metadaten',
            'studies': 'Studien (immer obligatorisch)',
            'surveys': 'Erhebungen',
            'instruments': 'Instrumente',
            'questions': 'Fragen',
            'dataSets': 'Datensätze',
            'variables': 'Variablen',
            'setting-info': 'Die folgenden Metadaten müssen bereitgestellt werden, bevor dieses Projekt für alle Benutzer freigegeben werden kann:'
          },
          'config': {
            'assigned-group': 'Zugewiesene Benutzergruppe',
            'released': 'Veröffentlicht',
            'expected': 'Erwartet',
            'ready': 'bereit',
            'new': 'Neu',
            'upload': 'Hochladen'
          },
          'message-dialog': {
            'title': 'Nachricht für {{recipient}} eingeben',
            'description': 'Geben Sie eine Nachricht ein, die nach erfolgter Zuweisung an alle {{recipient}} dieses Projekts per E-Mail verschickt wird.',
            'label': 'Nachricht',
            'confirm': 'Bestätigen & Zuweisen',
            'cancel': 'Abbrechen'
          },
          'no-data-providers-dialog': {
            'text': 'Es gibt keine eingetragenen Datengeber für dieses Projekt. Wechseln Sie zur Projektkonfiguration und tragen Sie mindestens einen Datengeber ein'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
