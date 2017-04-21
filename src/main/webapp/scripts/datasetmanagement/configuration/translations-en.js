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
            'upload-terminated': 'Finished upload of {{ total }} Data Sets and {{ attachments }} Attachments with {{warnings}} warnings and {{ errors }} errors.',
            'cancelled': 'Datasets upload cancelled!',
            'unable-to-delete': 'The Data Sets could not be deleted!',
            'duplicate-data-set-number': 'The number ({{ number }}) of Data Set {{ index }} has already been used.',
            'sub-data-set': {
              'number-of-observations-parse-error': 'Number Of Observations of Sub Data Set {{name}} is not a Number'
            }
          },
          'data-set-attachment': {
            'not-saved': 'Attachment "{{ id }}" has not been saved.',
            'missing-survey-number': 'Attachment {{ index }} does not have an survey number and has not been saved.',
            'missing-filename': 'Attachment {{ index }} does not have a filename and has not been saved.',
            'file-not-found': 'The File {{ filename }} was not found and has not been saved.',
            'unknown-data-set-number': 'An Attachment of the Data Sets from the line {{ index }} of the excel document has a reference to an unknown Data Set Number: {{dataSetNumber}}.'
          },
          'sub-data-set': {
            'unknown-data-set-number': 'The Sub Data Set from the line {{ index }} of the excel document has a reference to an unknown Data Set Number: {{dataSetNumber}}.'
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
          'type': 'Type',
          'format': 'Format',
          'data-set-informations': 'Data Set Informations',
          'related-information': 'Related Information',
          'data-set-same-study': 'Data Sets of this Study',
          'description': 'Description',
          'not-found': 'The {{id}} references to an unknown Data Set.',
          'not-found-references': 'The id {{id}} has no References to Data Sets.',
          'sub-data-sets': {
            'title': 'Available Sub Data Sets',
            'name': 'Name',
            'accessWay': 'Access Way',
            'description': 'Description',
            'numberOfAnalyzedVariables': 'Analyzable Variables',
            'numberOfAnalyzedVariables-tooltip': 'Click to show all analyzable variables of this sub data set',
            'numberOfObservations': 'Observations'
          },
          'attachments': {
            'table-title': 'Documents related to the Data Set',
            'title': 'Title',
            'description': 'Description',
            'language': 'Document Language',
            'file': 'File'
          },
          'content': {
            'true': 'Click to maximize',
            'false': 'Click to minimize'
          },
          'generate-variable-report-tooltip': 'Click to generate variable report',
          'no-related-data-sets': 'No related Data Sets.',
          'related-data-sets': 'Related Data Sets',
          'not-released-toast': 'Data Set "{{ id }}" has not yet been released to all users!',
          'tooltips': {
            'data-sets': {
              'same-data-sets': 'Click to show all data sets of this study'
            },
            'surveys': {
              'one': 'Click to show the survey from which this data set has been generated',
              'many': 'Click to show all surveys from which this data set has been generated'
            },
            'publications': {
              'one': 'Click to show the publication related to this data set',
              'many': 'Click to show all publications related to this data set'
            },
            'variables': {
              'one': 'Click to show the variable of this data set',
              'many': 'Click to show all variables of this data set'
            },
            'studies': {
              'one': 'Click to show the study of this data set'
            }
          }
        },
        'error': {
          'files-in-template-zip-incomplete': 'The uploaded files for the data set report were not complete. The missing file is: {{invalidValue}}',
          'tex-template-error': 'The generation of the data set report failed. There was an error: {{invalidValue}}',
          'data-set': {
            'unique-data-set-number-in-project': 'The number of the Data Set is not unique within the study.',
            'id': {
              'valid-data-set-id-name': 'The RDC-ID of the Data Set is not valid for the Pattern: "dat-" + {FDZID} + "-ds" + {Number} + "!".',
              'not-empty': 'The RDC-ID of the Data Set must not be empty!',
              'size': 'The max length of the RDC-ID is 128 signs.',
              'pattern': 'Use only alphanumeric signs, german umlauts, ß and space, underscore, exclamation sign and minus for the RDC-ID.'
            },
            'description': {
              'i18n-string-size': 'The max length of the description is 2048 signs.'
            },
            'data-acquisition-project': {
              'id': {
                'not-empty': 'The RDC-ID of the Data Acquisition Project for the Data Set must not be empty!'
              }
            },
            'study': {
              'id': {
                'not-empty': 'The Study-ID for the Data Set must not be empty!'
              }
            },
            'survey-numbers': {
              'not-empty': 'The Data Set must contain at least one Survey Number!'
            },
            'number': {
              'not-null': 'The Number of the Data Set must not be empty!'
            },
            'format': {
              'valid-format': 'The only allowed values for the format of a data set are: wide and long.'
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
          'data-set-attachment-metadata': {
            'data-set-id': {
              'not-empty': 'The ID of the corresponding Data Set must not be empty.'
            },
            'data-set-number': {
              'not-null': 'The Number of the corresponding Data Set must not be empty.'
            },
            'project-id': {
              'not-empty': 'The ID of the Data Acquisition Project must not be empty!'
            },
            'description': {
              'not-null': 'The description of the attachment must not be empty!',
              'i18n-string-size': 'The description is mandatory and must in at least one language and must not contain more than 128 characters.',
              'i18n-string-not-empty': 'The description must not be empty!'
            },
            'title': {
              'not-null': 'The title of the attachment must not be empty!',
              'string-size': 'The title of the attachment is mandatory and must not contain more than 128 characters.'
            },
            'language': {
              'not-null': 'The language of the attachment must not be empty!',
              'not-supported': 'The language of the attachment must be a two-letter abbreviation according to ISO 639-1!'
            },
            'filename': {
              'not-empty': 'The filename of the attachment must not be empty!'
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
            'data-set-has-invalid-survey-id': 'The Data Set {{id}} references an unknown Survey ({{toBereferenzedId}}).'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
