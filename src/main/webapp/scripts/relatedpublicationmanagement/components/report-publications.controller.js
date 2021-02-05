'use strict';

angular.module('metadatamanagementApp')
  .controller('ReportPublicationsController',
    function($scope, $sessionStorage) {
      $scope.hideSpeechBubble = $sessionStorage.get(
        'report-publication.hideSpeechBubble') || false;
      $scope.closeSpeechBubble = function() {
        $sessionStorage.put('report-publication.hideSpeechBubble', true);
        $scope.hideSpeechBubble = true;
      };
    }
  );
