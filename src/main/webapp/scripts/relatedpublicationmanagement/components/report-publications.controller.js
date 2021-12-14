'use strict';

angular.module('metadatamanagementApp')
  .controller('ReportPublicationsController',
    function($scope, $sessionStorage, $rootScope, CurrentProjectService,
      Principal, CurrentDataPackageService, DataPackageIdBuilderService,
      CurrentAnalysisPackageService, AnalysisPackageIdBuilderService) {
      $scope.isAuthenticated = Principal.isAuthenticated;
      if (!$scope.analysisPackage && !$scope.dataPackageId) {
        $scope.dataPackageId = CurrentDataPackageService
          .getCurrentDataPackage() ?
          CurrentDataPackageService.getCurrentDataPackage().masterId : null;
        if (!$scope.dataPackageId && CurrentProjectService
          .getCurrentProject()) {
          $scope.dataPackageId =
            DataPackageIdBuilderService.buildDataPackageId(
              CurrentProjectService.getCurrentProject().id);
        }
      } else {
        $scope.analysisPackageId = CurrentAnalysisPackageService
          .getCurrentAnalysisPackage() ?
          CurrentAnalysisPackageService
            .getCurrentAnalysisPackage().masterId : null;
        if (!$scope.analysisPackageId && CurrentProjectService
          .getCurrentProject()) {
          $scope.analysisPackageId =
            AnalysisPackageIdBuilderService.buildAnalysisPackageId(
              CurrentProjectService.getCurrentProject().id);
        }
      }
      $scope.$on('current-project-changed',
        /* jshint -W098 */
        function(event, currentProject) {
          /* jshint +W098 */
          if (currentProject) {
            if (!$scope.analysisPackage) {
              $scope.dataPackageId =
                DataPackageIdBuilderService
                  .buildDataPackageId(currentProject.id);
            } else {
              $scope.analysisPackageId =
                AnalysisPackageIdBuilderService
                  .buildAnalysisPackageId(currentProject.id);
            }
          }
        });
      $scope.$on('current-data-package-changed',
        /* jshint -W098 */
        function(event, currentDataPackage) {
          /* jshint +W098 */
          $scope.dataPackageId = currentDataPackage ?
            currentDataPackage.masterId : null;
        });
      $scope.$on('current-analysis-package-changed',
        /* jshint -W098 */
        function(event, currentAnalysisPackage) {
          /* jshint +W098 */
          $scope.analysisPackageId = currentAnalysisPackage ?
            currentAnalysisPackage.masterId : null;
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
