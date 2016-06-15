/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').controller('SearchController',
  function($scope, Principal, ElasticSearchProperties, $location,
    AlertService, VariableSearchDao, $timeout) {
    Principal.identity().then(function(account) {
      $scope.account = account;
      $scope.isAuthenticated = Principal.isAuthenticated;
    });

    $scope.selectedTabIndex = 0;

    //Information for the different tabs
    $scope.tabs = [{
      title: 'global.menu.search.all',
      elasticSearchType: '',
      totalHits: 0,
      searchResult: []
    }, {
      title: 'global.menu.search.variables',
      elasticSearchType: 'variables',
      totalHits: 0,
      searchResult: []
    }];

    //Watch the tabs, set index of current active tab
    $scope.$watch('selectedTabs', function(current) {
      $scope.selectedTabIndex = current;
    });

    //Information for the pager / pagination
    $scope.pager = {
      currentPageNumber: 1,
      maxSize: 7,
    };

    //Need for interpretation of the query element in the url.
    $scope.query = $location.search().query;

    //Search function
    $scope.search = function(pageNumber) {
      $scope.tabs.forEach(function(tab) {

        if (pageNumber) {
          $scope.pager.currentPageNumber = pageNumber;
        }

        $location.search('query', $scope.query);
        $location.search('page', $scope.pager.currentPageNumber);
        VariableSearchDao.search($scope.query, $scope.pager.currentPageNumber,
            tab.elasticSearchType)
          .then(function(data) {
            tab.searchResult = data.hits.hits;
            tab.totalHits = data.hits.total;
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
