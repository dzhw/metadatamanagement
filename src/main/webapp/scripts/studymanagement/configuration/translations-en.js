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
            'dataPackage': 'Data package',
            'dataPackages': 'Data package',
            'institution': 'Institution',
            'institutions': 'Institution(s)',
            'authors': 'Authors',
            'sponsors': 'Sponsored by',
            'version': 'Version',
            'surveyDesign': 'Survey Design',
            'annotations': 'Annotations',
            'wave': 'Waves',
            'survey-data-type': 'Survey Data Type',
            'survey-period': 'Survey Period',
            'title': 'Title',
            'dataAvailability': 'Data Availability',
            'tags': 'Tags',
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
              'maxNumberOfObservations': 'Observations',
              'maxNumberOfEpisodes': 'Episodes',
              'surveyed-in': 'Contains data from these surveys'
            },
            'doi': 'DOI',
            'published-at': 'published at',
            'published': 'Published at'
          },
          'attachments': {
            'table-title': 'Documents related to this Data Package',
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
            'change-file-tooltip': 'Click to choose a file.',
            'open-choose-previous-version-tooltip': 'Click to restore a previous version of the metadata.',
            'current-version-restored-toast': 'Current version of the metadata for document "{{ filename }}" has been restored.',
            'previous-version-restored-toast': 'Previous version of the metadata for document "{{ filename }}" can be saved now.',
            'language-not-found': 'No valid language found!',
            'save-study-before-adding-attachment': 'The Study has to be saved to enable attaching documents.',
            'hints': {
              'filename': 'Choose a file which you want to attach to the study.'
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
          'not-yet-released': 'Currently not released',
          'not-released-toast': 'Study "{{ id }}" is being worked on. Therefore it is not visible to all users at the moment!',
          'beta-release-no-doi': 'This study has no DOI yet.',
          'publications-for-series': 'Publications related to Series "{{studySeries}}"',
          'publications-for-study': 'Publications related to this Study',
          'tooltips': {
            'surveys': {
              'one': 'Click to show the survey of this study',
              'many': 'Click to show all surveys of this study'
            },
            'data-sets': {
              'one': 'Click to show the data set of this study',
              'many': 'Click to show all data sets of this study'
            },
            'series-publications': 'Click to show all publications related to this study series',
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
              'study-series': 'Click to show all data packages of the study series.'
            },
            'concepts': {
              'one': 'Click to show the concept which has been measured in this study',
              'many': 'Click to show all concepts which have been measured in this study'
            }
          },
          'doi-tooltip': 'Click to open the DOI in a new Tab',
          'tag-tooltip': 'Click to search for data packages with this tag'
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
          }
        },
        'error': {
          'study': {
            'id': {
              'not-empty': 'The RDC-ID of the Study must not be empty!',
              'size': 'The max length of the RDC-ID is 512 signs.',
              'pattern': 'Use only alphanumeric signs, German umlauts, ß and space, underscore and minus for the RDC-ID.',
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
              'i18n-string-not-empty': 'The description of the study must not be empty for all languages.'
            },
            'institution': {
              'not-null': 'The institution of the study must not be empty!',
              'i18n-string-size': 'The max length of the institution is 512.',
              'i18n-string-entire-not-empty': 'The institution of the study must not be empty for both languages.'
            },
            'sponsor': {
              'not-null': 'The sponsor of the study must not be empty!',
              'i18n-string-size': 'The max length of the sponsor of the study is 512.',
              'i18n-string-entire-not-empty': 'The sponsor of the study must not be empty for both languages.'
            },
            'study-series': {
              'i18n-string-size': 'The max length of the study series is 512 signs.',
              'i18n-string-entire-not-empty-optional': 'If the study series is given in one language, it has to be set in all languages.',
              'i18n-string-must-not-contain-comma': 'The study series must not contain comma.'
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
          'choose-unreleased-project-toast': 'Studies may be edited if and only if the project is currently not released!',
          'label': {
            'edit-study': 'Edit Study:',
            'create-study': 'Create Study:',
            'first-name': 'First Name',
            'middle-name': 'Middle Name',
            'last-name': 'Last Name',
            'tags': 'Tags (Keywords) for the Study'
          },
          'open-choose-previous-version-tooltip': 'Click for restoring a previous version of this study.',
          'save-tooltip': 'Click to save this study.',
          'move-author-up-tooltip': 'Click to move the selected author up.',
          'move-author-down-tooltip': 'Click to move the selected author down.',
          'add-author-tooltip': 'Click to add a new author to this study.',
          'delete-author-tooltip': 'Click to remove the author from this study.',
          'move-institution-up-tooltip': 'Click to move the selected institution up.',
          'move-institution-down-tooltip': 'Click to move the selected institution down.',
          'add-institution-tooltip': 'Click to add another institution to this study.',
          'delete-institution-tooltip': 'Click to remove the institution from this study.',
          'choose-previous-version': {
            'title': 'Restore Previous Version of Study {{ studyId }}',
            'text': 'Choose a previous version of this study which shall be restored:',
            'cancel-tooltip': 'Click to return without choosing a previous study version.',
            'no-versions-found': 'There are no previous versions of study {{ studyId }}.',
            'study-deleted': 'The study has been deleted!'
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
          },
          'all-studies-deleted-toast': 'The study of Data Acquisition Project "{{id}}" has been deleted.'
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
