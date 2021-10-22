'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'analysis-package-management': {
        'detail': {
          'label': {
            'additional-links': 'Additional Links',
            'analysisPackage': 'Analysis Package',
            'analysisPackages': 'Analysis Packages',
            'annotations': 'Annotations',
            'authors': 'Authors',
            'data-curators': 'Datenkuratierung',
            'description': 'Analysis Package Description',
            'doi': 'DOI',
            'institution': 'Institution',
            'institutions': 'Institution(s)',
            'license': 'License',
            'scripts': 'Scripts',
            'sponsors': 'Sponsored by',
            'title': 'Title',
            'attachments': {
              'title': 'Title',
              'description': 'Description',
              'language': 'Document Language',
              'file': 'File'
            }
          },
          'attachments': {
            'table-title': 'Documents related to this Analysis Package',
            'delete-attachment-tooltip': 'Click to delete document "{{ filename }}"!',
            'edit-attachment-tooltip': 'Click to edit the metadata for document "{{ filename }}".',
            'select-attachment-tooltip': 'Click to select document "{{ filename }}" for moving it up or down.',
            'move-attachment-down-tooltip': 'Click to move the selected document down.',
            'move-attachment-up-tooltip': 'Click to move the selected document up.',
            'save-attachment-order-tooltip': 'Click to save the modified order of the documents.',
            'add-attachment-tooltip': 'Click to add a new document to this analysis package.',
            'create-title': 'Add new Document to Analysis Package "{{ analysisPackageId }}"',
            'edit-title': 'Modify Document "{{ filename }}" of Analysis Package "{{ analysisPackageId }}"',
            'attachment-deleted-toast': 'Document "{{ filename }}" has been deleted!',
            'attachment-order-saved-toast': 'The modified order of the documents has been saved.',
            'change-file-tooltip': 'Click to choose a file.',
            'open-choose-previous-version-tooltip': 'Click to restore a previous version of the metadata.',
            'current-version-restored-toast': 'Current version of the metadata for document "{{ filename }}" has been restored.',
            'previous-version-restored-toast': 'Previous version of the metadata for document "{{ filename }}" can be saved now.',
            'language-not-found': 'No valid language found!',
            'save-analysis-package-before-adding-attachment': 'The analysis package has to be saved to enable attaching documents.',
            'hints': {
              'filename': 'Choose a file which you want to attach to the analysis package.'
            }
          },
          'not-found': 'The {{id}} references to an unknown Analysis Package.'
        },
        'edit': {
          'all-analysis-packages-deleted-toast': 'The analysis package of Data Acquisition Project "{{id}}" has been deleted.',
          'open-choose-previous-version-tooltip': 'Click for restoring a previous version of this analysis package.',
          'save-tooltip': 'Click to save this analysis package.',
          'add-package-author-tooltip': 'Click to add a new author to this analysis package.',
          'move-sponsor-up-tooltip': 'Click to move the selected sponsor up.',
          'move-sponsor-down-tooltip': 'Click to move the selected sponsor down.',
          'add-sponsor-tooltip': 'Click to add another sponsor to this analysis package.',
          'delete-sponsor-tooltip': 'Click to remove the sponsor from this analysis package.',
          'move-script-up-tooltip': 'Click to move the selected script up.',
          'move-script-down-tooltip': 'Click to move the selected script down.',
          'add-script-tooltip': 'Click to add another script to this analysis package.',
          'delete-script-tooltip': 'Click to remove the script from this analysis package.',
          'move-institution-up-tooltip': 'Click to move the selected institution up.',
          'move-institution-down-tooltip': 'Click to move the selected institution down.',
          'add-institution-tooltip': 'Click to add another institution to this analysis package.',
          'add-link-tooltip': 'Click to add another link to this analysis package.',
          'move-link-up-tooltip': 'Click to move the selected link up.',
          'move-link-down-tooltip': 'Click to move the selected link down.',
          'delete-link-tooltip': 'Click to remove the link from this analysis package.',
          'delete-institution-tooltip': 'Click to remove the institution from this analysis package.',
          'delete-package-author-tooltip': 'Click to remove the author from this analysis package.',
          'move-package-author-up-tooltip': 'Click to move the selected author up.',
          'move-package-author-down-tooltip': 'Click to move the selected author down.',
          'add-curator-tooltip': 'Click to add a new data curator to this analysis package.',
          'delete-curator-tooltip': 'Click to remove the data curator from this analysis package.',
          'move-curator-up-tooltip': 'Click to move the selected data curator up.',
          'move-curator-down-tooltip': 'Click to move the selected data curator down.',
          'choose-unreleased-project-toast': 'Analysis Packages may be edited if and only if the project is currently not released!',
          'not-authorized-toast': 'You are not authorized to create or edit analysis packages!',
          'error-on-save-toast': 'An error occurred during saving of Analysis Package {{analysisPackageId}}!',
          'analysis-package-has-validation-errors-toast': 'The Analysis Package has not been saved because there are invalid fields!',
          'success-on-save-toast': 'Analysis Package {{analysisPackageId}} has been saved successfully.',
          'previous-version-restored-toast': 'Previous version of Analysis Package {{ analysisPackageId }} can be saved now.',
          'current-version-restored-toast': 'Current version of Analysis Package {{ analysisPackageId }} has been restored.',
          'choose-previous-version': {
            'title': 'Restore Previous Version of Analysis Package {{ analysisPackageId }}',
            'text': 'Choose a previous version of this analysis package which shall be restored:',
            'cancel-tooltip': 'Click to return without choosing a previous analysis package version.',
            'no-versions-found': 'There are no previous versions of analysis package {{ analysisPackageId }}.',
            'data-package-deleted': 'The analysis package has been deleted!'
          },
          'label': {
            'create-analysis-package': 'Create Analysis Package:',
            'edit-analysis-package': 'Edit Analysis Package:',
            'first-name': 'First Name',
            'middle-name': 'Middle Name',
            'last-name': 'Last Name',
            'tags': 'Tags (Keywords) for the Analysis Package',
            'publication-year': 'Year of Publication',
            'institution': 'Institution',
            'sponsor': 'Sponsor',
            'location': 'Location',
            'additional-links': {
              'url': 'URL',
              'display-text': 'Display Text'
            }
          },
          'hints': {
            'authors': {
              'first-name': 'Enter the first name of this author.',
              'middle-name': 'If available enter the middle-name of this author.',
              'last-name': 'Enter the last name of this author.'
            },
            'annotations': {
              'de': 'Enter additional annotations for this analysis package in German.',
              'en': 'Enter additional annotations for this analysis package in English.'
            },
            'curators': {
              'first-name': 'Enter the first name of the person involved in data preparation.',
              'middle-name': 'If available enter the middle-name of this person.',
              'last-name': 'Enter the last name of the person involved in data preparation.'
            },
            'description': {
              'de': 'Enter a description of this analysis package in German.',
              'en': 'Enter a description of this analysis package in English.'
            },
            'institution': {
              'de': 'Please enter the German name of the institution which participated in the analysis package.',
              'en': 'Please enter the English name of the institution which participated in the analysis package.'
            },
            'license': 'If no contract is signed we will need a license like cc-by-sa',
            'sponsor': {
              'de': 'Enter the German name of the sponsor of this analysis package.',
              'en': 'Enter the English name of the sponsor of this analysis package.'
            },
            'title': {
              'de': 'Please enter the title of this analysis package in German.',
              'en': 'Please enter the title of this analysis package in English.'
            }
          }
        },
        'error': {
          'analysis-package': {
            'annotations': {
              'i18n-string-size': 'The max length of the annotations is 2048 signs.'
            },
            'description': {
              'not-null': 'The description of the analysis package must not be empty!',
              'i18n-string-size': 'The max length of the analysis package description is 2048.',
              'i18n-string-not-empty': 'The description of the analysis package must not be empty for all languages.'
            },
            'institution': {
              'not-null': 'The institution of the analysis package must not be empty!',
              'i18n-string-size': 'The max length of the institution is 512.',
              'i18n-string-entire-not-empty': 'The institution of the analysis package must not be empty for both languages.'
            },
            'license': {
              'string-size': 'The max length of the license is 1048576 signs.'
            },
            'sponsor': {
              'not-null': 'The sponsor of the analysis package must not be empty!',
              'i18n-string-size': 'The max length of the sponsor of the analysis package is 512.',
              'i18n-string-entire-not-empty': 'The sponsor of the analysis package must not be empty for both languages.'
            },
            'title': {
              'not-null': 'The title of the analysis package must not be empty!',
              'i18n-string-size': 'The max length of the analysis package title is 2048.',
              'i18n-string-entire-not-empty': 'The title of the analysis package must not be empty for all languages.'
            }
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
