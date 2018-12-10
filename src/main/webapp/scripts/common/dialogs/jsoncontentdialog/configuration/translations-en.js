'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'common': {
        'dialogs': {
          'jsoncontentdialog': {
            'close': 'Close dialog',
            'tooltip': 'Display data as JSON'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
