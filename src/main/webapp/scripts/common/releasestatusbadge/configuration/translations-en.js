'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'common': {
        'releasestatusbadge': {
          'released': 'Released',
          'unreleased': 'Unreleased'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
