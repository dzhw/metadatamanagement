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
            'hints': {
              'filename': 'Choose a file which you want to attach to the analysis package.'
            }
          },
          'not-found': 'The {{id}} references to an unknown Analysis Package.'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
