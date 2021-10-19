'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'analysis-package-management': {
        'detail': {
          'label': {
            'analysisPackage': 'Analysis Package',
            'analysisPackages': 'Analysis Packages',
            'authors': 'Authors',
            'data-curators': 'Datenkuratierung',
            'doi': 'DOI',
            'title': 'Title',
            'attachments': {
              'title': 'Title',
              'description': 'Description',
              'language': 'Document Language',
              'file': 'File'
            }
          },
          'attachments': {
            'create-title': 'Add new Document to Analysis Package "{{ analysisPackageId }}"',
            'edit-title': 'Modify Document "{{ filename }}" of Analysis Package "{{ analysisPackageId }}"',
            'attachment-deleted-toast': 'Document "{{ filename }}" has been deleted!',
            'attachment-order-saved-toast': 'The modified order of the documents has been saved.',
            'hints': {
              'filename': 'Choose a file which you want to attach to the analysis package.'
            }
          },
          'edit': {
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
            'hints': {
              'authors': {
                'first-name': 'Enter the first name of this author.',
                'middle-name': 'If available enter the middle-name of this author.',
                'last-name': 'Enter the last name of this author.'
              },
              'curators': {
                'first-name': 'Enter the first name of the person involved in data preparation.',
                'middle-name': 'If available enter the middle-name of this person.',
                'last-name': 'Enter the last name of the person involved in data preparation.'
              }
            }
          },
          'not-found': 'The {{id}} references to an unknown Analysis Package.'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
