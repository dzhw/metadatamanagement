'use strict';

angular.module('metadatamanagementApp').factory('LanguageService',
  function($q, $http, $translate, $location, $rootScope,
    tmhDynamicLocale, LANGUAGES) {
    return {
      getCurrent: function() {
        var deferred = $q.defer();
        var language = this.getCurrentInstantly();
        deferred.resolve(language);
        return deferred.promise;
      },

      getCurrentInstantly: function() {
        var language = $translate.storage().get('NG_TRANSLATE_LANG_KEY');

        if (angular.isUndefined(language)) {
          language = 'de';
        }

        return language;
      },

      setCurrent: function(language) {
        $rootScope.currentLanguage = language;
        $translate.storage().set('NG_TRANSLATE_LANG_KEY', language);
        tmhDynamicLocale.set(language).then(function() {
          $translate.use(language);
          var currentPath = $location.path();
          if (language === 'en' && currentPath.indexOf('/de/') === 0) {
            currentPath = currentPath.replace('/de/', '/en/');
            $location.path(currentPath);
          }
          if (language === 'de' && currentPath.indexOf('/en/') === 0) {
            currentPath = currentPath.replace('/en/', '/de/');
            $location.path(currentPath);
          }
        });
      },

      getAll: function() {
        var deferred = $q.defer();
        deferred.resolve(LANGUAGES);
        return deferred.promise;
      }
    };
  })

/*
 * Languages codes are ISO_639-1 codes, see
 * http://en.wikipedia.org/wiki/List_of_ISO_639-1_codes They are written in
 * English to avoid character encoding issues (not a perfect solution)
 */
.constant('LANGUAGES', ['en', 'de'
  // JHipster will add new languages here
]);
