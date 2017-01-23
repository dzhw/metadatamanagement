'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'data-set-management': {
        'log-messages': {
          'data-set': {
            'saved': 'Dataset with RDC-ID {{ id }} was saved successfully!',
            'not-saved': 'Dataset with RDC-ID {{ id }} has not been saved!',
            'missing-id': 'Dataset {{ index }} does not contain a RDC-ID and has not been saved!',
            'upload-terminated': 'Finished upload of {{ total }} Data Sets with {{ errors }} errors.',
            'cancelled': 'Datasets upload cancelled!',
            'unable-to-delete': 'The Data Sets could not be deleted!',
            'sub-data-set': {
              'number-of-observations-parse-error': 'Number Of Observations of Sub Data Set {{name}} is not a Number',
              'number-of-analyzed-variables-parse-error': 'Number Of Analyzed Variables of Sub Data Set {{name}} is not a Number!'
            }
          },
          'tex': {
            'upload-terminated': 'Tex file upload terminated!',
            'saved': 'Tex file was created successfully!',
            'cancelled': 'Tex file creation cancelled!'
          }
        },
        'home': {
          'title': 'Data Sets'
        },
        'detail': {
          'title': '{{ description }} ({{ dataSetId }})',
          'data-set': 'Data Set',
          'data-sets': 'Data Sets',
          'type': 'Typ',
          'data-set-informations': 'Data Set Informations',
          'related-information': 'Related Information',
          'data-set-same-study': 'Data Sets of the same Study',
          'description': 'Description',
          'not-found': 'The {{id}} references to an unknown Data Set.',
          'not-found-references': 'The id {{id}} has no References to Data Sets.',
          'sub-data-sets': {
            'title': 'Sub Data Sets',
            'name': 'Name',
            'accessWay': 'Data Product',
            'description': 'Description',
            'numberOfAnalyzedVariables': 'Analyzable Variables',
            'numberOfObservations': 'Observations'
          },
          'content': {
            'true': 'Click to maximize',
            'false': 'Click to minimize'
          },
          'generate-variable-report': 'Generate Variable report',
          'no-related-data-sets': 'No related Data Sets.',
          'related-data-sets': 'Related Data Sets'
        },
        'error': {
          'files-in-template-zip-incomplete': 'The uploaded files for the data set report were not complete. The missing file is: {{invalidValue}}',
          'tex-template-error': 'The generation of the data set report failed. There was an error: {{invalidValue}}',
          'data-set': {
            'unique-data-set-number-in-project': 'The number of the Data Set is not unique within the study.',
            'id': {
              'valid-data-set-id-name': 'The RDC-ID of the Data Set is not valid for the Pattern: RDCID-ds{Number}',
              'not-empty': 'The RDC-ID of the Data Set must not be empty!',
              'size': 'The max length of the RDC-ID is 128 signs.',
              'pattern': 'Use only alphanumeric signs, german umlauts, ß and space, underscore and minus for the RDC-ID.'
            },
            'description': {
              'i18n-string-size': 'The max length of the description is 2048 signs.'
            },
            'data-acquisition-project': {
              'id': {
                'not-empty': 'The RDC-ID of the Data Acquisition Project for the Data Set must not be empty!'
              }
            },
            'survey-numbers': {
              'not-empty': 'The Data Set must contain at least one Survey Number!'
            },
            'number': {
              'not-null': 'The Number of the Data Set must not be empty!'
            },
            'survey': {
              'ids': {
                'not-empty': 'The Data Set must reference at least one Survey!'
              }
            },
            'sub-data-sets': {
              'not-empty': 'There must be at least one Sub Data Set!',
              'access-way-unique-within-data-set': 'The Access Way of the Sub Data Sets has to be unique within the Data Set.'
            },
            'type': {
              'valid-type': 'The value of Type is invalid. Valid values are: Personal Record or Episode Record.',
              'not-null': 'The Type Set must not be empty!'
            }
          },
          'sub-data-set': {
            'name': {
              'not-empty': 'The Name of the {{index}}. Sub Data Set must not be empty!',
              'size': 'The max length of the Name the {{index}}. Sub Data Set is 32 signs.'
            },
            'description': {
              'i18n-string-not-empty': 'The Description of the {{index}}. Sub Data Set must not be empty!',
              'i18n-string-size': 'The max length of the Name of the {{index}}. Sub Data Set is 32 signs.'
            },
            'access-way': {
              'not-null': 'The value of Access Way of the {{index}}. Sub Data Set must not be empty!',
              'valid-access-way': 'The value of Access Way of the {{index}}. Sub Data Set is invalid. Valid values are: download-cuf, download-suf, remote-desktop-suf or onsite-suf.'
            }
          },
          'post-validation': {
            'data-set-has-invalid-survey-id': 'The Data Set {{id}} references an unknown Survey ({{toBereferenzedId}}).',
            'sub-data-set-has-an-accessway-which-was-not-found-in-study': 'The Sub-Data-Set {{id}} has an accessway ({{toBereferenzedId}}) which was not found in the Study.'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
