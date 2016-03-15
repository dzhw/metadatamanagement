'use strict';

describe('Controllers Tests ', function() {
  var $scope, createController, Variable, AlertService, VariableSearchDao,
    MockEntity, $q, $location;
  var data = {
    hits: {
      total: 1,
      max_score: 1,
      hits: [{
        _index: 'metadata_de',
        _type: 'variables',
        _id: '564dd567ce1a0ed573dbda51',
        _score: 1,
        _source: {
          id: '564dd567ce1a0ed573dbda51',
          dataType: 'string',
          scaleLevel: 'nominal',
          label: 'Test_Mock_de_1',
          name: 'Test_de_1'
        }
      }]
    }
  };
  beforeEach(mockApis);
  beforeEach(function() {
    inject(function($controller, _$rootScope_, _AlertService_, _$q_,
      _$location_) {
      $scope = _$rootScope_.$new();
      AlertService = _AlertService_;
      $location = _$location_;
      $q = _$q_;
      MockEntity = {
        $save: function(success, error) {
          success();
          error();
        }
      };
      Variable = {
        get: function(param, callback) {
          var deferred = $q.defer();
          deferred.resolve(data);
          callback(data.hits.hits[0]);
          return deferred.promise;
        },
        delete: function(param, callback) {
          var deferred = $q.defer();
          callback();
          return deferred.promise;
        }
      };
      VariableSearchDao = {
        search: function(queryterm, pageNumber) {
          return {
            then: function(callback, error) {
              callback(data);
              error({
                message: 'error'
              });
              return callback(data);
            }
          };
        }
      };
      var locals = {
        '$scope': $scope,
        'entity': MockEntity,
        'AlertService': AlertService,
        'Variable': Variable,
        'VariableSearchDao': VariableSearchDao,
        '$location': $location
      };
      $location.search('page', 1);
      createController = function() {
        return $controller('VariablesController', locals);
      };
      spyOn(VariableSearchDao, 'search').and.callThrough();
      spyOn(Variable, 'get').and.callThrough();
      spyOn(Variable, 'delete').and.callThrough();
      spyOn(AlertService, 'error').and.callThrough();
    });
  });
  describe('VariablesController', function() {
    beforeEach(function() {
      createController();
    });
    it('should set $scope.searchResult to 1', function() {
      $scope.search(1);
      expect($scope.searchResult.length).toEqual(1);
    });
    it('should call AlertService.error with error', function() {
      $scope.search(1);
      expect(AlertService.error).toHaveBeenCalledWith('error');
    });
    it('should call $scope.search', function() {
      $scope.refresh();
      expect($scope.searchResult.length).toEqual(1);
    });
    it('should call hasNextPage', function() {
      spyOn($scope.page, 'hasNextPage').and.callThrough();
      $scope.nextPage();
      expect($scope.page.hasNextPage).toHaveBeenCalled();
    });
    it('should set currentPageNumber to 2', function() {
      spyOn($scope.page, 'hasNextPage').and.callFake(function() {
        return 2;
      });
      $scope.nextPage();
      expect($scope.page.currentPageNumber).toEqual(2);
    });
    it('should call $scope.search', function() {
      spyOn($scope.page, 'hasPreviousPage').and.callThrough();
      $scope.previousPage();
      expect($scope.page.hasPreviousPage).toHaveBeenCalled();
    });
    it('should set currentPageNumber to 0', function() {
      spyOn($scope.page, 'hasPreviousPage').and.callFake(function() {
        return 1;
      });
      $scope.previousPage();
      expect($scope.page.currentPageNumber).toEqual(0);
    });
    it('should delete variable with id 564dd567ce1a0ed573dbda51',
      function() {
        $scope.delete();
        expect($scope.variable._id).toEqual(
          '564dd567ce1a0ed573dbda51');
      });
    it('should set totalHits to 0', function() {
      $scope.search(1);
      $scope.confirmDelete('564dd567ce1a0ed573dbda51');
      expect($scope.page.totalHits).toEqual(0);
    });
    it('should call $scope.search() on variable.created event',
      function() {
        spyOn($scope, 'search').and.callThrough();
        $scope.$broadcast('variable.created');
        expect($scope.search).toHaveBeenCalled();
      });
    it('should call $scope.search() on variable.updated event',
      function() {
        spyOn($scope, 'search').and.callThrough();
        $scope.$broadcast('variable.updated');
        expect($scope.search).toHaveBeenCalled();
      });
    it('should set MinHitNumber to 1', function() {
      expect($scope.page.getMinHitNumber()).toEqual(1);
    });
    it('should set MinHitNumber to 0', function() {
      $scope.page.totalHits = 0;
      $scope.$apply();
      expect($scope.page.getMinHitNumber()).toEqual(0);
    });
    it('should set MaxHitNumber to 0', function() {
      expect($scope.page.getMaxHitNumber()).toEqual(0);
    });
    it('should set MaxHitNumber to 10', function() {
      spyOn($scope.page, 'hasNextPage').and.callFake(function() {
        return 1;
      });
      expect($scope.page.getMaxHitNumber()).toEqual(10);
    });
    it('should set MaxHitNumber to 0', function() {
      $scope.page.totalHits = 0;
      $scope.$apply();
      expect($scope.page.getMaxHitNumber()).toEqual(0);
    });
  });
});
