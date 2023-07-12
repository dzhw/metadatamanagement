'use strict';

angular.module('metadatamanagementApp').config(
    function($translateProvider) {
      var translations = {
        //jscs:disable
        'choose-previous-version': {
          'cancel-tooltip': 'Click to return without choosing a previous version.',
          'current-version-tooltip': 'This is the current version!',
          'deleted': 'Deleted',
          'lastModified': 'Modified',
          'lastModifiedBy': 'by',
          'next-page-tooltip': 'Click to show older versions.',
          'no-versions-found': 'There are no previous versions of {{ id }}.',
          'previous-page-tooltip': 'Click to show more recent versions.',
          'text': 'Choose a previous version which shall be restored:',
          'title': 'Title',
          'description': 'Description'
        }
        //jscs:enable
      };
      $translateProvider.translations('en', translations);
    });
