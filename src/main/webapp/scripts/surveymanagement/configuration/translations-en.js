'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'survey-management': {
        'log-messages': {
          'survey': {
            'saved': 'Survey with RDC-ID {{ id }} was saved successfully!',
            'not-saved': 'Survey with RDC-ID {{ id }} has not been saved!',
            'missing-number': 'Survey from the Excel document in the worksheet "surveys" in the line {{ index }} does not contain a Number and has not been saved!',
            'unable-to-upload-image-file': 'Image file "{{ file }}" could not be uploaded!',
            'unable-to-read-image-file': 'Image file "{{ file }}" could not be read!',
            'upload-terminated': 'Finished upload of {{ totalSurveys }} Surveys, {{ totalImages }} Images and {{totalAttachments}} Attachments with {{ totalErrors }} errors.',
            'unable-to-delete': 'The Surveys could not be deleted!',
            'image-file-not-found': 'Image file "{{ file }}" could not be found!',
            'duplicate-survey-number': 'The number ({{ number }}) of Survey from the Excel document in the worksheet "surveys" in the line {{ index }} has already been used.',
            'cancelled': 'Surveys upload cancelled!'
          },
          'survey-attachment': {
            'not-saved': 'Attachment "{{ id }}" has not been saved.',
            'missing-survey-number': 'Attachment of a survey from the Excel document in the worksheet "attachments" in the line {{ index }} does not have an survey number and has not been saved.',
            'missing-filename': 'Attachment of a survey from the Excel document in the worksheet "attachments" in the line {{ index }} does not have a filename and has not been saved.',
            'file-not-found': 'The File {{ filename }} was not found and has not been saved.'
          }
        },
        'detail': {
          'label': {
            'survey': 'Survey',
            'surveys': 'Surveys',
            'surveys-same-study': 'All Surveys of this Study',
            'field-period': 'Field Period',
            'population': 'Population',
            'survey-method': 'Survey Method',
            'data-type': 'Survey Data Type',
            'sample': 'Sample',
            'grossSampleSize': 'Gross Sample Size',
            'sampleSize': 'Net Sample Size',
            'responseRate': 'Response Rate',
            'annotations': 'Annotations',
            'attachments': {
              'title': 'Title',
              'description': 'Description',
              'language': 'Document Language',
              'file': 'File'
            }
          },
          'attachments': {
            'table-title': 'Documents related to the Survey'
          },
          'title': '{{ title }} ({{ surveyId }})',
          'response-rate-information': 'Further Information about the Response Rate',
          'not-found': 'The {{id}} references to an unknown Survey.',
          'not-found-references': 'The id {{id}} has no References to Surveys.',
          'response-rate-information-alt-text': 'Image to display further information about the response rate',
          'not-released-toast': 'Survey "{{ id }}" has not yet been released to all users!',
          'tooltips': {
            'surveys': {
              'many': 'Click to show all surveys of this study'
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
            'studies': {
              'one': 'Click to show the study of this survey'
            }
          }
        },
        'error': {
          'survey': {
            'id': {
              'valid-survey-id-name': 'The RDC-ID of the Survey is not valid for the Pattern: "sur-" + {RDCID} + "-sy" + {Number} + "$".',
              'not-empty': 'The RDC-ID of the Survey must not be empty!',
              'size': 'The max length of the RDC-ID is 128 signs.',
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
              'not-null': 'The Population of a Survey must not be empty!'
            },
            'sample': {
              'not-null': 'The Sample of a Survey must not be empty!',
              'i18n-string-not-empty': 'The Sample of the Survey has to be given for one language.',
              'i18n-string-size': 'The max length of the sample of the survey is 2048 signs.'
            },
            'wave': {
              'not-null': 'The Wave of a Survey must not be empty!',
              'min': 'The number of waves has to be 1 or higher.'
            },
            'survey-method': {
              'not-null': 'The Survey-Method must not be empty!',
              'i18n-string-not-empty': 'The Survey-Method has to be given for one language.',
              'i18n-string-size': 'The max length of the Survey-Method is 128 signs.'
            },
            'sample-size': {
              'not-null': 'The Sample Size of a Survey must not be empty!'
            },
            'unique-survey-number': 'The Number of a Survey has to be unique within a Data Acquisition Project!',
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
            'title': {
              'not-null': 'The title of the Population must not be empty!',
              'i18n-string-not-empty': 'The title of the Population has to be given for one language.',
              'i18n-string-size': 'The max length of the population title is 128 signs.'
            },
            'description': {
              'not-null': 'The description of the Population must not be empty!',
              'i18n-string-not-empty': 'The description of the Population has to be given for one language.',
              'i18n-string-size': 'The max length of the population description is 2048 signs.'
            }
          },
          'survey-attachment-metadata': {
            'survey-id': {
              'not-empty': 'The ID of the corresponding Survey must not be empty.'
            },
            'survey-number': {
              'not-null': 'The Number of the corresponding Survey must not be empty.'
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
              'string-size': 'The title of the attachment is mandatory and must not contain more than 2048 characters.'
            },
            'language': {
              'not-null': 'The language of the attachment must not be empty!',
              'not-supported': 'The language of the attachment must be a two-letter abbreviation according to ISO 639-1!'
            },
            'filename': {
              'not-empty': 'The filename of the attachment must not be empty!'
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
          'choose-unreleased-project-toast': 'Please choose a project which is currently not released!',
          'survey-image-saved-toast': 'The graphical representation of the response rate has been saved.',
          'survey-image-deleted-toast': 'The graphical representation of the response rate has been deleted.',
          'survey-deleted-toast': 'The Survey {{ id }} has been deleted.',
          'label': {
            'edit-survey': 'Edit Survey:',
            'create-survey': 'Create Survey:',
            'title': 'Title',
            'wave': 'Wave',
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
            'wave': 'Please enter the number of the wave for this survey or keep 1 if not relevant.',
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
              'de': 'Please describe the sample of this survey in German.',
              'en': 'Please describe the sample of this survey in English.'
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
              'de': 'Upload a graphical representation (SVG) of the response rate in German.',
              'en': 'Upload a graphical representation (SVG) of the response rate in English.'
            },
            'survey-number': 'Please select a survey number for the new survey.'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
