'use strict';

angular.module('metadatamanagementApp').filter('removeMarkdown', ['$showdown',
  function($showdown) {
  return function(text) {
    if (text) {
      return $showdown.stripHtml($showdown.makeHtml(text));
    }
    return '';
  };
}]);
