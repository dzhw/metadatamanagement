'use strict';

angular.module('metadatamanagementApp').factory('LanguageService',
  function($q, $translate, $location, $rootScope,
    tmhDynamicLocale, LANGUAGES, amMoment, $mdDateLocale, moment) {
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
        if (language !== 'de' && language !== 'en') {
          if (language.indexOf('de-') >= 0 || language.indexOf('de_') >= 0) {
            language = 'de';
          } else {
            language = 'en';
          }
        }
        if ($rootScope.currentLanguage === language) {
          return;
        }
        $rootScope.currentLanguage = language;
        amMoment.changeLocale(language);
        $mdDateLocale.firstDayOfWeek = 1;
        $mdDateLocale.parseDate = function(dateString) {
          if (dateString != null && dateString !== '') {
            var m = moment(dateString, 'L', true);
            return m.isValid() ? m.toDate() : new Date(NaN);
          }
          return new Date(NaN);
        };
        $mdDateLocale.formatDate = function(date) {
          if (date) {
            var m = moment(date);
            return m.isValid() ? m.format('L') : '';
          } else {
            return '';
          }
        };
        $rootScope.$broadcast('current-language-changed', language);
        $translate.storage().set('NG_TRANSLATE_LANG_KEY', language);
        tmhDynamicLocale.set(language).then(function() {
          $translate.use(language);
          var currentPath = $location.path();
          if (!currentPath || currentPath === '' || currentPath === '/') {
            currentPath = '/' + language + '/start';
            $location.path(currentPath);
          }
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
