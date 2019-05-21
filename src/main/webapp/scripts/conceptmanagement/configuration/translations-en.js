'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'concept-management': {
        'detail': {
          'label': {
            'concept': 'Concept',
            'concepts': 'Concepts',
          },
          'attachments': {
            'table-title': 'Documents related to the concept',
            'attachment-deleted-toast': 'Document "{{ filename }}" has been deleted!',
            'delete-attachment-tooltip': 'Click to delete document "{{ filename }}"!',
            'edit-attachment-tooltip': 'Click to edit the metadata for document "{{ filename }}".',
            'select-attachment-tooltip': 'Click to select document "{{ filename }}" for moving it up or down.',
            'move-attachment-down-tooltip': 'Click to move the selected document down.',
            'move-attachment-up-tooltip': 'Click to move the selected document up.',
            'save-attachment-order-tooltip': 'Click to save the modified order of the documents.',
            'attachment-order-saved-toast': 'The modified order of the documents has been saved.',
            'add-attachment-tooltip': 'Click to add a new document to this concept.',
            'edit-title': 'Modify Document "{{ filename }}" of Concept "{{ conceptId }}"',
            'create-title': 'Add new Document to Concept "{{ conceptId }}"',
            'cancel-tooltip': 'Click to close this dialog without saving.',
            'save-tooltip': 'Click to save this document.',
            'change-file-tooltip': 'Click to choose a file.',
            'attachment-saved-toast': 'Document "{{ filename }}" has been saved.',
            'attachment-has-validation-errors-toast': 'The document has not been saved because there are invalid fields.',
            'open-choose-previous-version-tooltip': 'Click to restore a previous version of the metadata.',
            'current-version-restored-toast': 'Current version of the metadata for document "{{ filename }}" has been restored.',
            'previous-version-restored-toast': 'Previous version of the metadata for document "{{ filename }}" can be saved now.',
            'choose-previous-version': {
              'title': 'Restore Previous Version of the Metadata for Document "{{ filename }}"',
              'text': 'Choose a previous version of the metadata for document "{{ filename }}" which shall be restored:',
              'attachment-description': 'Description (in English)',
              'lastModified': 'Modified',
              'lastModifiedBy': 'by',
              'cancel-tooltip': 'Click to return without choosing a previous metadata version.',
              'current-version-tooltip': 'This is the current version!',
              'next-page-tooltip': 'Click to show older versions.',
              'previous-page-tooltip': 'Click to more recent versions.',
              'attachment-deleted': 'Metadata has been deleted!',
              'no-versions-found': 'There are no previous versions of the metadata.'
            },
            'language-not-found': 'No valid language found!',
            'save-concept-before-adding-attachment': 'The Concept has to be saved to enable attaching documents.',
            'hints': {
              'filename': 'Choose a file which you want to attach to the concept.',
              'type': 'Choose the type of the file.',
              'language': 'Select the language which has been used in the file.',
              'title': 'Enter the title of the file in the language of the file.',
              'description': {
                'de': 'Please enter a description for the file in German.',
                'en': 'Please enter a description for the file in English.'
              }
            }
          },
          'title': '{{ title }} ({{ conceptId }})',
          'not-found': 'The {{id}} references to an unknown Concept.',
          'not-found-references': 'The id {{id}} has no References to Concepts.',
          'tooltips': {
            'surveys': {
              'one': 'Click to show the survey in which this concept has been measured',
              'many': 'Click to show all surveys in which this concept has been measured'
            },
            'data-sets': {
              'one': 'Click to show the data set in which this concept has been measured',
              'many': 'Click to show all data sets in which this concept has been measured'
            },
            'variables': {
              'one': 'Click to show the variable which has been used to measure this concept',
              'many': 'Click to show all variables which have been used to measure this concept'
            },
            'questions': {
              'one': 'Click to show the question which has been used to measure this concept',
              'many': 'Click to show all questions which have been used to measure this concept'
            },
            'instruments': {
              'one': 'Click to show the instrument which has been used to measure this concept',
              'many': 'Click to show all instruments which have been used to measure this concept'
            },
            'studies': {
              'one': 'Click to show the study in which this concept has been measured',
              'many': 'Click to show all studies in which this concept has been measured'
            }
          },
          'doi-tooltip': 'Click to open the DOI in a new tab',
          'tag-tooltip': 'Click to search for concepts with this tag'
        },
        'log-messages': {
          'concept': {
            'saved': 'Concept with RDC-ID {{ id }} was saved successfully!',
            'not-saved': 'Concept with RDC-ID {{ id }} has not been saved!',
            'unable-to-delete': 'The concept could not be deleted!',
          },
          'concept-attachment': {
            'not-saved': 'Attachment "{{ id }}" has not been saved:',
            'file-not-found': 'The File {{ filename }} was not found and has not been saved.'
          }
        },
        'error': {
          'concept': {
            'id': {
              'not-empty': 'The RDC-ID of the concept must not be empty!',
              'size': 'The max length of the RDC-ID is 512 signs.',
              'pattern': 'The RDC-ID must not contain any whitespace.',
              'not-valid-id': 'The RDC-ID of the concept must correspond to the form "con-" + {text} + "$". {text} must not contain any whitespace.'
            },
            'title': {
              'not-null': 'The title of the concept must not be empty!',
              'i18n-string-size': 'The max length of the concept title is 2048.',
              'i18n-string-entire-not-empty': 'The title of the concept must not be empty for all languages.'
            },
            'description': {
              'not-null': 'The description of the concept must not be empty!',
              'i18n-string-size': 'The max length of the concept description is 2048.',
              'i18n-string-not-empty': 'The description of the concept must not be empty for all languages.'
            },
            'authors': {
              'not-empty': 'There must be at least one author of this concept!'
            },
            'doi': {
              'size': 'The max length of the DOI of the concept is 512 signs.'
            },
            'license': {
              'size': 'The max length of the license of the concept is 1 MB signs.'
            },
            'citation-hint': {
              'not-empty': 'There must be a citation hint for the concept!',
              'size': 'The max length of the citation hint of the concept is 2048 MB signs.'
            },
            'tags': {
              'not-empty': 'There must be at least one tag in both languages.'
            }
          },
          'concept-attachment-metadata': {
            'concept-id': {
              'not-empty': 'The ID of the corresponding concept must not be empty.'
            },
            'type': {
              'not-null': 'The type of the attachment must not be empty!',
              'i18n-string-size': 'The type is mandatory in both languages and must not contain more than 32 characters.',
              'valid-type': 'The type must be one of the following: Documentation, Instrument, Other.'
            },
            'description': {
              'not-null': 'The description of the attachment must not be empty!',
              'i18n-string-size': 'The description must be specified in at least one language and must not contain more than 512 characters.',
              'i18n-string-not-empty': 'The description must not be empty for at least one language.'
            },
            'title': {
              'string-size': 'The title of the attachment must not contain more than 2048 characters.'
            },
            'language': {
              'not-null': 'The language of the attachment must not be empty!',
              'not-supported': 'The language of the attachment must be a two-letter abbreviation according to ISO 639-1!',
              'not-valid': 'Please select a supported language.'
            },
            'filename': {
              'not-empty': 'The filename of the attachment must not be empty!',
              'not-unique': 'There is already an attachment with this name!',
              'not-valid': 'The filename is invalid.'
            }
          }
        },
        'edit': {
          'edit-page-title': 'Edit Concept {{conceptId}}',
          'create-page-title': 'Create Concept {{conceptId}}',
          'success-on-save-toast': 'Concept {{conceptId}} has been saved successfully.',
          'error-on-save-toast': 'An error occurred during saving of Concept {{conceptId}}!',
          'concept-has-validation-errors-toast': 'The Concept has not been saved because there are invalid fields!',
          'previous-version-restored-toast': 'Previous version of Concept {{ conceptId }} can be saved now.',
          'current-version-restored-toast': 'Current version of Concept {{ conceptId }} has been restored.',
          'not-authorized-toast': 'You are not authorized to create or edit concepts!',
          'label': {
            'edit-concept': 'Edit Concept:',
            'create-concept': 'Create Concept:',
            'first-name': 'First Name',
            'middle-name': 'Middle Name',
            'last-name': 'Last Name',
            'tags': 'Tags (Keywords) for the Concept'
          },
          'open-choose-previous-version-tooltip': 'Click for restoring a previous version of this concept.',
          'save-tooltip': 'Click to save this concept.',
          'move-author-up-tooltip': 'Click to move the selected author up.',
          'move-author-down-tooltip': 'Click to move the selected author down.',
          'add-author-tooltip': 'Click to add a new author to this concept.',
          'delete-author-tooltip': 'Click to remove the author from this concept.',
          'choose-previous-version': {
            'next-page-tooltip': 'Click to show older versions.',
            'previous-page-tooltip': 'Click to show more recent versions.',
            'title': 'Restore Previous Version of Concept {{ conceptId }}',
            'text': 'Choose a previous version of this concept which shall be restored:',
            'cancel-tooltip': 'Click to return without choosing a previous concept version.',
            'no-versions-found': 'There are no previous versions of concept {{ conceptId }}.',
            'concept-title': 'Title',
            'lastModified': 'Modified',
            'lastModifiedBy': 'by',
            'current-version-tooltip': 'This is the current version!'
          },
          'hints': {
            'title': {
              'de': 'Please enter the title of this concept in German.',
              'en': 'Please enter the title of this concept in English.'
            },
            'description': {
              'de': 'Enter a description of this concept in German.',
              'en': 'Enter a description of this concept in English.'
            },
            'authors': {
              'first-name': 'Enter the first name of this project member.',
              'middle-name': 'If available enter the middle-name of this project member.',
              'last-name': 'Enter the last name of this project member.'
            }
          }
        },
        'tag-editor': {
          'label': {
            'german-tags': 'German Tags',
            'english-tags': 'English Tags'
          },
          'placeholder': 'Enter a new tag',
          'error': {
            'required': 'At least one tag must be provided'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
