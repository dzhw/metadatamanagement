'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'instrument-management': {
        'log-messages': {
          'instrument': {
            'saved': 'Instrument with RDC-ID {{ id }} was saved successfully!',
            'not-saved': 'Instrument with RDC-ID {{ id }} has not been saved!',
            'missing-number': 'Instrument {{ index }} does not contain a number and has not been saved:',
            'upload-terminated': 'Finished upload of {{ totalInstruments }} Instruments and {{ totalAttachments }} Attachments with {{totalWarnings}} warnings and {{ totalErrors }} errors.',
            'unable-to-delete': 'The Instruments could not be deleted!',
            'cancelled': 'Instrument upload cancelled!',
            'duplicate-instrument-number': 'The number ({{ number }}) of Instrument {{ index }} has already been used.'
          },
          'instrument-attachment': {
            'not-saved': 'Attachment "{{ id }}" has not been saved:',
            'missing-instrument-number': 'Attachment {{ index }} does not have an instrument number and has not been saved.',
            'unknown-instrument-number': 'The number of the instrument of attachment {{ index }} does not exist. The attachment has not been saved.',
            'missing-filename': 'Attachment {{ index }} does not have a filename and has not been saved.',
            'file-not-found': 'The File {{ filename }} was not found and has not been saved.'
          }
        },
        'home': {
          'title': 'Instruments'
        },
        'detail': {
          'page-title': '{{ description }} ({{ instrumentId }})',
          'instrument': 'Instrument',
          'instruments': 'Instruments',
          'instrument-informations': 'Details for the Instrument',
          'title': 'Title',
          'subtitle': 'Subtitle',
          'type': 'Type',
          'annotations': 'Annotations',
          'related-information': 'Related Objects',
          'no-related-instruments': 'No related Instruments.',
          'attachments': {
            'table-title': 'Documents related to the Instrument',
            'type': 'Type',
            'title': 'Title',
            'description': 'Description',
            'language': 'Document Language',
            'file': 'File'
          },
          'not-released-toast': 'Instrument "{{ id }}" has not yet been released to all users!',
          'tooltips': {
            'surveys': {
              'one': 'Click to show the survey in which this instrument has been used',
              'many': 'Click to show all surveys in which this instrument has been used'
            },
            'publications': {
              'one': 'Click to show the publication related to this instrument',
              'many': 'Click to show all publications related to this instrument'
            },
            'questions': {
              'one': 'Click to show the question of this instrument',
              'many': 'Click to show all questions of this instrument'
            },
            'studies': {
              'one': 'Click to show the study in which this instrument has been used'
            }
          }
        },
        'error': {
          'instrument': {
            'id': {
              'not-empty': 'The RDC-ID of the Instrument must not be empty!',
              'size': 'The max length of the RDC-ID is 128 signs.',
              'pattern': 'Use only alphanumeric signs, german umlauts, ß and space, underscore, exclamation sign and minus for the RDC-ID.'
            },
            'title': {
              'not-null': 'The title of the Instrument must not be empty!',
              'i18n-string-size': 'The title must not contain more than 2048 characters.',
              'i18n-string-not-empty': 'The title is mandatory in one language.'
            },
            'subtitle': {
              'i18n-string-size': 'The subtitle must not contain more than 2048 characters.',
            },
            'description': {
              'not-null': 'The description of the Instrument must not be empty!',
              'i18n-string-size': 'The description is mandatory in both languages and must not contain more than 128 characters.',
              'i18n-string-not-empty': 'The description must not be empty!'
            },
            'annotations': {
              'i18n-string-size': 'The max length of the annotations is 2048 signs.'
            },
            'type': {
              'not-empty': 'The type of the Instrument must not be empty!',
              'valid': 'The type of the Instrument must be one of the following: PAPI, CAPI, CATI, CAWI'
            },
            'data-acquisition-project-id': {
              'not-empty': 'The ID of the Data Acquisition Project must not be empty!'
            },
            'instrument.unique-instrument-number': 'The Number of a Survey has to be unique within a Data Acquisition Project!',
            'survey-numbers.not-empty': 'The List of Surveys must not be empty!',
            'number': {
              'not-null': 'The Number of the Instrument must not be empty!'
            },
            'survey-id': {
              'not-empty': 'The ID of the corresponding Survey must not be empty!'
            },
            'valid-instrument-id-pattern': 'The RDC-ID of the Instrument is not valid for the Pattern: "ins-" + {DataAcquisitionProjectId} + "-" + "ins" + {Number} + "$".'
          },
          'instrument-attachment-metadata': {
            'instrument-id': {
              'not-empty': 'The ID of the corresponding Instrument must not be empty.'
            },
            'instrument-number': {
              'not-null': 'The Number of the corresponding Instrument must not be empty.'
            },
            'project-id': {
              'not-empty': 'The ID of the Data Acquisition Project must not be empty!'
            },
            'type': {
              'not-null': 'The type of the attachment must not be empty!',
              'i18n-string-size': 'The type is mandatory in both languages and must not contain more than 32 characters.',
              'valid-type': 'The type must be one of the following: Questionnaire, Question Flow, Variable Questionnaire, Other.'
            },
            'description': {
              'not-null': 'The description of the attachment must not be empty!',
              'i18n-string-size': 'The description is mandatory and must in at least one language and must not contain more than 128 characters.',
              'i18n-string-not-empty': 'The description must not be empty!'
            },
            'title': {
              'string-size': 'The title of the attachment must not contain more than 2048 characters.'
            },
            'language': {
              'not-null': 'The language of the attachment must not be empty!',
              'not-supported': 'The language of the attachment must be a two-letter abbreviation according to ISO 639-1!'
            },
            'filename': {
              'not-empty': 'The filename of the attachment must not be empty!'
            }
          },
          'post-validation': {
            'instrument-has-invalid-survey-id': 'The Instrument {{id}} references an unknown Survey ({{toBereferenzedId}}).'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
