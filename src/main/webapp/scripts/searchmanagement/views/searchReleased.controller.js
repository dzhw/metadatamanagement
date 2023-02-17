/* global _, bowser */

'use strict';

/* The Controller for the search. It differs between tabs and a tab represent
a result of a type like variable or dataSet and so on. */
angular.module('metadatamanagementApp').controller('SearchReleasedController',
  function($scope, Principal, $location, $state, SearchDao, MessageBus,
           VariableUploadService, ProjectUpdateAccessService,
           QuestionUploadService, RelatedPublicationUploadService,
           CleanJSObjectService, CurrentProjectService, $timeout,
           PageMetadataService, BreadcrumbService, SearchHelperService,
           SearchResultNavigatorService, DataPackageResource,
           AnalysisPackageResource, DataPackageIdBuilderService,
           AnalysisPackageIdBuilderService,
           $rootScope, ProjectStatusScoringService, DeleteMetadataService,
           SimpleMessageToastService, $mdSidenav, $analytics) {

  console.log("you are using the new controller");
  $scope.message = "This is to order data packages";
});