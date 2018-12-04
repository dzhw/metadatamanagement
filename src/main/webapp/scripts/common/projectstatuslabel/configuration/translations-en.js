'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'common': {
        'projectstatuslabel': {
          'assigned-to': 'Assigned to',
          'PUBLISHER': 'Publisher',
          'DATA_PROVIDER': 'Data Provider'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
