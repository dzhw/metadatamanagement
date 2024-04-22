'use strict';

angular.module('metadatamanagementApp').config([
  '$translateProvider',
function($translateProvider) {
    var translations = {
        //jscs:disable
        'instrument-management': {
            'log-messages': {
                'instrument': {
                    'saved': 'Instrument with RDC-ID {{ id }} was saved successfully!',
                    'not-saved': 'Instrument with RDC-ID {{ id }} has not been saved!',
                    'missing-number': 'Instrument from the Excel document in the worksheet "instruments" in line {{ index }} does not contain a number and has not been saved:',
                    'upload-terminated': 'Finished upload of {{ totalInstruments }} Instruments and {{ totalAttachments }} Attachments with {{totalWarnings}} warnings and {{ totalErrors }} errors.',
                    'unable-to-delete': 'The Instruments could not be deleted!',
                    'cancelled': 'Instrument upload cancelled!',
                    'duplicate-instrument-number': 'The number ({{ number }}) of Instrument from the Excel document in the worksheet "instruments" in line {{ index }} has already been used.'
                },
                'instrument-attachment': {
                    'missing-instrument-number': 'Attachment from the Excel document in the worksheet "attachments" in the line {{ index }} does not have an instrument number and has not been saved.',
                    'unknown-instrument-number': 'The number of the instrument of an attachment from the Excel document in the worksheet in the line {{ index }} does not exist. The attachment has not been saved.',
                    'missing-filename': 'Attachment of an instrument from the Excel document in the worksheet "attachments" in line {{ index }} does not have a filename and has not been saved.'
                }
            },
            'home': {
                'title': 'Instruments'
            },
            'detail': {
                'label': {
                    'instrument': 'Instrument',
                    'instruments': 'Instruments',
                    'description': 'Description',
                    'title': 'Title',
                    'subtitle': 'Subtitle',
                    'type': 'Type',
                    'original-languages': 'Original Languages',
                    'annotations': 'Annotations',
                    'attachments': {
                        'type': 'Type',
                        'description': 'Description',
                        'language': 'Document Language',
                        'file': 'File'
                    }
                },
                'attachments': {
                    'table-title': 'Documents related to the Instrument',
                    'attachment-deleted-toast': 'Document "{{ filename }}" has been deleted!',
                    'delete-attachment-tooltip': 'Click to delete document "{{ filename }}"!',
                    'edit-attachment-tooltip': 'Click to edit the metadata for document "{{ filename }}".',
                    'select-attachment-tooltip': 'Click to select document "{{ filename }}" for moving it up or down.',
                    'move-attachment-down-tooltip': 'Click to move the selected document down.',
                    'move-attachment-up-tooltip': 'Click to move the selected document up.',
                    'save-attachment-order-tooltip': 'Click to save the modified order of the documents.',
                    'attachment-order-saved-toast': 'The modified order of the documents has been saved.',
                    'add-attachment-tooltip': 'Click to add a new document to this instrument.',
                    'add-attachment-tooltip-disabled': 'Document can only be added if list of surveys is not empty.',
                    'edit-title': 'Modify Document "{{ filename }}" of Instrument "{{ instrumentId }}"',
                    'create-title': 'Add new Document to Instrument "{{ instrumentId }}"',
                    'change-file-tooltip': 'Click to choose a file.',
                    'open-choose-previous-version-tooltip': 'Click to restore a previous version of the metadata.',
                    'current-version-restored-toast': 'Current version of the metadata for document "{{ filename }}" has been restored.',
                    'previous-version-restored-toast': 'Previous version of the metadata for document "{{ filename }}" can be saved now.',
                    'language-not-found': 'No valid language found!',
                    'save-instrument-before-adding-attachment': 'The Instrument has to be saved to enable attaching documents.',
                    'hints': {
                        'filename': 'Choose a file which you want to attach to the instrument.'
                    }
                },
                'page-title': '{{ description }}',
                'page-description': '{{ title }}',
                'not-found': 'The {{id}} references to an unknown Instrument.',
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
                    'data-packages': {
                        'one': 'Click to show the data package in which this instrument has been used'
                    },
                    'concepts': {
                        'one': 'Click to show the concept which has been measured with this instrument',
                        'many': 'Click to show all concepts which have been measured with this instrument'
                    }
                }
            },
            'error': {
                'instrument': {
                    'id': {
                        'not-empty': 'The RDC-ID of the Instrument must not be empty!',
                        'size': 'The max length of the RDC-ID is 512 signs.',
                        'pattern': 'Use only alphanumeric signs, german umlauts, ß and space, underscore, exclamation sign and minus for the RDC-ID.'
                    },
                    'title': {
                        'not-null': 'The title of the Instrument must not be empty!',
                        'i18n-string-size': 'The title must not contain more than 2048 characters.',
                        'i18n-string-not-empty': 'The title is mandatory in one language.'
                    },
                    'subtitle': {
                        'i18n-string-size': 'The subtitle must not contain more than 2048 characters.'
                    },
                    'description': {
                        'not-null': 'The description of the Instrument must not be empty!',
                        'i18n-string-size': 'The description is mandatory in both languages and must not contain more than 512 characters.',
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
                'post-validation': {
                    'instrument-has-invalid-survey-id': 'The Instrument {{id}} references an unknown Survey ({{toBereferenzedId}}).',
                    'no-instruments': 'There are no instruments in this project.'
                }
            },
            'edit': {
                'edit-page-title': 'Edit Instrument {{instrumentId}}',
                'create-page-title': 'Create Instrument {{instrumentId}}',
                'success-on-save-toast': 'Instrument {{instrumentId}} has been saved successfully.',
                'error-on-save-toast': 'An error occurred during saving of Instrument {{instrumentId}}!',
                'instrument-has-validation-errors-toast': 'The Instrument has not been saved because there are invalid fields!',
                'previous-version-restored-toast': 'Previous version of Instrument {{ instrumentId }} can be saved now.',
                'current-version-restored-toast': 'Current version of Instrument {{ instrumentId }} has been restored.',
                'not-authorized-toast': 'You are not authorized to create or edit instruments!',
                'choose-unreleased-project-toast': 'Instruments may be edited if and only if the project is currently not released!',
                'instrument-deleted-toast': 'The Instrument {{ id }} has been deleted.',
                'label': {
                    'edit-instrument': 'Edit Instrument:',
                    'create-instrument': 'Create Instrument:',
                    'surveys': 'Surveys *',
                    'concepts': 'Concepts'
                },
                'open-choose-previous-version-tooltip': 'Click for restoring a previous version of this instrument.',
                'save-tooltip': 'Click to save this instrument.',
                'choose-previous-version': {
                  'title': 'Restore Previous Version of Instrument {{ instrumentId }}',
                  'text': 'Choose a previous version of this instrument which shall be restored:',
                  'cancel-tooltip': 'Click to return without choosing a previous instrument version.',
                  'no-versions-found': 'There are no previous versions of instrument {{ instrumentId }}.',
                  'instrument-description': 'Description',
                  'instrument-deleted': 'The instrument has been deleted!'
                },
                'choose-instrument-number': {
                  'title': 'Choose a instrument number',
                  'label': 'Available Instrument Numbers',
                  'ok-tooltip': 'Click to use the selected instrument number.'
                },
                'hints': {
                    'description': {
                        'de': 'Please enter a short description of the instrument in German.',
                        'en': 'Please enter a short description of the instrument in English.'
                    },
                    'title': {
                        'de': 'Please enter the title of this instrument in German.',
                        'en': 'Please enter the title of this instrument in English.'
                    },
                    'subtitle': {
                      'de': 'Please enter the subtitle of this instrument in German.',
                      'en': 'Please enter the subtitle of this instrument in English.'
                    },
                    'type': 'Please select the type of this instrument.',
                    'surveys': 'Select the surveys in which this instrument has been used.',
                    'concepts': 'Select the concepts which this instrument has measured.',
                    'search-surveys': 'Search surveys...',
                    'search-concepts': 'Search concepts...',
                    'no-surveys-found': 'No (further) surveys found.',
                    'no-concepts-found': 'No (further) concepts found.',
                    'annotations': {
                      'de': 'Please provide additional annotations (in German) to this survey here.',
                      'en': 'Please provide additional annotations (in English) to this survey here.'
                    },
                    'instrument-number': 'Please select an instrument number for the new instrument.'
                },
                'all-instruments-deleted-toast': 'All Instruments of the Data Acquisition Project "{{id}}" have been deleted.'
            }
        }
        //jscs:enable
      };
    $translateProvider.translations('en', translations);
  }]);
