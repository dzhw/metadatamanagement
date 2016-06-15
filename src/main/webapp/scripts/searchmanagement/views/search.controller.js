/* Author: Daniel Katzberg */
'use strict';

/* The Controller for the search. It differs between tabs and a tab represent
a result of a type like variable or dataSet and so on. */
angular.module('metadatamanagementApp').controller('SearchController',
  function($scope, Principal, ElasticSearchProperties, $location,
    AlertService, VariableSearchDao, $timeout) {

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
    });

    //Search function
    $scope.search = function(pageNumber) {
      //Iterate the tabs. The search results have to be
      //different between the tabs
      $scope.tabs.forEach(function(tab) {
        var tabIndex = $scope.tabs.indexOf(tab);

        //save actual page for every pagination of the tab
        if (pageNumber && tabIndex === $scope.selectedTabIndex) {
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
      });
    };

    // --------------------------------------------------------------
    // In this example, we set up our model using a plain object.
    // Using a class works too. All that matters is that we implement
    // getItemAtIndex and getLength.
    this.infiniteItems = {
      numLoaded_: 0,
      toLoad_: 0,
      // Required.
      getItemAtIndex: function(index) {
        if (index > this.numLoaded_) {
          this.fetchMoreItems_(index);
          return null;
        }
        return index;
      },
      // Required.
      // For infinite scroll behavior, we always return a slightly higher
      // number than the previously loaded items.
      getLength: function() {
        return this.numLoaded_ + 5;
      },
      fetchMoreItems_: function(index) {
        // For demo purposes, we simulate loading more items with a timed
        // promise. In real code, this function would likely contain an
        // $http request.
        if (this.toLoad_ < index) {
          this.toLoad_ += 20;
          $timeout(angular.noop, 300).then(angular.bind(this,
            function() {
              this.numLoaded_ = this.toLoad_;
            }));
        }
      }
    };

    //found elements for the scope
    $scope.infiniteItems = this.infiniteItems;

    //Refresh function for the refresh button
    $scope.refresh = function() {
      $scope.search();
    };

    //Start the search.
    $scope.search();
  });
