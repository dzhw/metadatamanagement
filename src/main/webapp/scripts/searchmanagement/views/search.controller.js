/* Author: Daniel Katzberg */
'use strict';

/* The Controller for the search. It differs between tabs and a tab represent
a result of a type like variable or dataSet and so on. */
angular.module('metadatamanagementApp').controller('SearchController',
  function($scope, Principal, ElasticSearchProperties, $location,
    AlertService, VariableSearchDao, $translate, CustomModalService,
    VariableUploadService, CurrentProjectService, $mdToast,
    CleanJSObjectService) {

    //Check the login status
    Principal.identity().then(function(account) {
      $scope.account = account;
      $scope.isAuthenticated = Principal.isAuthenticated;
    });

    //Max number of button at a pager / pagination
    $scope.pagerMaxSize = 7;

    //Need for interpretation of the query element in the url.
    $scope.query = $location.search().query;

    //The current index of the active tab
    $scope.selectedTabIndex = 0;

    //Information for the different tabs
    $scope.tabs = [{
      title: 'global.menu.search.all',
      elasticSearchType: '',
      totalHits: 0,
      searchResult: [],
      currentPageNumber: 1
    }, {
      title: 'global.menu.search.variables',
      elasticSearchType: 'variables',
      totalHits: 0,
      searchResult: [],
      currentPageNumber: 1
    }];

    //Watch the tabs, set index of current active tab
    $scope.$watch('selectedTabs', function(current) {
      $scope.selectedTabIndex = current;

      //Reset search, if tab is changed
      $scope.tabs.forEach(function(tab) {
        tab.totalHits = 0;
        tab.searchResult = [];
        tab.currentPageNumber = 1;
      });

      //Search
      $scope.search();
    });

    //Search function
    $scope.search = function(pageNumber) {
      //Iterate the tabs. The search results have to be
      //different between the tabs
      var tab = $scope.tabs[$scope.selectedTabIndex];

      //check for undefined tab. no valid tab, no valid search.
      if (!tab) {
        return;
      }

      //save actual page for every pagination of the tab
      if (pageNumber) {
        tab.currentPageNumber = pageNumber;
      }

      //Search with different types, binded on every tab
      $location.search('query', $scope.query);
      $location.search('page', tab.currentPageNumber);
      VariableSearchDao.search($scope.query, tab.currentPageNumber,
          tab.elasticSearchType)
        .then(function(data) {
          tab.searchResult = data.hits.hits;
          tab.totalHits = data.hits.total;

          //If something going wrong: send an alert
        }, function(error) {
          AlertService.error(error.message);
          console.trace(error);
        });
    };

    //The Toast for the upload complete
    function openLogToast() {
      $mdToast.show({
        controller: 'SearchToastController',
        templateUrl: 'scripts/searchmanagement/' +
          'views/upload-complete-toast.html.tmpl',
        hideDelay: 0,
        position: 'top right'
      });
    }

    //The Toast for no project is choosen
    function openNoProjectToast() {
      $mdToast.show({
        controller: 'SearchToastController',
        templateUrl: 'scripts/searchmanagement/' +
          'views/no-project-toast.html.tmpl',
        hideDelay: 10000,
        position: 'top right'
      });

    }

    $scope.uploadVariables = function(file) {
      var dataAcquisitionProject = CurrentProjectService.getCurrentProject();
      if (file !== null &&
        !CleanJSObjectService.isNullOrEmpty(dataAcquisitionProject)) {
        CustomModalService.getModal($translate.instant(
          'metadatamanagementApp.dataAcquisitionProject.detail.' +
          'deleteMessages.deleteVariables', {
            id: dataAcquisitionProject.id
          })).then(function(returnValue) {
          if (returnValue) {
            VariableUploadService
              .uploadVariables(file, dataAcquisitionProject.id);
          }
        }).then(function() {
          openLogToast();
        });
      } else {
        openNoProjectToast();
      }
    };

    //Refresh function for the refresh button
    $scope.refresh = function() {
      $scope.search();
    };

    //Start the search.
    $scope.search();
  });
