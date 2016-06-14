/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').controller('SearchController',
  function($scope, Principal, ElasticSearchProperties, $location,
    AlertService, VariableSearchDao, $timeout) {
    Principal.identity().then(function(account) {
      $scope.account = account;
      $scope.isAuthenticated = Principal.isAuthenticated;
    });

    //Information about the search results and the page.
    $scope.page = {
      size: ElasticSearchProperties.pageSize,
      contentSize: 0,
      currentPageNumber: 1,
      totalHits: 0,

      //Total number of pages (Pagination)
      getTotalNumberOfPages: function() {
        return Math.ceil(this.totalHits / this.size);
      },

      //Actiated Next Page Button?
      hasNextPage: function() {
        return this.getTotalNumberOfPages() > this.currentPageNumber;
      },

      //Activated Previous Page Button?
      hasPreviousPage: function() {
        return this.currentPageNumber > 1;
      }
    };

    //Need for interpretation of the query element in the url.
    $scope.query = $location.search().query;

    //Search function
    $scope.search = function(pageNumber) {
      if (pageNumber) {
        $scope.page.currentPageNumber = pageNumber;
      }
      $location.search('query', $scope.query);
      $location.search('page', $scope.page.currentPageNumber);
      VariableSearchDao.search($scope.query, $scope.page.currentPageNumber)
        .then(function(data) {
          $scope.searchResult = data.hits.hits;
          $scope.page.contentSize = $scope.searchResult.length;
          $scope.page.totalHits = data.hits.total;
        }, function(error) {
          AlertService.error(error.message);
          console.trace(error);
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

    //Go to the next page of results
    $scope.nextPage = function() {
      if ($scope.page.hasNextPage()) {
        $scope.page.currentPageNumber++;
        $scope.search();
      }
    };

    //Go to the previous page of results
    $scope.previousPage = function() {
      if ($scope.page.hasPreviousPage()) {
        $scope.page.currentPageNumber--;
        $scope.search();
      }
    };

    //Start the search.
    $scope.search();
  });
