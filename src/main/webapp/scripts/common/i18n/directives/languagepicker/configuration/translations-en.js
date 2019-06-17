'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'common': {
        'language-picker': {
          'error': {
            'required': 'At least one language must be selected',
            'md-require-match': 'This language could not be found.'
          },
          'label': 'Languages',
          'placeholder': 'Select languages'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  }
);
