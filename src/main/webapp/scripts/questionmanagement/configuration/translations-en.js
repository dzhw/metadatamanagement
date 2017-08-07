'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'question-management': {
        'log-messages': {
          'question': {
            'saved': 'Question with RDC-ID {{ id }} was saved successfully!',
            'not-saved': 'Question with RDC-ID {{ id }} has not been saved!',
            'missing-id': 'Question {{ index }} does not contain a RDC-ID and has not been saved!',
            'upload-terminated': 'Finished upload of {{ totalQuestions }} Questions and {{ totalImages }} Images with {{totalWarnings}} warnings and {{ totalErrors }} errors.',
            'unable-to-delete': 'The questions could not be deleted!',
            'not-found-image-file': 'There is no image for the question "{{questionNumber}}" in Instrument {{instrument}}!',
            'unable-to-upload-image-file': 'Image file "{{ file }}" could not be uploaded!',
            'unable-to-read-image-file': 'Image file "{{ file }}" could not be read!',
            'technical-representation-success-copy-to-clipboard': 'The Technical Representation was successfully copied into the clipboard.',
            'question-has-no-image': 'No image was found for the question {{id}}.',
            'unable-to-parse-json-file': 'The JSON file "{{file}}" in Instrument {{instrument}} does not contain valid JSON!',
            'unable-to-read-file': 'The file "{{file}}" in Instrument {{instrument}} could not be read!',
            'non-unique-index-in-instrument': 'At least two questions ({{firstQuestionId}}, {{secondQuestionId}}) have the same index "{{index}}" in an instrument.'
          }
        },
        'home': {
          'title': 'Questions'
        },
        'detail': {
          'title': 'Question {{ questionNumber }}: {{ instrumentDescription }} ({{ questionId }})',
          'question': 'Question',
          'questions': 'Questions',
          'annotations': 'Annotations',
          'predecessors': 'Previous Questions in Questionnaire',
          'successors': 'Subsequent Questions in Questionnaire',
          'no-predecessors': 'No previous Questions in the Questionnaire',
          'no-successors': 'No subsequent Questions in Questionnaire',
          'technical-representation': 'Technical Representation',
          'related-information': 'Related Information',
          'not-found': 'The {{id}} references to an unknown Question.',
          'not-found-references': 'The id {{id}} has no References to Questions.',
          'no-related-questions': 'No related Questions.',
          'related-questions': 'Related Questions',
          'copy-technical-representation-to-clipboard-tooltip': 'Click to copy the complete technical representation to clipboard',
          'show-complete-technical-representation-tooltip': {
            'true': 'Click to show the complete technical representation',
            'false': 'Click to minimize the content area of the technical representation'
          },
          'type': 'Question Type',
          'topic': 'Topic',
          'instruction': 'Instruction',
          'introduction': 'Introduction',
          'number': 'Question Number',
          'questionText': 'Question Text',
          'not-released-toast': 'Question "{{ id }}" has not yet been released to all users!',
          'tooltips': {
            'publications': {
              'one': 'Click to show the publication related to this question',
              'many': 'Click to show all publications related to this question'
            },
            'instruments': {
              'one': 'Click to show the instrument of this question'
            },
            'variables': {
              'one': 'Click to show the variable of this question',
              'many': 'Click to show all variables of this question'
            },
            'studies': {
              'one': 'Click to show the study in which this question has been used'
            }
          }
        },
        'error': {
          'question': {
            'valid-question-id-name': 'The RDC-ID of the Question is not valid for the Pattern: {RCDID} + "-ins" + {InstrumentNumber} + "-" + {QuestionNumber} + "$".',
            'unique-question-name': 'The name of a question has to be unique within a instrument.',
            'unique-question-number': 'The number of a question has to be unique within a instrument.',
            'id': {
              'not-empty': 'The RDC-ID of an Atomic Question must not be empty!',
              'size': 'The max length of the RDC-ID is 128 signs.',
              'pattern': 'Use only alphanumeric signs, german umlauts, ß, space, underscore, exclamation sign and minus for the RDC-ID.'
            },
            'number': {
              'not-empty': 'The number of the Question must not be empty.',
              'size': 'The max length of the number of the question is 32 signs.'
            },
            'question-text': {
              'not-null': 'The question text of the Question must not be empty!',
              'i18n-string-not-empty': 'The question text of the question must not be empty at least for one language.',
              'i18n-string-size': 'The max length of the question text is 2048 signs.'
            },
            'indexInInstrument': {
              'not-null': 'The "Index in a Instrument" of a Question must not be empty!'
            },
            'instruction': {
              'i18n-string-size': 'The max length of the instruction is 2048 signs.'
            },
            'introduction': {
              'i18n-string-size': 'The max length of the introduction is 2048 signs.'
            },
            'type': {
              'not-null': 'The type of the question must not be empty.',
              'valid-question-type': 'The type of the question has an invalid value or is inconsistent between the languages.'
            },
            'additional-question-text': {
              'i18n-string-size': 'The max length of the additional question text is 1048576 signs.'
            },
            'image-type': {
              'not-null': 'The image type of the question must not be empty.',
              'valid-question-image-type': 'The image type of a question must be PNG.'
            },
            'topic': {
              'i18n-string-size': 'The max length of the topic is 2048 signs.'
            },
            'data-acquisition-project-id': {
              'not-empty': 'The RDC-ID of the Project must not be empty!'
            },
            'study-id': {
              'not-empty': 'The Study-ID of of the Question must not be empty!'
            },
            'instrument-number': {
              'not-null': 'The Number of the Instrument must not be empty!'
            },
            'annotations': {
              'i18n-string-size': 'The max length of the annotations is 2048 signs.'
            }
          },
          'technical-representation': {
            'type': {
              'size': 'The max length of the type of the technical representation is 32 signs.',
              'not-empty': 'The type of the technical representation must not be empty.'
            },
            'language': {
              'size': 'The max length of the language of the technical representation is 32 signs.',
              'not-empty': 'The language of the technical representation must not be empty.'
            },
            'source': {
              'size': 'The max length of the source of the technical representation is 1048576 signs.',
              'not-empty': 'The source of the technical representation must not be empty.'
            }
          },
          'post-validation': {
            'question-has-invalid-instrument-id': 'The Question {{id}} references an unknown Instrument ({{toBereferenzedId}}).',
            'question-has-invalid-successor': 'The Question {{id}} references an unknown successor ({{toBereferenzedId}}).',
            'question-has-invalid-survey-id': 'The Question {{id}} references an unknown Survey ({{toBereferenzedId}}).',
            'non-unique-question-number-in-instrument': 'The Question {{id}} with the number {{number}} is not unique within the Insturment {{instrumentId}}.'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
