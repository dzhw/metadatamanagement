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
          'instrument': 'Survey Instrument',
          'instruments': 'Instruments',
          'instrument-informations': 'Details for the Instrument',
          'title': 'Title',
          'type': 'Type',
          'related-information': 'Related Objects',
          'no-related-instruments': 'No related Instruments.',
          'attachments': {
            'table-title': 'Documents related to the Instrument',
            'type': 'Type',
            'title': 'Title',
            'file': 'File'
          }
        },
        'error': {
          'instrument': {
            'id': {
              'not-empty': 'The RDC-ID of the Instrument must not be empty!',
              'size': 'The max length of the RDC-ID is 128 signs.',
              'pattern': 'Use only alphanumeric signs, german umlauts, ß and space, underscore and minus for the RDC-ID.'
            },
            'title': {
              'not-null': 'The titel of the Instrument must not be empty!',
              'i18n-string-size': 'The titel is mandatory in both languages and must not contain more than 128 characters.'
            },
            'description': {
              'not-null': 'The description of the Instrument must not be empty!',
              'i18n-string-size': 'The description is mandatory in both languages and must not contain more than 128 characters.'
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
            'valid-instrument-id-pattern': 'The RDC-ID of the Instrument is not valid for the Pattern: DataAcquisitionProjectId + "-" + "ins" + Number'
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
            'title': {
              'not-null': 'The titel of the attachment must not be empty!',
              'i18n-string-size': 'The titel is mandatory in both languages and must not contain more than 128 characters.'
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
