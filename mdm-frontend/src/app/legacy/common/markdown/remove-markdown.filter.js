'use strict';

angular.module('metadatamanagementApp').filter('removeMarkdown',
  function($showdown) {
  return function(text) {
    if (text) {
      return $showdown.stripHtml($showdown.makeHtml(text));
    }
    return '';
  };
});
