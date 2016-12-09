'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'data-acquisition-project-management': {
        'name': 'Name des Datenaufbereitungsprojekts',
        'home': {
          'title': 'Datenaufbereitungsprojekte',
          'createLabel': 'Neues Datenaufbereitungsprojekt anlegen'
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
            'deleted-not-successfully-project': 'Das Datenaufbereitungsprojekt "{{ id }}" konnte nicht gelöscht werden!'
          }
        },
        'error': {
          'data-acquisition-project': {
            'id': {
              'not-empty': 'Die FDZ-ID des Datenaufbereitungsprojekts darf nicht leer sein!',
              'pattern': 'Die FDZ-ID darf nur alphanumerische Zeichen, deutsche Umlaute und ß beinhalten.',
              'size': 'Die Maximallänge der FDZ-ID ist 32 Zeichen.'
            }
          },
          'post-validation': {
            'project-has-no-study': 'Das Projekt enthält keine Studie mit der ID {{ id }}.'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
