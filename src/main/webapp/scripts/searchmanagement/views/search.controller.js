/* Author: Daniel Katzberg */
'use strict';

/* The Controller for the search. It differs between tabs and a tab represent
a result of a type like variable or dataSet and so on. */
angular.module('metadatamanagementApp').controller('SearchController',
  function($scope, Principal, ElasticSearchProperties, $location,
    AlertService, SearchDao, $translate, CustomModalService,
    VariableUploadService, CurrentProjectService, $mdDialog,
    CleanJSObjectService, SearchToastService) {

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

    /* Helper method for getting an index by the elastic search type */
    function getTabIndexBySearchType(searchType) {
      var index;
      $scope.tabs.forEach(function(tab) {
        if (tab.elasticSearchType === searchType) {
          index = $scope.tabs.indexOf(tab);
        }
      });
      return index;
    }

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
      SearchDao.search($scope.query, tab.currentPageNumber,
          tab.elasticSearchType)
        .then(function(data) {
          tab.searchResult = data.hits.hits;
          tab.totalHits = data.hits.total;

          //Every other tab adds the number of 'all' tabs
          if (!CleanJSObjectService.isNullOrEmpty(tab.elasticSearchType)) {
            $scope.tabs[getTabIndexBySearchType('')].totalHits = data.hits
              .total;
          }

          //Count information by aggregations
          if (!CleanJSObjectService.isNullOrEmpty(data.aggregations)) {
            // jscs:disable
            $scope.tabs[getTabIndexBySearchType('variables')].totalHits =
              data.aggregations.countVariables.doc_count;
            // jscs:enable
          }

          //If something going wrong: send an alert
        }, function(error) {
          AlertService.error(error.message);
          console.trace(error);
        });
    };

    $scope.uploadVariables = function(file) {
      var dataAcquisitionProject = CurrentProjectService.getCurrentProject();
      if (file !== null &&
        !CleanJSObjectService.isNullOrEmpty(dataAcquisitionProject)) {
        // Appending dialog to document.body to cover sidenav in docs app
        var confirm = $mdDialog.confirm()
          .title()
          .textContent($translate.instant(
            'search.deleteMessages.deleteVariables', {
              id: dataAcquisitionProject.id
            }))
          .ariaLabel($translate.instant(
            'search.deleteMessages.deleteVariables', {
              id: dataAcquisitionProject.id
            }))
          .ok($translate.instant(
            'search.buttons.ok', {
              id: dataAcquisitionProject.id
            }))
          .cancel($translate.instant(
            'search.buttons.cancel', {
              id: dataAcquisitionProject.id
            }));
        $mdDialog.show(confirm).then(function() {
          //start upload and open log toast
          VariableUploadService
            .uploadVariables(file, dataAcquisitionProject.id);

          //Cancel. Nothing happens
        }, function() {});
      } else {
        SearchToastService.openNoProjectToast();
      }
    };

    //Refresh function for the refresh button
    $scope.refresh = function() {
      $scope.search();
    };
  });
