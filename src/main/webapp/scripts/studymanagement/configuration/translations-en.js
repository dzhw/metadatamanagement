'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'study-management': {
        'detail': {
          'label': {
            'study': 'Study',
            'studies': 'Studies',
            'studySeries': 'Study Series',
            'institution': 'Institution',
            'authors': 'Authors',
            'sponsors': 'Sponsored by',
            'surveyDesign': 'Survey Design',
            'annotations': 'Annotations',
            'wave': 'Available Waves',
            'survey-data-type': 'Survey Data Type',
            'version': 'Version of Data Sets',
            'title': 'Title',
            'dataAvailability': 'Data Availability',
            'attachments': {
              'type': 'Type',
              'title': 'Title',
              'description': 'Description',
              'language': 'Document Language',
              'file': 'File'
            },
            'data-set': {
              'accessWays': 'Access Ways',
              'description': 'Description',
              'description-tooltip': 'Click to show data set "{{id}}"',
              'maxNumberOfObservations': 'Observations'
            },
            'doi': 'DOI'
          },
          'attachments': {
            'table-title': 'Documents related to the Study',
            'attachment-deleted-toast': 'Document "{{ filename }}" has been deleted!',
            'delete-attachment-tooltip': 'Click to delete document "{{ filename }}"!',
            'edit-attachment-tooltip': 'Click to edit the metadata for document "{{ filename }}".',
            'select-attachment-tooltip': 'Click to select document "{{ filename }}" for moving it up or down.',
            'move-attachment-down-tooltip': 'Click to move the selected document down.',
            'move-attachment-up-tooltip': 'Click to move the selected document up.',
            'save-attachment-order-tooltip': 'Click to save the modified order of the documents.',
            'attachment-order-saved-toast': 'The modified order of the documents has been saved.',
            'add-attachment-tooltip': 'Click to add a new document to this study.',
            'edit-title': 'Modify Document "{{ filename }}" of Study "{{ studyId }}"',
            'create-title': 'Add new Document to Study "{{ studyId }}"',
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
            'save-study-before-adding-attachment': 'The Study has to be saved to enable attaching documents.',
            'hints': {
              'filename': 'Choose a file which you want to attach to the study.',
              'type': 'Choose the type of the file.',
              'language': 'Select the language which has been used in the file.',
              'title': 'Enter the title of the file in the language of the file.',
              'description': {
                'de': 'Please enter a description for the file in German.',
                'en': 'Please enter a description for the file in English.'
              }
            }
          },
          'data-set': {
            'card-title': 'Available Data Sets'
          },
          'title': '{{ title }} ({{ studyId }})',
          'description': 'Study Description',
          'basic-data-of-surveys': 'Basic Data of Surveys',
          'not-found': 'The {{id}} references to an unknown Study.',
          'not-found-references': 'The id {{id}} has no References to Studies.',
          'not-yet-released': 'Not yet released',
          'not-released-toast': 'Study "{{ id }}" has not yet been released to all users!',
          'tooltips': {
            'surveys': {
              'one': 'Click to show the survey of this study',
              'many': 'Click to show all surveys of this study'
            },
            'data-sets': {
              'one': 'Click to show the data set of this study',
              'many': 'Click to show all data sets of this study',
            },
            'publications': {
              'one': 'Click to show the publication related to this study',
              'many': 'Click to show all publications related to this study'
            },
            'variables': {
              'one': 'Click to show the variable of this study',
              'many': 'Click to show all variables of this study'
            },
            'questions': {
              'one': 'Click to show the question of this study',
              'many': 'Click to show all questions of this study'
            },
            'instruments': {
              'one': 'Click to show the instruments of this study',
              'many': 'Click to show all instruments of this study'
            },
            'studies': {
              'study-series': 'Click to show all studies of the study series.'
            }
          },
          'doi-tooltip': 'Click to open the DOI in a new Tab'
        },
        'log-messages': {
          'study': {
            'saved': 'Study with RDC-ID {{ id }} was saved successfully!',
            'not-saved': 'Study with RDC-ID {{ id }} has not been saved!',
            'study-file-not-found': 'The selected directory does not contain the following file: study.xlsx!',
            'releases-file-not-found': 'The selected directory does not contain the following file: releases.xlsx!',
            'unable-to-delete': 'The study could not be deleted!',
            'upload-terminated': 'Finished upload of {{ total }} Study and {{ attachments }} Attachments with {{ warnings }} warnings and {{ errors }} errors.',
            'cancelled': 'Study upload cancelled!'
          },
          'study-attachment': {
            'not-saved': 'Attachment "{{ id }}" has not been saved:',
            'file-not-found': 'The File {{ filename }} was not found and has not been saved.'
          }
        },
        'error': {
          'study': {
            'id': {
              'not-empty': 'The RDC-ID of the Study must not be empty!',
              'size': 'The max length of the RDC-ID is 128 signs.',
              'pattern': 'Use only alphanumeric signs, German umlauts, ß and space, underscore, exclamation mark and minus for the RDC-ID.',
              'not-valid-id': 'The study id must be equal to the id scheme "stu-" + {ProjectId} + "$" .'
            },
            'title': {
              'not-null': 'The title of the study must not be empty!',
              'i18n-string-size': 'The max length of the study title is 2048.',
              'i18n-string-entire-not-empty': 'The title of the study must not be empty for all languages.'
            },
            'description': {
              'not-null': 'The description of the study must not be empty!',
              'i18n-string-size': 'The max length of the study description is 2048.',
              'i18n-string-not-empty': 'The description of the study must not be empty at least for one language.'
            },
            'institution': {
              'not-null': 'The institution of the study must not be empty!',
              'i18n-string-size': 'The max length of the institution is 128.',
              'i18n-string-not-empty': 'The institution of the study must not be empty at least for one language.'
            },
            'sponsor': {
              'not-null': 'The sponsor of the study must not be empty!',
              'i18n-string-size': 'The max length of the sponsor of the study is 128.',
              'i18n-string-not-empty': 'The sponsor of the study must not be empty at both languages.'
            },
            'study-series': {
              'i18n-string-size': 'The max length of the survey series is 128 signs.',
              'i18n-string-entire-not-empty-optional': 'If the survey series is given in one language, it has to be set in all languages.'
            },
            'data-availability': {
              'not-null': 'The data availability of the study must not be empty!',
              'valid-data-availability': 'The allowed values for data availability of the study are: Available, In preparation, Not available.'
            },
            'survey-design': {
              'not-null': 'The survey design of the study must not be empty!',
              'valid-survey-design': 'The allowed values for the survey design of the study are: Cross-Section, Panel.'
            },
            'authors': {
              'not-empty': 'The list of authors of a study needs min. one element and must not be empty!',
            },
            'annotations': {
              'i18n-string-size': 'The max length of the annotations is 2048 signs.'
            },
            'data-acquisition-project': {
              'id': {
                'not-empty': 'The RDC-ID of the Data Acquisition Project for the Study must not be empty!'
              }
            }
          },
          'study-attachment-metadata': {
            'study-id': {
              'not-empty': 'The ID of the corresponding Study must not be empty.'
            },
            'project-id': {
              'not-empty': 'The ID of the Data Acquisition Project must not be empty!'
            },
            'type': {
              'not-null': 'The type of the attachment must not be empty!',
              'i18n-string-size': 'The type is mandatory in both languages and must not contain more than 32 characters.',
              'valid-type': 'The type must be one of the following: Method Report, Other.'
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
              'not-empty': 'The filename of the attachment must not be empty!',
              'not-unique': 'There is already an attachment with this name!',
              'not-valid': 'The filename is invalid.'
            }
          }
        },
        'edit': {
          'edit-page-title': 'Edit Study {{studyId}}',
          'create-page-title': 'Create Study {{studyId}}',
          'success-on-save-toast': 'Study {{studyId}} has been saved successfully.',
          'error-on-save-toast': 'An error occurred during saving of Study {{studyId}}!',
          'study-has-validation-errors-toast': 'The Study has not been saved because there are invalid fields!',
          'previous-version-restored-toast': 'Previous version of Study {{ studyId }} can be saved now.',
          'current-version-restored-toast': 'Current version of Study {{ studyId }} has been restored.',
          'not-authorized-toast': 'You are not authorized to create or edit studies!',
          'choose-unreleased-project-toast': 'Please choose a project which is currently not released!',
          'label': {
            'edit-study': 'Edit Study:',
            'create-study': 'Create Study:',
            'first-name': 'First Name',
            'middle-name': 'Middle Name',
            'last-name': 'Last Name'
          },
          'open-choose-previous-version-tooltip': 'Click for restoring a previous version of this study.',
          'save-tooltip': 'Click to save this study.',
          'move-author-up-tooltip': 'Click to move the selected author up.',
          'move-author-down-tooltip': 'Click to move the selected author down.',
          'add-author-tooltip': 'Click to add a new author to this study.',
          'delete-author-tooltip': 'Click to remove the author from this study.',
          'choose-previous-version': {
            'next-page-tooltip': 'Click to show older versions.',
            'previous-page-tooltip': 'Click to show more recent versions.',
            'title': 'Restore Previous Version of Study {{ studyId }}',
            'text': 'Choose a previous version of this study which shall be restored:',
            'cancel-tooltip': 'Click to return without choosing a previous study version.',
            'no-versions-found': 'There are no previous versions of study {{ studyId }}.',
            'study-title': 'Title',
            'lastModified': 'Modified',
            'lastModifiedBy': 'by',
            'current-version-tooltip': 'This is the current version!'
          },
          'hints': {
            'title': {
              'de': 'Please enter the title of this study in German.',
              'en': 'Please enter the title of this study in English.'
            },
            'study-series': {
              'de': 'If available enter the name of the study series in German.',
              'en': 'If available enter the name of the study series in English.'
            },
            'institution': {
              'de': 'Please enter the German name of the institution which has conducted the surveys.',
              'en': 'Please enter the English name of the institution which has conducted the surveys.'
            },
            'sponsor': {
              'de': 'Enter the German name of the sponsor of this study.',
              'en': 'Enter the English name of the sponsor of this study.'
            },
            'survey-design': 'Choose the survey design of this study.',
            'annotations': {
              'de': 'Enter additional annotations for this study in German.',
              'en': 'Enter additional annotations for this study in English.'
            },
            'data-availability': 'Choose the state which best describes the current availability of the data of this study.',
            'description': {
              'de': 'Enter a description of this study in German.',
              'en': 'Enter a description of this study in English.'
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
