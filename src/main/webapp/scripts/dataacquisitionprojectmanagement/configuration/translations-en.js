'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'data-acquisition-project-management': {
        'name': 'Name of the Data Acquisition Projects',
        'release': {
          'version': 'Version of the Data Acquisition Projects',
          'notes': {
            'de': 'Release Notes (in German)',
            'en': 'Release Notes (in English)'
          }
        },
        'home': {
          'title': 'Data Acquisition Projects',
          'createLabel': 'Create a new Data Acquisition Project',
          'releaseLabel': 'Release Data Acquisition Project "{{ id }}"',
          'dialog-tooltip': {
            'ok': 'Click to create the Data Acquisition Project',
            'cancel': 'Click to cancel',
            'close': 'Click to close the dialog'
          }
        },
        'delete': {
          'question': 'Are you sure you want to delete Data Acquisition Project "{{ name }}"?'
        },
        'log-messages': {
          'data-acquisition-project': {
            'saved': 'Successfully saved the Data Acquisition Project "{{ id }}"!',
            'server-error': 'An error on the server has occurred: ',
            'delete-title': 'Delete Project "{{ id }}"?',
            'delete': 'Do you really want to delete the Project "{{ id }}"? This cannot be undone.',
            'deleted-successfully-project': 'Successfully deleted Data Acquisition Project "{{ id }}"!',
            'deleted-not-successfully-project': 'Could not delete Data Acquisition Project "{{ id }}"!',
            'released-successfully': 'The data of the project "{{ id }}" will be visible to all users in about 10 minutes.',
            'unreleased-successfully': 'The data of the project "{{ id }}" will be visible to RDC employees only in about 10 minutes.',
            'dara-released-not-successfully': 'The data of the project "{{ id }}" could not be released. It happened an error on the registration / update of the DOI at Dara.',
            'unrelease-title': 'Unrelease Project "{{ id }}"?',
            'unrelease': 'Do you really want to reduce visibility of the project "{{ id }}" to RDC employees only?',
            'release-not-possible-title': 'Project "{{ id }}" cannot be released!',
            'release-not-possible': 'The project "{{ id }}" cannot be released, since there are post-validation errors.'
          }
        },
        'error': {
          'data-acquisition-project': {
            'id': {
              'not-empty': 'The RDC-ID of Data Acquisition Project must not be empty!',
              'pattern': 'The RDC-ID must contain only alphanumeric signs, german umlauts and ß.',
              'size': 'The max length of the RDC-ID is 32 signs.'
            },
            'has-been-released-before': {
              'not-null': 'The information is missing, that a Data Acquisition Project has been relesed before.'
            }
          },
          'post-validation': {
            'project-has-no-study': 'The Project with the RDC-ID {{ id }} has no study.'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
