/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('TagEditorController', function($scope, $mdMedia) {

    var removeExistingTags = function(tags, language) {
      return _.difference(tags, $scope.tags[language]);
    };

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

    $scope.$mdMedia = $mdMedia;
    $scope.searchTags = function(searchText, language) {
      return $scope.tagSearch({searchText: searchText, language: language})
        .then(function(foundTags) {
          return removeExistingTags(foundTags, language);
        });
    };
  });
