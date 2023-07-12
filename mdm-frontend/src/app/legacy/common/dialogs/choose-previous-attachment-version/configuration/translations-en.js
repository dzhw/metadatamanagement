'use strict';

angular.module('metadatamanagementApp').config(
    function($translateProvider) {
      var translations = {
        //jscs:disable
        'choose-previous-attachment-version': {
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
        }
        //jscs:enable
      };
      $translateProvider.translations('en', translations);
    });
