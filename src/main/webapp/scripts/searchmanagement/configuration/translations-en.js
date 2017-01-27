'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var searchTranslations = {
      //jscs:disable
      'search-management': {
        'delete-messages': {
          'delete-variables-title': 'Delete all Variables?',
          'delete-variables': 'Are you sure you want to delete all Variables within the Data Acquisition Project with RDC-ID "{{ id }}"?',
          'delete-surveys-title': 'Delete all Surveys?',
          'delete-surveys': 'Are you sure you want to delete all Surveys within the Data Acquisition Project with RDC-ID "{{ id }}"?',
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
          'questions': 'Questions',
          'surveys': 'Surveys',
          'data_sets': 'Data Sets',
          'studies': 'Studies',
          'all': 'All',
          'related_publications': 'Publications',
          'instruments': 'Instruments'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', searchTranslations);
  });
