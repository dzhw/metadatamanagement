'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'common': {
        'projectstatuslabel': {
          'assigned-to': 'Zugewiesen an',
          'PUBLISHER': 'Publisher',
          'DATA_PROVIDER': 'Datengeber'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
