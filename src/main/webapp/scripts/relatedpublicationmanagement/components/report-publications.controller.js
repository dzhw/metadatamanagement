'use strict';

angular.module('metadatamanagementApp')
  .controller('ReportPublicationsController',
    function($scope, $sessionStorage, $rootScope, CurrentProjectService,
      Principal) {
      $scope.isAuthenticated = Principal.isAuthenticated;
      $scope.currentProject = CurrentProjectService.getCurrentProject();
      $scope.$on('current-project-changed',
        /* jshint -W098 */
        function(event, currentProject) {
          /* jshint +W098 */
          $scope.currentProject = currentProject;
        });
      $scope.bowser = $rootScope.bowser;
      $scope.hideSpeechBubble = $sessionStorage.get(
        'report-publication.hideSpeechBubble') || $scope.isAuthenticated();
      $scope.closeSpeechBubble = function() {
        $sessionStorage.put('report-publication.hideSpeechBubble', true);
        $scope.hideSpeechBubble = true;
      };
    }
  );
