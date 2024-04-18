'use strict';

angular.module('metadatamanagementApp').config([
  '$translateProvider',

  function($translateProvider) {
    var translations = {
      //jscs:disable
      'common': {
        'tag-editor': {
          'error': {
            'required': 'Es muss mindestens ein Schlagwort eingetragen sein.'
          },
          'label': {
            'german-tags': 'Deutsche Schlagwörter',
            'english-tags': 'Englische Schlagwörter'
          },
          'placeholder': 'Neues Schlagwort eingeben'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  }]);
