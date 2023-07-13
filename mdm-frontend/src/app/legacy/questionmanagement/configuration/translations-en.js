'use strict';

angular.module('metadatamanagementApp').config([
  '$translateProvider',

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
            'unable-to-read-image-file': 'Image file "{{ file }}" could not be read!',
            'cancelled': 'Upload of questions cancelled!',
            'technical-representation-success-copy-to-clipboard': 'The Technical Representation was successfully copied into the clipboard.',
            'unable-to-parse-json-file': 'The JSON file "{{file}}" in Instrument {{instrument}} does not contain valid JSON!',
            'unable-to-read-file': 'The file "{{file}}" in Instrument {{instrument}} could not be read!',
            'non-unique-index-in-instrument': 'At least two questions ({{firstQuestionId}}, {{secondQuestionId}}) have the same index "{{index}}" in an instrument.'
          },
          'question-image': {
            'not-saved': 'Image file "{{ id }}" of Instrument {{ instrument }} could not be uploaded!'
          },
          'question-image-metadata': {
            'unable-to-parse-json-file': 'The JSON file "{{file}}" of the question with number "{{questionNumber}}" does not contain valid JSON!',
            'unable-to-read-file': 'The file "{{file}}" of the question with number "{{questionNumber}}" could not be read!',
            'unable-to-read-resolution': 'The resolution of the image "{{file}}" of the question with number "{{questionNumber}}" could not be read!',
            'not-found-image-and-metadata-file': 'There is no image with the name "{{imageFilename}}" and no JSON metadata file with the name "{{metdadataFilename}}" for the question "{{questionNumber}}" in Instrument "{{instrument}}"!',
            'not-found-image-file': 'There is no image with the name "{{imageFilename}}" for the question "{{questionNumber}}" in Instrument "{{instrument}}"!',
            'not-found-metadata-file': 'There is no JSON metadata file with the name "{{metdadataFilename}}" for the question "{{questionNumber}}" in Instrument "{{instrument}}"!',
            'not-depending-image-metadata': 'The question with the number "{{questionNumber}}" in Instrument "{{instrument}}" has no related image metadata. No images were uploaded for this question.'
          }
        },
        'home': {
          'title': 'Questions'
        },
        'detail': {
          'label': {
            'question': 'Question',
            'questions': 'Questions',
            'annotations': 'Annotations',
            'type': 'Question Type',
            'topic': 'Topic',
            'instruction': 'Instruction',
            'introduction': 'Introduction',
            'number': 'Question Number',
            'questionText': 'Question Text',
            'language': 'Language',
            'note': 'Note',
            'resolution': 'Resolution',
            'resolution-hint': 'Depending on the screen resolution, the question was displayed differently.',
            'has-annotations': 'This image contains additional information, which have not been visible for the interviewed person.'
          },
          'predecessors': 'Previous Questions in Questionnaire',
          'successors': 'Subsequent Questions in Questionnaire',
          'images-for-question': 'Images of this Question',
          'technical-representation': 'Technical Representation',
          'title': 'Question {{ questionNumber }}: {{ instrumentDescription }}',
          'description': 'Question Text: {{ questionText }}',
          'no-predecessors': 'No previous Questions in the Questionnaire',
          'no-successors': 'No subsequent Questions in Questionnaire',
          'not-found': 'The {{id}} references to an unknown Question.',
          'not-found-references': 'The id {{id}} has no References to Questions.',
          'copy-technical-representation-to-clipboard-tooltip': 'Click to copy the complete technical representation to clipboard',
          'show-complete-technical-representation-tooltip': {
            'true': 'Click to show the complete technical representation',
            'false': 'Click to minimize the content area of the technical representation'
          },
          'change-image-language-tooltip': 'Click to change the language of the images',
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
            'data-packages': {
              'one': 'Click to show the data package in which this question has been used'
            },
            'concepts': {
              'one': 'Click to show the concept which has been measured with this question',
              'many': 'Click to show all concepts which have been measured with this question'
            },
          }
        },
        'error': {
          'question': {
            'valid-question-id-name': 'The RDC-ID of the Question is not valid for the Pattern: {RCDID} + "-ins" + {InstrumentNumber} + "-" + {QuestionNumber} + "$".',
            'unique-question-name': 'The name of a question has to be unique within a instrument.',
            'unique-question-number': 'The number of a question has to be unique within a instrument.',
            'id': {
              'not-empty': 'The RDC-ID of an Atomic Question must not be empty!',
              'size': 'The max length of the RDC-ID is 512 signs.',
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
            'topic': {
              'i18n-string-size': 'The max length of the topic is 2048 signs.'
            },
            'data-acquisition-project-id': {
              'not-empty': 'The RDC-ID of the Project must not be empty!'
            },
            'data-package-id': {
              'not-empty': 'The Data Package-ID of of the Question must not be empty!'
            },
            'instrument-number': {
              'not-null': 'The Number of the Instrument must not be empty!'
            },
            'annotations': {
              'i18n-string-size': 'The max length of the annotations is 2048 signs.'
            },
            'concept-ids': {
              'not-exists': 'There is no Concept with ID "{{invalidValue}}".'
            }
          },
          'question-image-metadata': {
            'image-type': {
              'not-null': 'The image type of the question must not be empty.',
              'valid-question-image-type': 'The image type of a question must be PNG.'
            },
            'language': {
              'not-empty': 'The language of the question image metadata must not be empty.',
              'size': 'The max length of the language of the question image metadata 32 signs.',
              'not-supported': 'The language of the attachment must be a two-letter abbreviation according to ISO 639-1!'
            },
            'file-name': {
              'not-empty': 'The filename of the question image metadata must not be empty.',
              'not-valid': 'The filename is invalid.'
            },
            'contains-annotations': {
              'not-null': 'The boolean value "Contains Annotations" of the question image metadata must not be empty.'
            },
            'index-in-question': {
              'not-null': 'The index of the image of the question must not be empty.'
            },
            'data-acquisition-project-id': {
              'not-empty': 'The RDC-ID of the Project must not be empty!'
            },
            'question-id': {
              'not-empty': 'The question id must not be empty!'
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
        },
        'edit': {
          'all-questions-deleted-toast': 'All questions of the Data Acquisition Project "{{id}}" have been deleted.'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  }]);
