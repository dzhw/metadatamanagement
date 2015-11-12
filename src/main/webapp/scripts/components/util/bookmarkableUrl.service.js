'use strict';

angular.module('metadatamanagementApp').factory('BookmarkableUrl', function() {
  return {
    changeLocation: function(langKey, location) {
      var currentPath = location.path();
      if (langKey === 'en') {
        currentPath = currentPath.replace('/de/', '/en/');
      }
      if (langKey === 'de') {
        currentPath = currentPath.replace('/en/', '/de/');
      }
      location.path(currentPath);
    }
  };
});
