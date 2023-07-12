'use strict';

angular.module('metadatamanagementApp').filter('trustAsResourceUrl',
  function($sce) {
    return function(url) {
      return $sce.trustAsResourceUrl(url);
    };
  });
