'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'common': {
        'tag-editor': {
          'error': {
            'required': 'Es muss mindestens ein Tag eingetragen sein.'
          },
          'label': {
            'german-tags': 'Deutsche Tags',
            'english-tags': 'Englische Tags'
          },
          'placeholder': 'Neuen Tag eingeben'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  }
);
