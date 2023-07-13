'use strict';

angular.module('metadatamanagementApp').filter('trustAsResourceUrl', ['$sce', 
  function($sce) {
    return function(url) {
      return $sce.trustAsResourceUrl(url);
    };
  }]);

