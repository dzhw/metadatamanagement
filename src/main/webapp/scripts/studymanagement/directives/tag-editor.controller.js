/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('TagEditorController', function($scope, $mdMedia,
                                              StudySearchService) {

    var removeExistingTags = function(tags, language) {
      return _.difference(tags, $scope.tags[language]);
    };

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
      return StudySearchService.findTags(searchText, language)
        .then(function(foundTags) {
          return removeExistingTags(foundTags, language);
        });
    };
  });
