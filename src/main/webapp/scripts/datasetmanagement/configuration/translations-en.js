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
            'missing-id': 'Dataset in Excel Document in work sheet "dataSets" in line {{ index }} does not contain a RDC-ID and has not been saved!',
            'upload-terminated': 'Finished upload of {{ total }} Data Sets and {{ attachments }} Attachments with {{warnings}} warnings and {{ errors }} errors.',
            'cancelled': 'Datasets upload cancelled!',
            'unable-to-delete': 'The Data Sets could not be deleted!',
            'duplicate-data-set-number': 'The number ({{ number }}) of the Data Set in the Excel document on sheet "dataSets" in line {{ index }} has already been used.',
            'sub-data-set': {
              'number-of-observations-parse-error': 'Number Of Observations of Subdataset {{name}} is not a Number'
            }
          },
          'data-set-attachment': {
            'unknown-data-set-number': 'An Attachment of the Data Sets from the line {{ index }} in the worksheet "attachments" of the excel document has a reference to an unknown Data Set Number: {{dataSetNumber}}.'
          },
          'sub-data-set': {
            'unknown-data-set-number': 'The Subdataset from the line {{ index }} in the worksheet "subDataSets" of the excel document has a reference to an unknown Data Set Number: {{dataSetNumber}}.'
          }
        },
        'home': {
          'title': 'Data Sets'
        },
        'detail': {
          'label': {
            'data-set': 'Data Set',
            'data-sets': 'Data Sets',
            'description': 'Description',
            'type': 'Type',
            'format': 'Format',
            'annotations': 'Annotations',
            'data-set-same-data-package': 'Data Sets of this Data Package',
            'languages': 'Data available in',
            'sub-data-sets': {
              'name': 'Name',
              'data-formats': 'Data Formats',
              'accessWay': 'Access Way',
              'description': 'Description',
              'numberOfAnalyzedVariables': 'Analyzable Variables',
              'unknownNumberOfAnalyzedVariables': 'Unknown',
              'numberOfAnalyzedVariables-tooltip': 'Click to show all analyzable variables of this subdataset',
              'numberOfObservations': 'Observations',
              'numberOfEpisodes': 'Episodes'
            },
            'attachments': {
              'title': 'Title',
              'description': 'Description',
              'language': 'Document Language',
              'file': 'File'
            }
          },
          'sub-data-sets': {
            'title': 'Available Subdatasets',
            'data-formats': {
              'Stata': 'Stata',
              'R': 'R',
              'SPSS': 'SPSS',
              'Word': 'Word',
              'CSV': 'CSV'
            }
          },
          'attachments': {
            'table-title': 'Documents related to the Data Set',
            'attachment-deleted-toast': 'Document "{{ filename }}" has been deleted!',
            'delete-attachment-tooltip': 'Click to delete document "{{ filename }}"!',
            'edit-attachment-tooltip': 'Click to edit the metadata for document "{{ filename }}".',
            'select-attachment-tooltip': 'Click to select document "{{ filename }}" for moving it up or down.',
            'move-attachment-down-tooltip': 'Click to move the selected document down.',
            'move-attachment-up-tooltip': 'Click to move the selected document up.',
            'save-attachment-order-tooltip': 'Click to save the modified order of the documents.',
            'attachment-order-saved-toast': 'The modified order of the documents has been saved.',
            'add-attachment-tooltip': 'Click to add a new document to this data set.',
            'edit-title': 'Modify Document "{{ filename }}" of Data Set "{{ dataSetId }}"',
            'create-title': 'Add new Document to Data Set "{{ dataSetId }}"',
            'change-file-tooltip': 'Click to choose a file.',
            'open-choose-previous-version-tooltip': 'Click to restore a previous version of the metadata.',
            'current-version-restored-toast': 'Current version of the metadata for document "{{ filename }}" has been restored.',
            'previous-version-restored-toast': 'Previous version of the metadata for document "{{ filename }}" can be saved now.',
            'language-not-found': 'No valid language found!',
            'save-data-set-before-adding-attachment': 'The Data Set has to be saved to enable attaching documents.',
            'hints': {
              'filename': 'Choose a file which you want to attach to the data set.'
            }
          },
          'title': '{{ description }}',
          'description': 'A data set can contain data from several surveys.',
          'not-found': 'The {{id}} references to an unknown Data Set.',
          'not-found-references': 'The id {{id}} has no References to Data Sets.',
          'content': {
            'true': 'Click to maximize',
            'false': 'Click to minimize'
          },
          'generate-dataset-report-tooltip': 'Click to generate the dataset report for this dataset',
          'not-released-toast': 'Data Set "{{ id }}" has not yet been released to all users!',
          'tooltips': {
            'data-sets': {
              'same-data-sets': 'Click to show all data sets of this data package'
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
            'concepts': {
              'one': 'Click to show the concept which has been measured in this data set',
              'many': 'Click to show all concepts which have been measured in this data set'
            },
            'data-packages': {
              'one': 'Click to show the data package of this data set'
            },
            'get-data-set-tooltip': 'Click for information on data access'
          },
          'report-generation-started-toast': 'The dataset report is now being generated. You will be notified by e-mail as soon as the process is completed.'
        },
        'error': {
          'data-set': {
            'unique-data-set-number-in-project': 'The number of the Data Set is not unique within the data package.',
            'id': {
              'valid-data-set-id-name': 'The RDC-ID of the Data Set is not valid for the Pattern: "dat-" + {FDZID} + "-ds" + {Number} + "$".',
              'not-empty': 'The RDC-ID of the Data Set must not be empty!',
              'size': 'The max length of the RDC-ID is 512 signs.',
              'pattern': 'Use only alphanumeric signs, german umlauts, ß and space, underscore, exclamation sign and minus for the RDC-ID.'
            },
            'description': {
              'i18n-string-size': 'The max length of the description is 2048 signs.',
              'i18n-string-not-empty': 'The description must be specified in at least one language'
            },
            'annotations': {
              'i18n-string-size': 'The max length of the annotations is 2048 signs.'
            },
            'data-acquisition-project': {
              'id': {
                'not-empty': 'The RDC-ID of the Data Acquisition Project for the Data Set must not be empty!'
              }
            },
            'data-package': {
              'id': {
                'not-empty': 'The Data Package-ID for the Data Set must not be empty!'
              }
            },
            'survey-numbers': {
              'not-empty': 'The Data Set must be assigned to at least one survey!'
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
              'not-empty': 'There must be at least one Subdataset!',
              'access-way-unique-within-data-set': 'The Access Way of the Subdatasets has to be unique within the Data Set.'
            },
            'type': {
              'valid-type': 'The value of Data Set Type is invalid. Valid english values are: Individual Data or Spell Data. The type might be invalid in other languagses.',
              'not-null': 'The Type of a Data Set must not be empty!'
            }
          },
          'sub-data-set': {
            'name': {
              'not-empty': 'The Name of a Subdataset must not be empty!',
              'size': 'The max length of the Name of a Subdataset is 32 signs.'
            },
            'description': {
              'i18n-string-not-empty': 'The Description of a Subdataset must not be empty!',
              'i18n-string-size': 'The max length of the Description of a Subdataset is 32 signs.'
            },
            'access-way': {
              'not-null': 'The value of Access Way of a Subdataset must not be empty!',
              'valid-access-way': 'The value of Access Way of a Subdataset is invalid. Valid values are: download-cuf, download-suf, remote-desktop-suf or onsite-suf.'
            },
            'number-of-observations': {
              'not-null': 'The number of observations/episodes of a Subdataset must not be empty!',
              'invalid-number': 'Enter a positive integer (less than 2,147,483,648)!'
            },
            'data-formats': {
              'required': 'At least on data format must be specified.'
            }
          },
          'post-validation': {
            'data-set-has-invalid-survey-id': 'The Data Set {{id}} references an unknown Survey ({{toBereferenzedId}}).'
          }
        },
        'edit': {
            'edit-page-title': 'Edit Data Set {{dataSetId}}',
            'create-page-title': 'Create Data Set {{dataSetId}}',
            'success-on-save-toast': 'Data Set {{dataSetId}} has been saved successfully.',
            'error-on-save-toast': 'An error occurred during saving of Data Set {{dataSetId}}!',
            'data-set-has-validation-errors-toast': 'The Data Set has not been saved because there are invalid fields!',
            'previous-version-restored-toast': 'Previous version of Data Set {{ dataSetId }} can be saved now.',
            'current-version-restored-toast': 'Current version of Data Set {{ dataSetId }} has been restored.',
            'not-authorized-toast': 'You are not authorized to create or edit data sets!',
            'choose-unreleased-project-toast': 'Data Sets may be edited if and only if the project is currently not released!',
            'data-set-deleted-toast': 'The Data Set {{ id }} has been deleted.',
            'delete-sub-data-set-tooltip': 'Click to delete the subdataset',
            'add-sub-data-set-tooltip': 'Click to create a new subdataset',
            'move-sub-data-set-up-tooltip': 'Click to move the selected subdataset up',
            'move-sub-data-set-down-tooltip': 'Click to move the selected subdataset down',
            'label': {
                'edit-data-set': 'Edit Data Set:',
                'create-data-set': 'Create Data Set:',
                'surveys': 'Surveys *',
                'sub-data-set': {
                  'name': 'Name',
                  'access-way': 'Access Way',
                  'number-of-observations': 'Number of Observations/Episodes',
                  'description': 'Description',
                  'data-formats': 'Available Data Formats'
                }
            },
            'open-choose-previous-version-tooltip': 'Click for restoring a previous version of this data set.',
            'save-tooltip': 'Click to save this data set.',
            'choose-previous-version': {
              'title': 'Restore Previous Version of Data Set {{ dataSetId }}',
              'text': 'Choose a previous version of this data set which shall be restored:',
              'cancel-tooltip': 'Click to return without choosing a previous data set version.',
              'no-versions-found': 'There are no previous versions of data set {{ dataSetId }}.',
              'data-set-deleted': 'The data set has been deleted!'
            },
            'choose-data-set-number': {
              'title': 'Choose a data set number',
              'label': 'Available Data Set Numbers',
              'ok-tooltip': 'Click to use the selected data set number.'
            },
            'hints': {
                'description': {
                    'de': 'Please enter a short description of the data set in German.',
                    'en': 'Please enter a short description of the data set in English.'
                },
                'format': 'Select the data set format.',
                'type': 'Specify whether it is a personal or episodic data set.',
                'surveys': 'Select the surveys that result in this data set.',
                'search-surveys': 'Search surveys...',
                'no-surveys-found': 'No (further) surveys found.',
                'annotations': {
                  'de': 'Please provide additional annotations (in German) to this survey here.',
                  'en': 'Please provide additional annotations (in English) to this survey here.'
                },
                'data-set-number': 'Please select an data set number for the new data set.',
                'sub-data-set': {
                    'name': 'Specify the (file)name of the subdataset.',
                    'access-way': 'How is the subdataset available?',
                    'number-of-observations': 'How many cases or epsiodes does the subdataset contain?',
                    'description': {
                      'de': 'Enter a short description for the subdataset in German.',
                      'en': 'Enter a short description for the subdataset in English.'
                    },
                    'data-formats': 'Specify the data formats in which the subrecord is available.'
                }
            },
            'all-data-sets-deleted-toast': 'All data sets of the Data Acquisition Project "{{id}}" have been deleted.'
        },
        'create-report': {
          'title': 'Generate Dataset Report',
          'version': 'Version of the Report',
          'languages': {
            'in-german': 'German',
            'in-english': 'English'
          },
          'error': {
            'version': {
              'not-empty': 'The version must not be empty.',
              'pattern': 'The version must match the pattern "major.minor.patch" (e.g. "1.0.0").',
              'size': 'The version must not contain more than 32 characters.'
            }
          },
          'hints': {
            'version': 'Specify the version number to be displayed on the title page of the dataset report.',
            'languages': 'Specify at least one language in which the report shall be generated.'
          },
          'tooltip': {
            'cancel': 'Click to cancel the generation of the report',
            'ok': 'Click to start the generation of the report'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
