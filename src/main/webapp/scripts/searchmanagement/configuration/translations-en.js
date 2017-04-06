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
          'refresh-tooltip': 'Click to refresh search results',
          'upload-variables-tooltip': 'Click to upload variables for selected data acquisition project',
          'upload-surveys-tooltip': 'Click to upload upload Surveys for selected data acquisition project',
          'upload-data-sets-tooltip': 'Click to upload data sets for selected data acquisition project',
          'upload-questions-tooltip': 'Click to upload questions for selected data acquisition project',
          'upload-studies-tooltip': 'Click to upload study for selected data acquisition project',
          'upload-related-publications-tooltip': 'Click to upload publications',
          'post-validate-related-publications-tooltip': 'Click to validate publications',
          'upload-instruments-tooltip': 'Click to upload instruments for selected data acquisition project'
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
        'cards': {
          'question-tooltip': 'Click to navigate to question',
          'variable-tooltip': 'Click to navigate to variable',
          'data-set-tooltip': 'Click to navigate to data set',
          'instrument-tooltip': 'Click to navigate to instrument',
          'survey-tooltip': 'Click to navigate to survey',
          'study-tooltip': 'Click to navigate to study',
          'publication-tooltip': 'Click to navigate to publication'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', searchTranslations);
  });
