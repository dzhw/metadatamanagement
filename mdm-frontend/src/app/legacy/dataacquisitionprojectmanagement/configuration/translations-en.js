'use strict';

angular.module('metadatamanagementApp').config([
  '$translateProvider',

  function($translateProvider) {
    var translations = {
      //jscs:disable
      'data-acquisition-project-management': {
        'name': 'Name of the Data Acquisition Projects',
        'release': {
          'version': 'Version of the Data Acquisition Projects',
          'landing-page-de-title': 'German',
          'landing-page-en-title': 'English',
          'landing-page-hint': 'Please select the language of the DOI landing page',
          'pin-to-start-page': 'Show Data Package on Start Page',
          'pin-to-start-page-hint': 'Check this box, if you want to show this data package on the start page.',
          'confirmed': {
            'local': 'This is a local system. Are you sure?',
            'test': 'This is the test stage! Are you sure?',
            'dev': 'This is the dev stage! Are you sure?',
            'prod': 'WARNING: This is the PRODUCTION stage! Are you sure?',
          },
          'confirm-hint': 'Check this box if you really want to release this project on this system!'
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
            'unreleased-successfully': 'The data of the project "{{ id }}" can now be edited by assigned Publishers and Data Providers.',
            'dara-released-not-successfully': 'The data of the project "{{ id }}" could not be released. An error occured during sending metadata to da|ra.',
            'unrelease-title': 'Unrelease Project "{{ id }}"?',
            'unrelease': 'Do you really want to unrelease the project "{{ id }}" and edit its metadata?',
            'release-not-possible-title': 'Project "{{ id }}" cannot be released!',
            'release-not-possible': 'The project "{{ id }}" cannot be released, since there are post-validation errors.'
          }
        },
        'error': {
          'data-acquisition-project': {
            'assignee-group': {
              'not-null': 'The project must be assigned to the publisher or data provider group.',
              'not-assigned': 'Project assignment cannot be changed at this time, because it is assigned to publishers.'
            },
            'configuration': {
              'not-null': 'Die project configuration must not be empty.'
            },
            'create': {
              'unauthorized': 'Only publishers are allowed to create projects.'
            },
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
            'data-provider': {
              'update-not-allowed': 'At least one data provider must be assigned.'
            },
            'publishers': {
              'not-empty': 'The project must have at least one publisher.',
              'unauthorized': 'Publisher can only be assigned by other publishers.'
            },
            'requirements': {
              'unauthorized': 'Only publishers of this project are allowed to change mandatory fields.',
              'publications-required-for-analysis-packages': 'Publication is needed for an analysis package.',
              'either-data-packages-or-analysis-packages-required': 'Either a data package or analysis package is required.'
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
            'project-has-no-dataPackage': 'The Project with the RDC-ID {{ id }} has no data package.',
            'project-has-no-analysisPackage': 'The Project with the RDC-ID {{ id }} has no analysis package.',
            'project-must-have-exactly-one-publication': 'The Project with the RDC-ID {{ id }} must contain exactly one publication.',
            'requirements-not-met': 'There are still metadata that have not been marked as "ready" by the publishers.',
            'project-has-no-survey': 'The Project with the RDC-ID {{ id }} must contain at least one survey.',
            'project-has-no-data-set': 'The Project with the RDC-ID {{ id }} must contain at least one data set.'
          },
          'project-update-access': {
            'project-selected': 'A project has to be selected before data can be added.',
            'type-update-allowed': 'The action is not possible because the metadata has already been marked as "ready" by the publishers or the data providers.',
            'update-for-publishers-allowed': 'The action is not possible because the metadata has already been marked as "ready" by the publishers.',
            'update-for-data-providers-allowed': 'The action is not possible because the metadata has already been marked as "ready" by the publishers or data providers.',
            'project-released': 'The action is not possible because the projects metadata is currently open to all public users.',
            'member-of-assigned-group': 'The action is not possible because the project is currently assigned to the other project group.',
            'assigned-to-project': 'The action is not possible because you are not assigned to the project as a publisher or data provider.',
            'not-required': 'The action is not possible because this metadata was not marked as "expected" in the project settings.',
            'prerequisite-missing-surveys': 'The action is not possible because the project does not contain any survey.',
            'prerequisite-missing-data_packages': 'The action is not possible because the project does not contain a data package yet.'
          }
        },
        'projectstatuslabel': {
          'assigned-to': 'Assigned to',
          'PUBLISHER': 'Publishers',
          'DATA_PROVIDER': 'Data Providers'
        },
        'releasestatusbadge': {
          'released': 'Released',
          'unreleased': 'Unreleased'
        },
        'project-cockpit': {
          'title': 'Project-Cockpit ({{projectId}})',
          'header': 'Project-Cockpit',
          'search': {
            'placeholder': 'Search for users...',
            'header-data-provider': 'Data Providers of this project',
            'header-publisher': 'Publishers of this project',
            'no-users-found': 'No user found!'
          },
          'tooltip': {
            'not-assigned': 'The project is currently assigned to the other user group.',
            'not-in-group': 'You are not assigned to this user group.',
            'not-in-publishers': 'You are not assigned as a publisher of this project.'
          },
          'alert': {
            'title': 'Attention',
            'noproject': 'No project selected.',
            'close': 'Okay'
          },
          'label': {
            'ROLE_USER': 'User',
            'ROLE_ADMIN': 'Admin',
            'ROLE_DATA_PROVIDER': 'Data Providers',
            'ROLE_PUBLISHER': 'Publishers'
          },
          'button': {
            'save': 'Click to save the changes.',
            'save-assign': 'Click to save the changes and to assign the project.',
            'save-takeback': 'Click to save the changes and to assign the project to the publishers group.',
            'remove-user': 'Remove user'
          },
          'list': {
            'empty-data-provider': 'No data providers are assigned to this project.',
            'empty-publisher': 'No publishers are assigned to this project'
          },
          'tabs': {
            'status': 'Status',
            'config': 'Settings',
            'versions': 'Versions'
          },
          'requirements': {
            'header': 'Expected Metadata',
            'dataPackages': 'Data Package',
            'surveys': 'Surveys',
            'instruments': 'Instruments',
            'questions': 'Questions',
            'dataSets': 'Data Sets',
            'variables': 'Variables',
            'publications': 'Publications',
            'concepts': 'Concepts',
            'setting-info': 'The following Metadata must be provided, before this project can be released to all public users:',
            'analysisPackages': 'Analysis Package'
          },
          'config': {
            'assigned-group': 'Assigned User Group',
            'released': 'Published',
            'expected': 'Expected',
            'ready': 'Ready',
            'new': 'New',
            'edit': 'Edit',
            'upload': 'Upload',
            'delete': 'Delete'
          },
          'message-dialog': {
            'title': 'Message to {{recipient}}',
            'description': 'Please provide a message which will be sent to all {{recipient}} of this project via e-mail.',
            'label': 'Message',
            'confirm': 'Assign',
            'cancel': 'Cancel'
          },
          'no-data-providers-dialog': {
            'text': 'This project has no assigned data providers. Please assign at least one data provider in the project configuration.'
          },
          'takeback-dialog': {
            'title': 'Take back project',
            'text': 'Do you really want to withdraw this project from the data provider group?'
          },
          'versions': {
            'header': 'List of all Project Versions (Shadow Copies):',
            'no-shadows': 'The project has not been released yet.',
            'confirm-hide' : {
              'title': 'Hide {{version}} of the project {{id}}?',
              'content': 'Are you sure you want to hide version {{version}} of the project {{id}} from the public user? Data users will no longer be able to see this version.'
            },
            'confirm-unhide' : {
              'title': 'Make version {{version}} of the project {{id}} available again?',
              'content': 'Are you sure you want to make version {{version}} of the project {{id}} available for all users again? Data users will then be able to see this version again.'
            },
            'hiding-toast': 'The version {{version}} of the project {{id}} will be visible only for registered users in about 10 minutes!',
            'unhiding-toast': 'The version {{version}} of the project {{id}} will be visible again for all users in about 10 minutes!',
            'button': {
              'hide-shadow': 'This version is currently visible for all users. Click here to hide this version!',
              'unhide-shadow': 'This version is currently not visible for all users. Click here to make it available for all users!'
            }
          }
        },
        'project-overview': {
          'header': 'Project Overview',
          'table': {
            'project-name': 'Project',
            'release-version': 'Current Version',
            'assigned-group': 'Assigned Group',
            'data-package-status': 'Data Package',
            'analysis-package-status': 'Analysis Package',
            'surveys-status': 'Surveys',
            'instruments-status': 'Instruments',
            'data-sets-status': 'Data Sets',
            'questions-status': 'Questions',
            'variables-status': 'Variables',
            'publications-status': 'Publications',
            'concepts-status': 'Concepts',
            'publisher': 'Publisher',
            'data-provider': 'Data Provider',
            'unreleased': 'unreleased',
            'tooltip': 'Click to open the project cockpit for this project'
          },
          'pagination': {
            'previous': 'Click to show previous projects',
            'next': 'Click to show next projects',
            'current': 'Click to show projects on page {{number}}'
          },
          'no-project-msg': 'There is no project assigned to your account'
        },
        'outdated-version-alert': 'This is an outdated page version ({{oldVersion}}). Choose the current version ({{newVersion}}) in the side menu.</a>',
        'version-not-found-alert': 'Your link refers to a version ({{oldVersion}}) of this page which does not exist. This is the current version ({{newVersion}}).',
        'not-master-alert': 'This is a {{hidden?"<u>hidden</u>":""}} shadow copy ({{version}}). ',
        'no-order-allowed-alert': 'This is a {{hidden?"<u>hidden</u>":""}} shadow copy ({{version}}) which is not orderable. Choose an available version in the side menu. ',
        'current-version': 'Click here to open the current version!'
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  }]);
