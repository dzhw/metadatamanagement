'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'data-acquisition-project-management': {
        'name': 'Name of the Data Acquisition Projects',
        'release': {
          'version': 'Version of the Data Acquisition Projects'
        },
        'home': {
          'title': 'Data Acquisition Projects',
          'createLabel': 'Create a new Data Acquisition Project',
          'releaseLabel': 'Release Data Acquisition Project "{{ id }}"',
          'dialog-tooltip': {
            'create-ok': 'Click to create the Data Acquisition Project',
            'create-cancel': 'Click to close the dialog without creating a project',
            'release-ok': 'Click to release the project',
            'release-cancel': 'Click to close the dialog without releasing the project'
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
            'released-successfully': 'The projects metadata has been sent to da|ra and the data of the project "{{ id }}" will be visible to all users in about 10 minutes.',
            'released-beta-successfully': 'The data of the project "{{ id }}" will be visible to all users in about 10 minutes. No metadata has been sent to da|ra.',
            'unreleased-successfully': 'The data of the project "{{ id }}" will be visible to Data Providers only in about 10 minutes.',
            'dara-released-not-successfully': 'The data of the project "{{ id }}" could not be released. An error occured during sending metadata to da|ra.',
            'unrelease-title': 'Unrelease Project "{{ id }}"?',
            'unrelease': 'Do you really want to reduce visibility of the project "{{ id }}" to Data Providers only?',
            'release-not-possible-title': 'Project "{{ id }}" cannot be released!',
            'release-not-possible': 'The project "{{ id }}" cannot be released, since there are post-validation errors.'
          }
        },
        'error': {
          'data-acquisition-project': {
            'id': {
              'not-empty': 'The name of the Data Acquisition Project must not be empty!',
              'pattern': 'The name of the project must contain only lowercase letters (a-z) and digits.',
              'size': 'The max length of the name is 32 signs.',
              'exists': 'There is already a Data Acquisition Project with this name.'
            },
            'has-been-released-before': {
              'not-null': 'The information is missing, that a Data Acquisition Project has been relesed before.'
            }
          },
          'configuration': {
            'publishers': {
              'not-empty': 'The project must have at least one publisher.'
            }
          },
          'release': {
            'version': {
              'not-empty': 'The version must not be empty.',
              'pattern': 'The version must match the pattern "major.minor.patch" (e.g. "1.0.0").',
              'not-parsable-or-not-incremented': 'The version number must be at least as high as the last version. The previous version was "{{lastVersion}}".',
              'size': 'The version must not contain more than 32 characters.'
            }
          },
          'post-validation': {
            'project-has-no-study': 'The Project with the RDC-ID {{ id }} has no study.'
          }
        },
        'project-cockpit': {
          'title': 'Project-Cockpit',
          'search': {
            'placeholder': 'Search for users...',
            'header-data-provider': 'Data Providers of this project',
            'header-publisher': 'Publishers of this project'
          },
          'alert': {
            'title': 'Attention',
            'noproject': 'No project selected.',
            'close': 'Okay'
          },
          'label': {
            'ROLE_USER': 'User',
            'ROLE_ADMIN': 'Admin',
            'ROLE_DATA_PROVIDER': 'Data Provider',
            'ROLE_PUBLISHER': 'Publisher'
          },
          'button': {
            'save': 'Click to save the changes.',
            'remove-user': 'Remove user'
          },
          'list': {
            'empty-data-provider': 'No data providers are assigned to this project.',
            'empty-publisher': 'No publishers are assigned to this project'
          },
          'required-object-types': {
            'header': 'Expected meta data',
            'studies': 'Studies (mandatory)',
            'surveys': 'Surveys',
            'instruments': 'Instruments',
            'questions': 'Questions',
            'data-sets': 'Data Sets',
            'variables': 'Variables',
            'setting-info': 'This meta data must be provided, before a project is publishable:'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
