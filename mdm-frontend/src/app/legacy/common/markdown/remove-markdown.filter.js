'use strict';

angular.module('metadatamanagementApp').filter('removeMarkdown', ['$showdown',
  function($showdown) {
  return function(text) {
    if (text) {
      // texts containing mail addresses cause problems when converted to html,
      // therefore @ is replaced by [at] to ensure safe conversion.
      const textWithoutAt = text.replace("@", "[at]");
      return $showdown.stripHtml($showdown.makeHtml(textWithoutAt));
    }
    return '';
  };
}]);
