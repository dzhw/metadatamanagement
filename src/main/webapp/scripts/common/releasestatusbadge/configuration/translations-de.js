'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'common': {
        'releasestatusbadge': {
          'released': 'Freigegeben',
          'unreleased': 'Nicht freigegeben'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
