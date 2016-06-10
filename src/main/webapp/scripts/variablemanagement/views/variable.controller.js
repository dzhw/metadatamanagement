/* globals $ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableController', function($scope,
    VariableResource, $location, $filter, VariableSearchDao, AlertService,
    ElasticSearchProperties, $timeout) {
    // TODO create global page class which is returned from search daos
    $scope.page = {
      size: ElasticSearchProperties.pageSize,
      contentSize: 0,
      currentPageNumber: 1,
      totalHits: 0,

      getTotalNumberOfPages: function() {
        return Math.ceil(this.totalHits / this.size);
      },

      getMinHitNumber: function() {
        if (this.totalHits === 0) {
          return 0;
        }
        return ((this.currentPageNumber - 1) *
          this.size) + 1;
      },

      getMaxHitNumber: function() {
        if (this.totalHits === 0) {
          return 0;
        }
        if (this.hasNextPage()) {
          return this.currentPageNumber * this.size;
        } else {
          return this.getMinHitNumber() + this.contentSize - 1;
        }
      },

      hasNextPage: function() {
        return this.getTotalNumberOfPages() > this.currentPageNumber;
      },

      hasPreviousPage: function() {
        return this.currentPageNumber > 1;
      }
    };

    if ($location.search().page) {
      $scope.page.currentPageNumber = parseInt($location.search().page);
    }

    $scope.query = $location.search().query;

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

    $scope.$on('variable.created', function() {
      $scope.search();
    });

    $scope.$on('variable.updated', function() {
      $scope.search();
    });

    $scope.search();

    $scope.refresh = function() {
      $scope.search();
    };

    $scope.nextPage = function() {
      if ($scope.page.hasNextPage()) {
        $scope.page.currentPageNumber++;
        $scope.search();
      }
    };

    $scope.previousPage = function() {
      if ($scope.page.hasPreviousPage()) {
        $scope.page.currentPageNumber--;
        $scope.search();
      }
    };

    $scope.delete = function(id) {
      VariableResource.get({
        id: id
      }, function(result) {
        $scope.variable = result;
      });
      $scope.item = $filter('filter')($scope.searchResult,
        function(variable) {
          return variable._id === id;
        })[0];
      $scope.itemtoBeRemoved = $scope.searchResult.indexOf($scope.item);
      $('#deleteVariableConfirmation').modal('show');
    };

    $scope.confirmDelete = function(id) {
      $scope.page.contentSize = $scope.page.contentSize - 1;
      $scope.page.totalHits = $scope.page.totalHits - 1;
      $scope.searchResult.splice($scope.itemtoBeRemoved, 1);
      VariableResource.delete({
          id: id
        },
        function() {
          $('#deleteVariableConfirmation').modal('hide');
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

    $scope.infiniteItems = this.infiniteItems;
  });
