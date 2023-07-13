'use strict';

angular.module('metadatamanagementApp').config([
  '$translateProvider',

  function($translateProvider) {
    var translations = {
      //jscs:disable
      'survey-management': {
        'log-messages': {
          'survey': {
            'saved': 'Survey with RDC-ID {{ id }} was saved successfully!',
            'not-saved': 'Survey with RDC-ID {{ id }} has not been saved!',
            'missing-number': 'Survey from the Excel document in the worksheet "surveys" in the line {{ index }} does not contain a Number and has not been saved!',
            'upload-terminated': 'Finished upload of {{ totalSurveys }} Surveys, {{ totalImages }} Images and {{totalAttachments}} Attachments with {{ totalErrors }} errors.',
            'unable-to-upload-image-file': 'Image file "{{ file }}" could not be uploaded!',
            'unable-to-read-image-file': 'Image file "{{ file }}" could not be read!',
            'image-file-not-found': 'Image file "{{ file }}" could not be found!',
            'unable-to-delete': 'The Surveys could not be deleted!',
            'cancelled': 'Surveys upload cancelled!',
            'duplicate-survey-number': 'The number ({{ number }}) of Survey from the Excel document in the worksheet "surveys" in the line {{ index }} has already been used.'
          },
          'survey-attachment': {
            'missing-survey-number': 'Attachment of a survey from the Excel document in the worksheet "attachments" in the line {{ index }} does not have an survey number and has not been saved.',
            'missing-filename': 'Attachment of a survey from the Excel document in the worksheet "attachments" in the line {{ index }} does not have a filename and has not been saved.'
          }
        },
        'detail': {
          'label': {
            'survey': 'Survey',
            'surveys': 'Surveys',
            'surveys-same-data-package': 'All Surveys of this Data Package',
            'field-period': 'Field Period',
            'population': 'Population',
            'unit': 'Survey Unit',
            'geographic-coverage': 'Geographic Coverage',
            'geographic-coverages': 'Geographic Coverages',
            'data-type': 'Survey Data Type',
            'survey-method': 'Survey Method',
            'sample': 'Sampling Procedure',
            'annotations': 'Annotations',
            'grossSampleSize': 'Gross Sample Size',
            'sampleSize': 'Net Sample Size',
            'responseRate': 'Response Rate',
            'serial-number': 'Serial Number',
            'attachments': {
              'title': 'Title',
              'description': 'Description',
              'language': 'Document Language',
              'file': 'File'
            }
          },
          'attachments': {
            'table-title': 'Documents related to the Survey',
            'attachment-deleted-toast': 'Document "{{ filename }}" has been deleted!',
            'delete-attachment-tooltip': 'Click to delete document "{{ filename }}"!',
            'edit-attachment-tooltip': 'Click to edit the metadata for document "{{ filename }}".',
            'select-attachment-tooltip': 'Click to select document "{{ filename }}" for moving it up or down.',
            'move-attachment-down-tooltip': 'Click to move the selected document down.',
            'move-attachment-up-tooltip': 'Click to move the selected document up.',
            'save-attachment-order-tooltip': 'Click to save the modified order of the documents.',
            'attachment-order-saved-toast': 'The modified order of the documents has been saved.',
            'add-attachment-tooltip': 'Click to add a new document to this survey.',
            'edit-title': 'Modify Document "{{ filename }}" of Survey "{{ surveyId }}"',
            'create-title': 'Add new Document to Survey "{{ surveyId }}"',
            'change-file-tooltip': 'Click to choose a file.',
            'open-choose-previous-version-tooltip': 'Click to restore a previous version of the metadata.',
            'current-version-restored-toast': 'Current version of the metadata for document "{{ filename }}" has been restored.',
            'previous-version-restored-toast': 'Previous version of the metadata for document "{{ filename }}" can be saved now.',
            'language-not-found': 'No valid language found!',
            'save-survey-before-adding-attachment': 'The Survey has to be saved to enable attaching documents.',
            'hints': {
              'filename': 'Choose a file which you want to attach to the survey.'
            }
          },
          'title': '{{ title }}',
          'description': 'Population: {{ population }}',
          'response-rate-information': 'Further Information about the Response Rate',
          'not-found': 'The {{id}} references to an unknown Survey.',
          'not-found-references': 'The id {{id}} has no References to Surveys.',
          'response-rate-information-alt-text': 'Image to display further information about the response rate',
          'not-released-toast': 'Survey "{{ id }}" has not yet been released to all users!',
          'tooltips': {
            'surveys': {
              'many': 'Click to show all surveys of this data package'
            },
            'data-sets': {
              'one': 'Click to show the data set of this survey',
              'many': 'Click to show all data sets of this survey'
            },
            'publications': {
              'one': 'Click to show the publication related to this survey',
              'many': 'Click to show all publications related to this survey'
            },
            'instruments': {
              'one': 'Click to show the instrument used by this survey',
              'many': 'Click to show all instruments used by this survey'
            },
            'data-packages': {
              'one': 'Click to show the data package of this survey'
            },
            'questions': {
              'one': 'Click to show the question of this survey',
              'many': 'Click to show all questions of this survey'
            },
            'concepts': {
              'one': 'Click to show the concept which has been measured in this survey',
              'many': 'Click to show all concepts which have been measured in this survey'
            },
            'show-serial-number-help': 'Click to show an explanation of the ordinal number.'
          },
          'serial-number-info': {
            'title': 'Serial Number',
            'content': '<p>The serial number of the survey represents the number of the survey as it is implemented in the survey design (e.g. number of the panel wave).</p>',
            'close-tooltip': 'Click to close this explanation.'
          }
        },
        'error': {
          'survey': {
            'id': {
              'valid-survey-id-name': 'The RDC-ID of the Survey is not valid for the Pattern: "sur-" + {RDCID} + "-sy" + {Number} + "$".',
              'not-empty': 'The RDC-ID of the Survey must not be empty!',
              'size': 'The max length of the RDC-ID is 512 signs.',
              'pattern': 'Use only alphanumeric signs, german umlauts, ß and space, underscore, excemation sign and minus for the RDC-ID.'
            },
            'title': {
              'i18n-string-size': 'The max length of the title is 2048 signs.',
              'i18n-string-entire-not-empty': 'The title of the survey has to be given in all languages.'
            },
            'field-period': {
              'not-null': 'The Field Period of a Survey must not be empty!'
            },
            'data-acquisition-project': {
              'id': {
                'not-empty': 'The RDC-ID of the Data Acquisition Project for the Survey must not be empty!'
              }
            },
            'population': {
              'not-null': 'The population of a survey must not be empty!'
            },
            'sample': {
              'not-null': 'The sampling procedure of a survey must not be empty!'
            },
            'serial-number': {
              'not-null': 'The serial number of a survey must not be empty!',
              'min': 'The serial number has to be 1 or higher.',
              'invalid-number': 'Please enter a valid number (less than 2,147,483,648).'
            },
            'response-rate': {
              'min': 'The response rate must not be less than 0%.',
              'max': 'The response rate must not be greater than 100%.',
              'invalid-number': 'Please enter a valid number.'
            },
            'survey-method': {
              'not-null': 'The survey method must not be empty!',
              'i18n-string-entire-not-empty': 'The survey method must not be empty for both languages.',
              'i18n-string-size': 'The max length of the survey method is 512 signs.'
            },
            'sample-size': {
              'min': 'The sample size must not be less than 0.',
              'max': 'The net sample size must be less than or equal to the gross sample size and less than 2,147,483,648.',
              'not-null': 'The sample size of a survey must not be empty!',
              'invalid-number': 'Please enter a valid number.'
            },
            'gross-sample-size': {
              'min': 'The gross sample size must be greater than or equal to the net sample size. It can be empty.',
              'invalid-number': 'Please enter a valid number (less than 2,147,483,648).'
            },
            'unique-survey-number': 'The Number of a survey has to be unique within a Data Acquisition Project!',
            'number': {
              'not-null': 'The Number of the Survey must not be empty!'
            },
            'annotations': {
              'i18n-string-size': 'The max length of the annotations is 2048 signs.'
            },
            'data-type': {
              'not-null': 'The Survey Data Type of the survey must not be empty.',
              'valid-data-type': 'The Survey Data Type of the survey allows only following valus: de = "Quantitative Daten" and en = "Quantitative Data" or de = "Qualitative Daten" and en = "Qualitative Data"'
            }
          },
          'population': {
            'description': {
              'not-null': 'The description of the Population must not be empty!',
              'i18n-string-not-empty': 'The description of the population must not be empty for both languages.',
              'i18n-string-size': 'The max length of the population description is 2048 signs.'
            }
          }
        },
        'edit': {
          'edit-page-title': 'Edit Survey {{surveyId}}',
          'create-page-title': 'Create Survey {{surveyId}}',
          'success-on-save-toast': 'Survey {{surveyId}} has been saved successfully.',
          'error-on-save-toast': 'An error occurred during saving of Survey {{surveyId}}!',
          'survey-has-validation-errors-toast': 'The Survey has not been saved because there are invalid fields!',
          'previous-version-restored-toast': 'Previous version of Survey {{ surveyId }} can be saved now.',
          'current-version-restored-toast': 'Current version of Survey {{ surveyId }} has been restored.',
          'not-authorized-toast': 'You are not authorized to create or edit surveys!',
          'choose-unreleased-project-toast': 'Surveys may be edited if and only if the project is currently not released!',
          'survey-image-saved-toast': 'The graphical representation of the response rate has been saved.',
          'survey-image-deleted-toast': 'The graphical representation of the response rate has been deleted.',
          'survey-deleted-toast': 'The Survey {{ id }} has been deleted.',
          'label': {
            'edit-survey': 'Edit Survey:',
            'create-survey': 'Create Survey:',
            'title': 'Title',
            'field-period-start': 'Field Period Start',
            'field-period-end': 'Field Period End',
            'population': {
              'title': 'Title of Population',
              'description': 'Description of Population'
            }
          },
          'open-choose-previous-version-tooltip': 'Click for restoring a previous version of this survey.',
          'save-tooltip': 'Click to save this survey.',
          'choose-previous-version': {
            'next-page-tooltip': 'Click to show older versions.',
            'previous-page-tooltip': 'Click to show more recent versions.',
            'title': 'Restore Previous Version of Survey {{ surveyId }}',
            'text': 'Choose a previous version of this survey which shall be restored:',
            'cancel-tooltip': 'Click to return without choosing a previous survey version.',
            'no-versions-found': 'There are no previous versions of survey {{ surveyId }}.',
            'survey-title': 'Title',
            'lastModified': 'Modified',
            'lastModifiedBy': 'by',
            'current-version-tooltip': 'This is the current version!',
            'survey-deleted': 'The survey has been deleted!'
          },
          'choose-survey-number': {
            'title': 'Choose a survey number',
            'label': 'Available Survey Numbers',
            'ok-tooltip': 'Click to use the selected survey number.'
          },
          'response-rate-image': {
            'add-german-image-tooltip': 'Click to select a german image.',
            'add-english-image-tooltip': 'Click to select an english image.',
            'delete-german-image-tooltip': 'Click to delete the german image.',
            'delete-english-image-tooltip': 'Click to delete the english image.',
            'upload-or-delete-german-image-tooltip': 'Click to save the changes of the german image.',
            'upload-or-delete-english-image-tooltip': 'Click to save the changes of the english image.'
          },
          'hints': {
            'title': {
              'de': 'Please enter the title of this survey in German.',
              'en': 'Please enter the title of this survey in English.'
            },
            'serial-number': 'Please enter the serial number of the survey as it is implemented in the survey design (e.g. number of the panel wave).',
            'field-period-start': 'Please enter the beginning of the field period.',
            'field-period-end': 'Please enter the end of the field period.',
            'survey-method': {
              'de': 'Please describe the survey method in German.',
              'en': 'Please describe the survey method in English.'
            },
            'data-type': 'Please select the data type of this survey.',
            'population': {
              'title': {
                'de': 'Please enter a title for the population of this survey in German.',
                'en': 'Please enter a title for the population of this survey in English.'
              },
              'description': {
                'de': 'Please enter a description for the population of this survey in German.',
                'en': 'Please enter a description for the population of this survey in English.'
              }
            },
            'sample': {
              'de': 'Please describe the sampling procedure of this survey in German.',
              'en': 'Please describe the sampling procedure of this survey in English.'
            },
            'grossSampleSize': 'Please enter the gross sample size.',
            'sampleSize': 'Please enter the real (net) sample size.',
            'responseRate': 'Please enter the response rate in percent.',
            'annotations': {
              'de': 'Please provide additional annotations (in German) to this survey here.',
              'en': 'Please provide additional annotations (in English) to this survey here.'
            },
            'response-rate-image': {
              'available-after-save': 'Graphical representations of the response rate can be uploaded after saving the survey.',
              'de': 'Upload a graphical representation of the response rate in German.',
              'en': 'Upload a graphical representation of the response rate in English.'
            },
            'survey-number': 'Please select a survey number for the new survey.'
          },
          'all-surveys-deleted-toast': 'All surveys of the Data Acquisition Project "{{id}}" have been deleted.'
        },
        'sample-type-picker': {
          'label': 'Sampling Procedure',
          'error': {
            'required': 'A sampling procedure must be selected',
            'no-match': 'No matching sampling procedure found'
          }
        },
        'geographic-coverage-list': {
          'tooltip': {
            'move-item-up': 'Click to move this geographic coverage entry up',
            'move-item-down': 'Click to move this geographic coverage entry down',
            'add-geographic-coverage': 'Click to add a new geographic coverage'
          },
          'hint': {
            'empty-list': 'No geographic coverages are specified. Click on the button at the bottom left to add one.'
          },
          'errors': {
            'empty': 'At least one geographic coverage must be specified!'
          }
        },
        'geographic-coverage': {
          'tooltip': {
            'delete': 'Click to remove this geographic coverage'
          },
          'label': {
            'country': 'Country',
            'country-not-found': 'No country name matches your search query',
            'description': {
              'de': 'Optional information regarding this geographic coverage (in German)',
              'en': 'Optional information regarding this geographic coverage (in English)'
            }
          },
          'hints': {
            'country': 'Select the country where the survey was conducted.',
            'description': 'You can add additional information here.'
          },
          'errors': {
            'required': 'Please select a country!',
            'no-match': 'This is not a valid selection!',
            'maxlength': 'The additional information exceeds the maximum character count!'
          }
        },
        'unit-value-picker': {
          'label': 'Select a survey unit',
          'hints': {
            'unit': 'Please select a survey unit'
          },
          'errors': {
            'required': 'A survey unit must be selected',
            'no-match': 'No survey unit found with this name'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  }]);
