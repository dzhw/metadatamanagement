/* global FileReader */
'use strict';

angular.module('metadatamanagementApp')
    .controller('TestSvgController', ['$scope', '$sce',
        function($scope, $sce) {
            $scope.svg = null;
            $scope.extractedText = null;

            $scope.onFileReady = function(e) {
                var regexTags = /(<[a-z?!|/]([^>]+)>)/ig;
                var regexSyllableDelims = /-\s\s+/g;
                var regexMultipleDots = /\.\.+/g;
                var regexMultipleLines = /––+/g;
                var regexMultipleWhitespacs = /\s\s+/g;
                $scope.svg = $sce.trustAsHtml(e.target.result);
                $scope.extractedText = e.target.result
                .replace('image/svg+xml', '')
                .replace(regexTags, ' ')
                .replace(regexSyllableDelims, '')
                .replace(regexMultipleDots, '')
                .replace(regexMultipleLines, '')
                .replace(regexMultipleWhitespacs, ' ');
                $scope.$apply();
              };

            $scope.parseSvg = function(file) {
                var reader = new FileReader();
                reader.onload = $scope.onFileReady;
                if (file) {
                  $scope.svg = null;
                  $scope.extractedText = null;
                  reader.readAsText(file);
                }
              };
          }
    ]);
