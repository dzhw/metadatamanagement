'use strict';

angular.module('metadatamanagementApp').factory('BookmarkableUrl', function() {
  return {
    changeLocation: function(langKey, location, rootscope) {
      var currentPath = location.path();
      if (langKey === 'en') {
        currentPath = currentPath.replace('/de/', '/en/');
      }
      if (langKey === 'de') {
        currentPath = currentPath.replace('/en/', '/de/');
      }
      location.path(currentPath);
      rootscope.currentLanguage = langKey;
    },
    setUrlLanguage: function(location, rootScope) {
      if (location.path().indexOf('/de/') >= 0) {
        rootScope.currentLanguage = 'de';
      } else {
        rootScope.currentLanguage = 'en';
      }
    }
  };
});
