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
            'ok': 'Klicken, um das Datenaufbereitungsprojekt zu erzeugen',
            'cancel': 'Klicken, um abzubrechen',
            'close': 'Klicken, um den Dialog zu schließen'
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
            'released-successfully': 'Es wurde eine DOI bei da|ra registriert und die Daten des Projektes "{{ id }}" werden in ca. 10 Minuten für alle Benutzer sichtbar sein.',
            'released-beta-successfully': 'Die Daten des Projektes "{{ id }}" werden in ca. 10 Minuten für alle Benutzer sichtbar sein. Es wurde keine DOI bei da|ra registriert.',
            'dara-released-not-successfully': 'Die Daten des Projektes "{{ id }}" können nicht veröffentlicht werden. Es trat ein Fehler bei der Registrierung der DOI bei da|ra auf.',
            'unreleased-successfully': 'Die Daten des Projektes "{{ id }}" werden in ca. 10 Minuten nur noch für FDZ Mitarbeiter sichtbar sein.',
            'unrelease-title': 'Freigabe für Projekt "{{ id }}" zurücknehmen?',
            'unrelease': 'Möchten Sie wirklich, dass das Projekt "{{ id }}" nur noch für FDZ Mitarbeiter sichtbar ist?',
            'release-not-possible-title': 'Projekt "{{ id }}" kann nicht freigegeben werden!',
            'release-not-possible': 'Das Projekt "{{ id }}" kann nicht freigegeben werden, weil bei der Post-Validierung Fehler aufgetreten sind!'
          }
        },
        'error': {
          'data-acquisition-project': {
            'id': {
              'not-empty': 'Die FDZ-ID des Datenaufbereitungsprojekts darf nicht leer sein!',
              'pattern': 'Der Name eines Projektes darf nur aus Zahlen und kleinen Buchstaben (a-z) bestehen.',
              'size': 'Die Maximallänge der FDZ-ID ist 32 Zeichen.'
            },
            'has-been-released-before': {
              'not-null': 'Es muss angegeben sein, ob ein des Datenaufbereitungsprojekts schon einmal veröffentlicht wurde oder nicht.'
            }
          },
          'release': {
            'version': {
              'not-empty': 'Die Version darf nicht leer sein.',
              'pattern': 'Die Version muss von der Form "major.minor.patch" (z.B. "1.0.0") sein.',
              'not-parsable-or-not-incremented': 'Die Version muss bei jedem Release erhöht werden. Die letzte Version war "{{lastVersion}}".',
              'size': 'Die Version darf nicht länger als 32 Zeichen sein.'
            }
          },
          'post-validation': {
            'project-has-no-study': 'Das Projekt mit der FDZID {{ id }} enthält keine Studie.'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
