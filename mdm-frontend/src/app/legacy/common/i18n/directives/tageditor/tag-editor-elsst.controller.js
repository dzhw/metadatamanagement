/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('TagEditorElsstController', [
  '$scope',
  '$mdMedia', 
  'DataPackageSearchService', 'ElsstSearchService', function($scope, $mdMedia, DataPackageSearchService, ElsstSearchService) {

    // return all selectable tags that are available according to the search text and have not already selected
    var removeExistingTags = function(tags, language) {
      if (!$scope.tags[language] || $scope.tags[language].length == 0) {
        $scope.tags = {
          de: [],
          en: []
        };
        return tags;
      }

      return tags.filter(t => !$scope.tags[language].some(i => t.localname === i.localname));
    };

    var getOppositeLanguage = function(language) {
      var oppositeLanuage;
      if (language === 'en') {
        oppositeLanuage = 'de';
      } else if (language === 'de') {
        oppositeLanuage = 'en';
      } else {
        console.error('Unknown language', language);
        return;
      }

      return oppositeLanuage;
    }

    if (angular.isUndefined($scope.readonly)) {
      $scope.readonly = false;
    }

    if (angular.isUndefined($scope.requireGermanTag)) {
      $scope.requireGermanTag = false;
    }

    if (angular.isUndefined($scope.requireEnglishTag)) {
      $scope.requireEnglishTag = false;
    }

    if (angular.isUndefined($scope.tags)) {
      $scope.tags = {
        de: [],
        en: []
      };
    } else if (angular.isUndefined($scope.tags.de)) {
      $scope.tags.de = [];
    } else if (angular.isUndefined($scope.tags.en)) {
      $scope.tags.en = [];
    }

    $scope.selectedItemChange = function(item, language) {
      // translate a german selected item to english and add it to the english selected tags, and vice verca
      var translateLang = getOppositeLanguage(language);
      if (!item || !translateLang) {
        return;
      }
      
      ElsstSearchService.findTagsElsstTranslation(item.prefLabel, language, translateLang)
        .then(function(response) {
          if (!response) {
            return;
          }
          for (var i = 0; i < response.length; i++) {
            if ($scope.tags[translateLang].length == 0) {
              $scope.tags[translateLang] = [response[i]];
            } else {
              $scope.tags[translateLang] = [...$scope.tags[translateLang], response[i]];
            }
          }
        })
        .catch(function(error) {
          console.error(error);
        });
    };

    $scope.openLink = function(item, language) {
      var url = 'https://thesauri.cessda.eu/elsst-4/en/page/' + item.localname;

      if (language == 'de') {
        url += '?clang=de';
      }

      window.open(url, '_blank');
    }


    $scope.onChipRemoved = function(removedItem, language) {
      // make sure that the translated version of the recently removed tag is also removed
      var remLanguage = getOppositeLanguage(language);
      if (!remLanguage) {
        return;
      }

      $scope.tags[remLanguage] = $scope.tags[remLanguage].filter(item => item.localname !== removedItem.localname);
    }

    $scope.$mdMedia = $mdMedia;
    $scope.searchTags = function(searchText, language) {
      return $scope.tagSearch({searchText: searchText, language: language})
        .then(function(foundTags) {
          return removeExistingTags(foundTags, language);
        });
    };
  }]);

