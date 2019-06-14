/* globals _ */
'use strict';

angular.module('metadatamanagementApp').controller('LanguagePickerController',
  function($scope, LanguageResource, LanguageService) {

    $scope.languagesRequestPending = true;

    if (angular.isUndefined($scope.languages)) {
      $scope.languages = [];
    }

    var languageMap = {};
    var languages = LanguageResource.get({locale: LanguageService
        .getCurrentInstantly()});

    languages.$promise.then(function(languages) {
      languageMap = languages.reduce(function(acc, currentValue) {
        acc[currentValue.languageCode] = currentValue;
        return acc;
      }, {});
      $scope.languagesRequestPending = false;
    });

    $scope.searchLanguage = function(languageName) {
      var lcLanguageName = languageName.toLowerCase();
      var filteredLanguages = _.filter(languages, function(language) {
        return language.displayName.toLowerCase()
          .indexOf(lcLanguageName) !== -1;
      }).map(function(language) {
        return language.languageCode;
      });

      return _.difference(filteredLanguages, $scope.languages);
    };

    $scope.getLanguageDisplayName = function(languageCode) {
      var language =  languageMap[languageCode];
      return language ? language.displayName : '';
    };
  });
