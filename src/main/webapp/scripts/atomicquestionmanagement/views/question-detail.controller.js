'use strict';

angular.module('metadatamanagementApp')
  .controller('QuestionDetailController', ['$scope', 'entity',
    function($scope, entity) {
      $scope.question = entity;
      $scope.controller = {};

      $scope.controller.svg = null;
      $scope.controller.extractedText = null;

      console.log($scope.question);
      $scope.onFileReady = function(e) {
        var regexTags = /(<[a-z?!|/]([^>]+)>)/ig;
        var regexSyllableDelims = /-\s\s+/g;
        var regexMultipleDots = /\.\.+/g;
        var regexMultipleWhitespacs = /\s\s+/g;
        $scope.controller.svg = e.target.result;
        $scope.controller.extractedText = $scope.controller.svg.
        replace('image/svg+xml', '').replace(
          regexTags, ' ').replace(regexSyllableDelims, '').replace(
          regexMultipleDots, '').replace(regexMultipleWhitespacs, ' ');
        $scope.$apply();
        console.log($scope.controller.extractedText);
      };

      $scope.parseSvg = function(file) {
        var reader = new FileReader();
        reader.onload = $scope.onFileReady;
        if (file) {
          reader.readAsText(file);
        }
      };
    }
  ]);
