'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetDetailController', ['$scope', '$state', 'entity',
  'SurveySearchDialogService', 'VariableSearchDialogService',
  'ShoppingCartService', 'RelatedPublicationSearchDialogService',
  'DataSetReportService', 'Principal', 'RelatedPublicationSearchResource',
    function($scope, $state, entity, SurveySearchDialogService,
    VariableSearchDialogService, ShoppingCartService,
    RelatedPublicationSearchDialogService, DataSetReportService, Principal,
    RelatedPublicationSearchResource) {
      $scope.allRowsVisible = true;
      $scope.counts = {};
      entity.$promise.then(function(dataSet) {
        $scope.dataSet = dataSet;
        RelatedPublicationSearchResource.getCounts('dataSetIds',
            $scope.dataSet.id).then(function(publicationsCount) {
                $scope.counts.publicationsCount = publicationsCount.count;
              });
      });
      $scope.isAuthenticated = Principal.isAuthenticated;

      $scope.uploadTexTemplate = function(file, dataSetId) {
        DataSetReportService.uploadTexTemplate(file, dataSetId);
      };
      $scope.isRowHidden = function(index) {
        if (index <= 4 || index >= $scope
          .dataSet.subDataSets.length - 5) {
          return false;
        } else {
          return $scope.allRowsVisible;
        }
      };
      $scope.toggleAllRowsVisible = function() {
        $scope.allRowsVisible = !$scope.allRowsVisible;
      };
      $scope.showSurveys = function() {
        SurveySearchDialogService.findSurveys('findSurveys',
        $scope.dataSet.surveyIds, $scope.dataSet.surveyIds.length);
      };
      $scope.showVariables = function() {
        VariableSearchDialogService.findVariables('findVariables',
        $scope.dataSet.variableIds, $scope.dataSet.variableIds.length);
      };
      $scope.showStudy = function() {
        $state.go('studyDetail', {id: $scope.dataSet.dataAcquisitionProjectId});
      };
      $scope.showRelatedPublications = function() {
        RelatedPublicationSearchDialogService.
        findRelatedPublications('findByDataSetId', $scope.dataSet.id,
        $scope.counts.publicationsCount);
      };
      /* add new  item to localStorage */
      $scope.addToNotepad = function() {
        ShoppingCartService
        .addToShoppingCart($scope.dataSet.dataAcquisitionProjectId);
      };
    }
  ]);
