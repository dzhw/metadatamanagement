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
            'authors': 'Authors',
            'doi': 'DOI',
            'title': 'Title',
            'citation-hint': 'Citation Hint',
            'license': 'License of this Concept',
            'license-edit': 'License of this Concept (in English)',
            'original-languages': 'Original Languages of this Concept',
            'attachments': {
              'type': 'Type',
              'title': 'Title',
              'description': 'Description',
              'language': 'Document Language',
              'file': 'File'
            }
          },
          'attachments': {
            'table-title': 'Documents related to this Concept',
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
            'change-file-tooltip': 'Click to choose a file.',
            'open-choose-previous-version-tooltip': 'Click to restore a previous version of the metadata.',
            'current-version-restored-toast': 'Current version of the metadata for document "{{ filename }}" has been restored.',
            'previous-version-restored-toast': 'Previous version of the metadata for document "{{ filename }}" can be saved now.',
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
          'title': '{{ title }}',
          'page-description': '{{ description }}',
          'description': 'Description',
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
            'data-packages': {
              'one': 'Click to show the data package in which this concept has been measured',
              'many': 'Click to show all data packages in which this concept has been measured'
            }
          },
          'doi-tooltip': 'Click to open the DOI in a new tab',
          'tag-tooltip': 'Click to search for concepts with this tag',
          'tags': 'Tags'
        },
        'log-messages': {
          'concept': {
            'saved': 'Concept with RDC-ID {{ id }} was saved successfully!',
            'not-saved': 'Concept with RDC-ID {{ id }} has not been saved!',
            'unable-to-delete': 'The concept could not be deleted!'
          }
        },
        'error': {
          'concept': {
            'id': {
              'not-empty': 'The RDC-ID of the concept must not be empty!',
              'size': 'The max length of the RDC-ID is 512 signs.',
              'pattern': 'The RDC-ID must not contain any whitespaces, commas or semicolons.',
              'not-valid-id': 'The RDC-ID of the concept must correspond to the form "con-" + {text} + "$". {text} must not contain any whitespace.',
              'not-unique': 'This RDC-ID already exists'
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
            },
            'in-use': {
              'instruments': 'The concept cannot be deleted, because it is referenced by the following instruments: {{ids}}.',
              'questions': 'The concept cannot be deleted, because it is referenced by the following questions: {{ids}}.'
            }
          }
        },
        'edit': {
          'id': 'RDC-ID',
          'edit-page-title': 'Edit Concept {{conceptId}}',
          'create-page-title': 'Create Concept {{conceptId}}',
          'success-on-save-toast': 'Concept {{conceptId}} has been saved successfully.',
          'error-on-save-toast': 'An error occurred during saving of Concept {{conceptId}}!',
          'concept-has-validation-errors-toast': 'The Concept has not been saved because there are invalid fields!',
          'previous-version-restored-toast': 'Previous version of Concept {{ conceptId }} can be saved now.',
          'current-version-restored-toast': 'Current version of Concept {{ conceptId }} has been restored.',
          'not-authorized-toast': 'You are not authorized to edit or create concepts. Contact a publisher of this project.<br><br>You can use already existing concepts directly in the instruments or questions!',
          'concept-deleted-toast': 'Concept {{ id }} has been deleted.',
          'label': {
            'id': 'RDC-ID',
            'edit-concept': 'Edit Concept:',
            'create-concept': 'Create Concept:',
            'first-name': 'First Name',
            'middle-name': 'Middle Name',
            'last-name': 'Last Name',
            'tags': 'Tags (Keywords) for this Concept'
          },
          'open-choose-previous-version-tooltip': 'Click for restoring a previous version of this concept.',
          'save-tooltip': 'Click to save this concept.',
          'move-author-up-tooltip': 'Click to move the selected author up.',
          'move-author-down-tooltip': 'Click to move the selected author down.',
          'add-author-tooltip': 'Click to add a new author to this concept.',
          'delete-author-tooltip': 'Click to remove the author from this concept.',
          'choose-previous-version': {
            'title': 'Restore Previous Version of Concept {{ conceptId }}',
            'text': 'Choose a previous version of this concept which shall be restored:',
            'cancel-tooltip': 'Click to return without choosing a previous concept version.',
            'no-versions-found': 'There are no previous versions of concept {{ conceptId }}.',
            'concept-deleted': 'The concept has been deleted!'
          },
          'hints': {
            'id': 'Specify the ID of this concept in our RDC.',
            'doi': 'Please enter the DOI of this concept (if available).',
            'license': 'Please provide the license in English under which this concept has been published.',
            'title': {
              'de': 'Please enter the title of this concept in German.',
              'en': 'Please enter the title of this concept in English.'
            },
            'citation-hint': 'Specify how this concept shall be cited.',
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
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
