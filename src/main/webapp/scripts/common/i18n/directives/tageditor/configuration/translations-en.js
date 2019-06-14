'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'common': {
        'tag-editor': {
          'error': {
            'required': 'At least one tag must be provided'
          },
          'label': {
            'german-tags': 'German Tags',
            'english-tags': 'English Tags'
          },
          'placeholder': 'Enter a new tag'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  }
);
