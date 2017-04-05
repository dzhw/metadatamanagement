'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var searchTranslations = {
      //jscs:disable
      'search-management': {
        'delete-messages': {
          'delete-variables-title': 'Replace all Variables?',
          'delete-variables': 'Are you sure you want to replace all data of the Variables within the Data Acquisition Project "{{ id }}"?',
          'delete-surveys-title': 'Replace all Surveys?',
          'delete-surveys': 'Are you sure you want to replace all data of the Surveys within the Data Acquisition Project "{{ id }}"?',
          'delete-questions-title': 'Delete all Questions?',
          'delete-questions': 'Are you sure you want to delete all Questions within the Data Acquisition Project with RDC-ID "{{ id }}"?',
          'delete-data-sets-title': 'Delete all Data Sets?',
          'delete-data-sets': 'Are you sure you want to delete all Data Sets within the Data Acquisition Project with RDC-ID "{{ id }}"?',
          'delete-studies-title': 'Delete Study?',
          'delete-studies': 'Are you sure you want to the Study of the Data Acquisition Project with RDC-ID "{{ id }}"?',
          'delete-related-publications-title': 'Delete all Publications?',
          'delete-related-publications': 'Are you sure you want to delete all Publications?',
          'delete-instruments-title': 'Delete all Instruments?',
          'delete-instruments': 'Are you sure you want to delete all Instruments within the Data Acquisition Project with RDC-ID "{{ id }}"?'
        },
        'detail': {
          'search': 'Search'
        },
        'buttons': {
          'refresh': 'Refresh search results',
          'upload-variables': 'Upload Variables for selected Data Acquisition Project',
          'upload-surveys': 'Upload Surveys for selected Data Acquisition Project',
          'upload-data-sets': 'Upload Data Sets for selected Data Acquisition Project',
          'upload-questions': 'Upload Questions for selected Data Acquisition Project',
          'upload-studies': 'Upload Study for selected Data Acquisition Project',
          'upload-related-publications': 'Upload Publications',
          'post-validate-related-publications': 'Validate Publications',
          'upload-instruments': 'Upload Instruments for selected Data Acquisition Project'
        },
        'input-label': {
          'all': 'Are you searching for Studies, Variables, Questions, Surveys, Data Sets, Instruments or Publications?',
          'variables': 'Are you searching for Variables?',
          'questions': 'Are you searching for Questions?',
          'surveys': 'Are you searching for Surveys?',
          'data-sets': 'Are you searching for Data Sets?',
          'studies': 'Are you searching for Studies?',
          'related-publications': 'Are you searching for Publications?',
          'instruments': 'Are you searching for Instruments?'
        },
        'tabs': {
          'variables': 'Variables',
          'variables-tooltip': 'Click to select the "Variables" tab',
          'questions': 'Questions',
          'questions-tooltip': 'Click to select the "Questions" tab',
          'surveys': 'Surveys',
          'surveys-tooltip': 'Click to select the "Surveys" tab',
          'data_sets': 'Data Sets',
          'data_sets-tooltip': 'Click to select the "Data Sets" tab',
          'studies': 'Studies',
          'studies-tooltip': 'Click to select the "Studies" tab',
          'all': 'All',
          'all-tooltip': 'Click to select the "All" tab',
          'related_publications': 'Publications',
          'related_publications-tooltip': 'Click to select the "Publications" tab',
          'instruments': 'Instruments',
          'instruments-tooltip': 'Click to select the "Instruments" tab'
        },
        'cards': 'Click to navigate to "{{type}}"'
      }
      //jscs:enable
    };
    $translateProvider.translations('en', searchTranslations);
  });
