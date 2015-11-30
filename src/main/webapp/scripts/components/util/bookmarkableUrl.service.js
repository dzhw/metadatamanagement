'use strict';

angular.module('metadatamanagementApp').factory('BookmarkableUrl', function() {
  return {
    changeUrlLanguage: function(langKey, location, rootscope, translate) {
      var currentPath = location.path();
      translate.use(langKey);
      if (langKey === 'en') {
        currentPath = currentPath.replace('/de/', '/en/');
      }
      if (langKey === 'de') {
        currentPath = currentPath.replace('/en/', '/de/');
      }
      location.path(currentPath);
      rootscope.currentLanguage = langKey;
    }
  };
});
