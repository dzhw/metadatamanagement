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
          'delete-questions-title': 'Replace all Questions?',
          'delete-questions': 'Are you sure you want to replace all Questions within the Data Acquisition Project with RDC-ID "{{ id }}"?',
          'delete-data-sets-title': 'Replace all Data Sets?',
          'delete-data-sets': 'Are you sure you want to replace all Data Sets within the Data Acquisition Project with RDC-ID "{{ id }}"?',
          'delete-studies-title': 'Replace Study?',
          'delete-studies': 'Are you sure you want to replace the Study of the Data Acquisition Project with RDC-ID "{{ id }}"?',
          'delete-related-publications-title': 'Replace all Publications?',
          'delete-related-publications': 'Are you sure you want to replace all Publications?',
          'delete-instruments-title': 'Replace all Instruments?',
          'delete-instruments': 'Are you sure you want to replace all Instruments within the Data Acquisition Project with RDC-ID "{{ id }}"?'
        },
        'search-result': {
          'dataPackageSearch': 'Search "{{ searchQuery }}" in Data Package'
        },
        'detail': {
          'search': 'Data Search',
          'dataPackageSearch': 'Data Search',
          'noresult': 'No search results found.',
          'versions': 'Versions',
          'access-ways': 'Access ways',
          'cart': 'Note for application'
        },
        'buttons': {
          'refresh-tooltip': 'Click to refresh the search results',
          'upload-variables-tooltip': 'Click to upload variables for the selected Data Acquisition Project',
          'upload-surveys-tooltip': 'Click to upload surveys for the selected Data Acquisition Project',
          'upload-data-sets-tooltip': 'Click to upload data sets for the selected Data Acquisition Project',
          'upload-questions-tooltip': 'Click to upload questions for the selected Data Acquisition Project',
          'upload-studies-tooltip': 'Click to upload the study with attachments',
          'upload-or-create-studies-tooltip': 'Create the study',
          'upload-or-create-surveys-tooltip': 'Create one survey',
          'upload-or-create-concepts-tooltip': 'Create one concept',
          'upload-or-create-instruments-tooltip': 'Create one instrument',
          'upload-or-create-data-sets-tooltip': 'Create one data set',
          'upload-or-edit-studies-tooltip': 'Click to edit the study',
          'create-study-tooltip': 'Click to create the study',
          'create-concept-tooltip': 'Click to create the concept',
          'create-survey-tooltip': 'Click to create a new survey',
          'edit-study-tooltip': 'Click to edit the study',
          'edit-concept-tooltip': 'Click to edit the concept',
          'edit-survey-tooltip': 'Click to edit the survey',
          'delete-survey-tooltip': 'Click to delete the survey',
          'delete-concept-tooltip': 'Click to delete the concept',
          'create-instrument-tooltip': 'Click to create a new instrument',
          'edit-instrument-tooltip': 'Click to edit the instrument',
          'delete-instrument-tooltip': 'Click to delete the instrument',
          'delete-all-instruments-tooltip': 'Click, to delete all instruments of the Data Acquisition Project',
          'create-data-set-tooltip': 'Click to create a new data set',
          'edit-data-set-tooltip': 'Click to edit the data set',
          'delete-data-set-tooltip': 'Click to delete the data set',
          'upload-related-publications-tooltip': 'Click to upload publications',
          'upload-instruments-tooltip': 'Click to upload instruments for the selected Data Acquisition Project',
          'previous-search-result-tooltip': 'Click (or CTRL+"\u21E6") to show search result {{ index }} ({{ id }})',
          'next-search-result-tooltip': 'Click (or CTRL+"\u21E8") to show search result {{ index }} ({{ id }})',
          'delete-all-studies-tooltip': 'Click, to delete the study of the Data Acquisition Project',
          'delete-all-questions-tooltip': 'Click, to delete all questions of the Data Acquisition Project',
          'delete-all-variables-tooltip': 'Click, to delete all variables of the Data Acquisition Project',
          'delete-all-surveys-tooltip': 'Click, to delete all surveys of the Data Acquisition Project',
          'delete-all-data-sets-tooltip': 'Click, to delete all data sets of the Data Acquisition Project',
          'edit-surveys-tooltip': 'Click to edit the existing surveys',
          'edit-instruments-tooltip': 'Click to edit the existing instruments',
          'edit-data-sets-tooltip': 'Click to edit the existing data sets',
          'edit-publications-tooltip': 'Click to add publications to the study or to remove them',
          'delete-publications-tooltip': 'Click to remove all publications from the study of the current project'
        },
        'input-label': {
          'all': 'Search for Studies, Variables, Questions, Surveys, Data Sets, Instruments and Publications...',
          'variables': 'Search for Variables...',
          'questions': 'Search for Questions...',
          'surveys': 'Search for Surveys...',
          'data-sets': 'Search for Data Sets...',
          'studies': 'Search for Studies...',
          'data-packages': 'Search for Data Packages...',
          'related-publications': 'Search for Publications...',
          'instruments': 'Search for Instruments...',
          'concepts': 'Search for Concepts...'
        },
        'no-results-text': {
          'all': 'No results found for your search request.',
          'variables': 'No Variables found for your search request.',
          'questions': 'No Questions found for your search request.',
          'surveys': 'No Surveys found for your search request.',
          'data-sets': 'No Data Sets found for your search request.',
          'studies': 'No Studies found for your search request.',
          'data-packages': 'No Data packages found for your search request.',
          'related-publications': 'No Publications found for your search request.',
          'instruments': 'No Instruments found for your search request.',
          'concepts': 'No Concepts found for your search request.',
          'sponsor': 'No Instruments found for your search request.'
        },
        'tabs': {
          'variables': 'Variables',
          'variables-found': '{number} {number, plural, =0{variables} =1{variable} other{variables}} found.',
          'variables-tooltip': 'Click to search for variables',
          'questions': 'Questions',
          'questions-found': '{number} {number, plural, =0{questions} =1{question} other{questions}} found.',
          'questions-tooltip': 'Click to search for questions',
          'surveys': 'Surveys',
          'surveys-found': '{number} {number, plural, =0{surveys} =1{survey} other{surveys}} found.',
          'surveys-tooltip': 'Click to search for surveys',
          'data_sets': 'Data Sets',
          'data_sets-found': '{number} {number, plural, =0{data sets} =1{data set} other{data sets}} found.',
          'data_sets-tooltip': 'Click to search for data sets',
          'studies': 'Studies',
          'studies-found': '{number} {number, plural, =0{studies} =1{study} other{studies}} found.',
          'studies-tooltip': 'Click to search for studies',
          'concepts': 'Concepts',
          'concepts-found': '{number} {number, plural, =0{concepts} =1{concept} other{concepts}} found.',
          'concepts-tooltip': 'Click to search for concepts',
          'data-packages': 'Data Packages',
          'data-packages-found': '{number} {number, plural, =0{data packages} =1{data package} other{data packages}} found.',
          'data-packages-tooltip': 'Click to search for data packages',
          'all': 'All',
          'all-tooltip': 'Click to search for all objects',
          'related_publications': 'Publications',
          'related_publications-found': '{number} {number, plural, =0{publications} =1{publication} other{publications}} found.',
          'related_publications-tooltip': 'Click to search for publications',
          'instruments': 'Instruments',
          'instruments-found': '{number} {number, plural, =0{instruments} =1{instrument} other{instruments}} found.',
          'instruments-tooltip': 'Click to search for instruments'
        },
        'cards': {
          'question-tooltip': 'Click to show question "{{id}}"',
          'variable-tooltip': 'Click to show variable "{{id}}"',
          'data-set-tooltip': 'Click to show data set "{{id}}"',
          'instrument-tooltip': 'Click to show instrument "{{id}}"',
          'survey-tooltip': 'Click to show survey "{{id}}"',
          'study-tooltip': 'Click to show study "{{id}}"',
          'publication-tooltip': 'Click to show publication "{{id}}"',
          'concept-tooltip': 'Click to show concept "{{id}}"'
        },
        'filter': {
          'study': 'Study',
          'concept': 'Concept',
          'data-set': 'Data Set',
          'question': 'Question',
          'related-publication': 'Publication',
          'panel-identifier': 'Panel Identifier',
          'derived-variables-identifier': 'Derived Variables',
          'access-way': 'Access Way',
          'instrument': 'Instrument',
          'variable': 'Variable',
          'survey': 'Survey',
          'study-series': 'Study Series',
          'study-series-de': 'Study Series',
          'study-series-en': 'Study Series',
          'institution-de': 'Institution',
          'institution-en': 'Institution',
          'sponsor-de': 'Sponsor',
          'sponsor-en': 'Sponsor',
          'survey-method-de': 'Survey Method',
          'survey-method-en': 'Survey Method',
          'floating-label': {
            'survey': 'By which survey do you want to filter?',
            'concept': 'By which concept do you want to filter?',
            'instrument': 'By which instrument do you want to filter?',
            'study': 'By which study do you want to filter?',
            'data-set': 'By which data set do you want to filter?',
            'related-publication': 'By which publication do you want to filter?',
            'panel-identifier': 'By which panel identifier of the variables do you want to filter?',
            'derived-variables-identifier': 'By which derived variables do you want to filter?',
            'access-way': 'By which access way do you want to filter?',
            'variable': 'By which variable do you want to filter?',
            'question': 'By which question do you want to filter?',
            'study-series': 'By which study series do you want to filter?',
            'institution': 'By which institute do you want to filter?',
            'sponsor': 'By which sponsor do you want to filter?',
            'survey-method': 'By which survey method do you want to filter?'
          },
          'input-label': {
            'studies': 'Select filters for Study Search...',
            'concepts': 'Select filters for Concept Search...',
            'surveys': 'Select filters for Survey Search...',
            'instruments': 'Select filters for Instrument Search...',
            'questions': 'Select filters for Question Search...',
            'data_sets': 'Select filters for Data Set Search...',
            'variables': 'Select filters for Variable Search...',
            'related_publications': 'Select filters for Publication Search...'
          },
          'clear-filters-tooltip': 'Click to unselect all filters',
          'uncollapse-filters-tooltip': {
            'true': 'Click to show selected filters',
            'false': 'Click to hide selected filters'
          },
          'filter-search-input-label': 'Filter by...',
          'study-filter': {
            'not-found': 'No study found!',
            'no-valid-selected': 'No valid study selected!'
          },
          'concept-filter': {
            'not-found': 'No concept found!',
            'no-valid-selected': 'No valid concept selected!'
          },
          'survey-filter': {
            'not-found': 'No survey found!',
            'no-valid-selected': 'No valid survey selected!'
          },
          'instrument-filter': {
            'not-found': 'No instrument found!',
            'no-valid-selected': 'No valid instrument selected!'
          },
          'question-filter': {
            'not-found': 'No question found!',
            'no-valid-selected': 'No valid question selected!'
          },
          'data-set-filter': {
            'not-found': 'No data set found!',
            'no-valid-selected': 'No valid data set selected!'
          },
          'variable-filter': {
            'not-found': 'No variable found!',
            'no-valid-selected': 'No valid variable selected!'
          },
          'related-publication-filter': {
            'not-found': 'No publication found!',
            'no-valid-selected': 'No valid publication selected!'
          },
          'panel-identifier-filter': {
            'not-found': 'No panel identifier found!',
            'no-valid-selected': 'No valid panel identifier selected!'
          },
          'derived-variables-identifier-filter': {
            'not-found': 'No derived variables identifier found!',
            'no-valid-selected': 'No valid derived variables identifier selected!'
          },
          'access-way-filter': {
            'not-found': 'No access way found!',
            'no-valid-selected': 'No valid access way selected!'
          },
          'study-series-filter': {
            'not-found': 'No existing study series found!',
            'no-valid-selected': 'No valid study series selected!'
          },
          'sponsor-filter': {
            'not-found': 'No existing sponsor found!',
            'no-valid-selected': 'No valid sponsor selected!'
          },
          'institution-filter': {
            'not-found': 'No existing institution found!',
            'no-valid-selected': 'No valid institution selected!'
          },
          'survey-method-filter': {
            'not-found': 'No existing survey method found!',
            'no-valid-selected': 'No valid survey method selected!'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', searchTranslations);
  });
