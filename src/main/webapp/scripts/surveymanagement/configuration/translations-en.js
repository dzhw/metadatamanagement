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
            'missing-number': 'Survey {{ index }} does not contain a Number and has not been saved!',
            'unable-to-upload-image-file': 'Image file "{{ file }}" could not be uploaded!',
            'unable-to-read-image-file': 'Image file "{{ file }}" could not be read!',
            'upload-terminated': 'Finished upload of {{ totalSurveys }} Surveys, {{ totalImages }} Images and {{totalAttachments}} Attachments with {{ totalErrors }} errors.',
            'unable-to-delete': 'The Surveys could not be deleted!',
            'image-file-not-found': 'Image file "{{ file }}" could not be found!',
            'duplicate-survey-number': 'The number ({{ number }}) of Survey {{ index }} has already been used.',
            'cancelled': 'Surveys upload cancelled!'
          },
          'survey-attachment': {
            'not-saved': 'Attachment "{{ id }}" has not been saved.',
            'missing-survey-number': 'Attachment {{ index }} does not have an survey number and has not been saved.',
            'missing-filename': 'Attachment {{ index }} does not have a filename and has not been saved.',
            'file-not-found': 'The File {{ filename }} was not found and has not been saved.'
          }
        },
        'detail': {
          'title': '{{ title }} ({{ surveyId }})',
          'survey': 'Survey',
          'surveys': 'Surveys',
          'surveys-same-study': 'All Surveys of the same Study',
          'survey-informations': 'Survey Informations',
          'related-information': 'Related Information',
          'related-objects': 'Related Objects',
          'field-period': 'Field Period',
          'population': 'Population',
          'survey-method': 'Survey Method',
          'sample': 'Sample',
          'not-found': 'The {{id}} references to an unknown Survey.',
          'not-found-references': 'The id {{id}} has no References to Surveys.',
          'no-related-surveys': 'No related Surveys.',
          'related-surveys': 'Related Surveys',
          'grossSampleSize': 'Gross Sample Size',
          'sampleSize': 'Net Sample Size',
          'responseRate': 'Response Rate',
          'response-rate-informations': 'Further information about the Response Rate',
          'attachments': {
            'table-title': 'Documents related to the Survey',
            'title': 'Title',
            'description': 'Description',
            'language': 'Document Language',
            'file': 'File'
          },
          'not-released-toast': 'Survey "{{ id }}" has not yet been released to all users!'
        },
        'error': {
          'survey': {
            'id': {
              'valid-survey-id-name': 'The RDC-ID of the Survey is not valid for the Pattern: "sur-" + {RDCID} + "-sy" + {Number} + "!".',
              'not-empty': 'The RDC-ID of the Survey must not be empty!',
              'size': 'The max length of the RDC-ID is 128 signs.',
              'pattern': 'Use only alphanumeric signs, german umlauts, ß and space, underscore, excemation sign and minus for the RDC-ID.'
            },
            'title': {
              'i18n-string-size': 'The max length of the title is 128 signs.'
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
              'not-null': 'The Population of a Survey must not be empty!',
              'i18n-string-not-empty': 'The Population of the Survey has to be given for one language.',
              'i18n-string-size': 'The max length of the population of the survey is 2048 signs.'
            },
            'sample': {
              'not-null': 'The Sample of a Survey must not be empty!',
              'i18n-string-not-empty': 'The Sample of the Survey has to be given for one language.',
              'i18n-string-size': 'The max length of the sample of the survey is 2048 signs.'
            },
            'survey-method': {
              'not-null': 'The Survey-Method must not be empty!',
              'i18n-string-not-empty': 'The Survey-Method has to be given for one language.',
              'i18n-string-size': 'The max length of the Survey-Method is 128 signs.'
            },
            'gross-sample-size': {
              'not-null': 'The Gross Sample Size of a Survey must not be empty!'
            },
            'sample-size': {
              'not-null': 'The Sample Size of a Survey must not be empty!'
            },
            'unique-survey-number': 'The Number of a Survey has to be unique within a Data Acquisition Project!',
            'number': {
              'not-null': 'The Number of the Survey must not be empty!'
            },
            'response-rate': {
              'not-null': 'The Response Rate of a Survey must not be empty!'
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
              'i18n-string-size': 'The description is mandatory in both languages and must not contain more than 128 characters.',
              'i18n-string-not-empty':'The description must not be empty!'
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
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
