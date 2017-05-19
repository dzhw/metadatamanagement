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
          'delete-studies': 'Are you sure you want to delete the Study of the Data Acquisition Project with RDC-ID "{{ id }}"?',
          'delete-related-publications-title': 'Delete all Publications?',
          'delete-related-publications': 'Are you sure you want to delete all Publications?',
          'delete-instruments-title': 'Delete all Instruments?',
          'delete-instruments': 'Are you sure you want to delete all Instruments within the Data Acquisition Project with RDC-ID "{{ id }}"?'
        },
        'detail': {
          'search': 'Search'
        },
        'buttons': {
          'refresh-tooltip': 'Click to refresh the search results',
          'upload-variables-tooltip': 'Click to upload variables for the selected data acquisition project',
          'upload-surveys-tooltip': 'Click to upload surveys for the selected data acquisition project',
          'upload-data-sets-tooltip': 'Click to upload data sets for the selected data acquisition project',
          'upload-questions-tooltip': 'Click to upload questions for the selected data acquisition project',
          'upload-studies-tooltip': 'Click to upload a study for the selected data acquisition project',
          'upload-related-publications-tooltip': 'Click to upload publications',
          'post-validate-related-publications-tooltip': 'Click to validate the publications',
          'upload-instruments-tooltip': 'Click to upload instruments for the selected data acquisition project'
        },
        'input-label': {
          'all': 'Search for Studies, Variables, Questions, Surveys, Data Sets, Instruments and Publications...',
          'variables': 'Search for Variables...',
          'questions': 'Search for Questions...',
          'surveys': 'Search for Surveys...',
          'data-sets': 'Search for Data Sets...',
          'studies': 'Search for Studies...',
          'related-publications': 'Search for Publications...',
          'instruments': 'Search for Instruments...'
        },
        'no-results-text': {
          'all': 'No results found for your search request.',
          'variables': 'No Variables found for your search request.',
          'questions': 'No Questions found for your search request.',
          'surveys': 'No Surveys found for your search request.',
          'data-sets': 'No Data Sets found for your search request.',
          'studies': 'No Studies found for your search request.',
          'related-publications': 'No Publications found for your search request.',
          'instruments': 'No Instruments found for your search request.'
        },
        'tabs': {
          'variables': 'Variables',
          'variables-tooltip': 'Click to search for variables',
          'questions': 'Questions',
          'questions-tooltip': 'Click to search for questions',
          'surveys': 'Surveys',
          'surveys-tooltip': 'Click to search for surveys',
          'data_sets': 'Data Sets',
          'data_sets-tooltip': 'Click to search for data sets',
          'studies': 'Studies',
          'studies-tooltip': 'Click to search for studies',
          'all': 'All',
          'all-tooltip': 'Click to search for all objects',
          'related_publications': 'Publications',
          'related_publications-tooltip': 'Click to search for publications',
          'instruments': 'Instruments',
          'instruments-tooltip': 'Click to search for instruments'
        },
        'cards': {
          'question-tooltip': 'Click to show question "{{id}}"',
          'variable-tooltip': 'Click to show variable "{{id}}"',
          'data-set-tooltip': 'Click to show data set "{{id}}"',
          'instrument-tooltip': 'Click to show instrument "{{id}}"',
          'survey-tooltip': 'Click to show survey "{{id}}"',
          'study-tooltip': 'Click to show study "{{id}}"',
          'publication-tooltip': 'Click to show publication "{{id}}"'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', searchTranslations);
  });
