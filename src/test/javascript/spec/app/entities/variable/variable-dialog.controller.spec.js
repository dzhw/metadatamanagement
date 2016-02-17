'use strict';

describe('Controllers Tests ', function() {
  var $scope, $controller, createController, $uibModalInstance, MockEntity,
    FdzProjectCollection, SurveyCollection, $q, isCreateMode;
  var result = {
    'page': {
      'totalElements': 2
    },
    '_embedded': {
      'fdzProjects': []
    }
  };
  beforeEach(mockApiAccountCall);
  beforeEach(mockI18nCalls);
  beforeEach(function() {
    inject(function(_$controller_, _$rootScope_, _$q_) {
      $scope = _$rootScope_.$new();
      $q = _$q_;
      $controller = _$controller_;
      MockEntity = {
        $save: function(success, error) {
          error();
          return {};
        }
      };
      FdzProjectCollection = {
        query: function(callback) {
          var deferred = $q.defer();
          deferred.resolve(result);
          callback(result);
          return deferred.promise;
        }
      };
      SurveyCollection = {
        query: function(par, callback) {
          var deferred = $q.defer();
          deferred.resolve(result);
          callback(result);
          return deferred.promise;
        }
      };
      $uibModalInstance = {
        dismiss: jasmine.createSpy('$uibModalInstance.cancel'),
        close: jasmine.createSpy('$uibModalInstance.close'),
        result: {
          then: jasmine.createSpy('$uibModalInstance.result.then')
        }
      };
      spyOn(FdzProjectCollection, 'query').and.callThrough();
      spyOn(SurveyCollection, 'query').and.callThrough();
      spyOn($scope, '$broadcast');
    });
  });
  describe('VariableDialogController with createMode', function() {
    beforeEach(function() {
      isCreateMode = true;
      var locals = {
        '$scope': $scope,
        'entity': MockEntity,
        '$uibModalInstance': $uibModalInstance,
        'isCreateMode': isCreateMode,
        'FdzProjectCollection': FdzProjectCollection,
        'SurveyCollection': SurveyCollection
      };
      createController = function() {
        return $controller('VariableDialogController', locals);
      };
      createController();
    });
    it('should call $scope.$broadcast with variable.created', function() {
      $scope.save();
      expect($scope.$broadcast).toHaveBeenCalledWith(
        'variable.created');
    });
    it('$scope.isSurveyEmpty should be true', function() {
      var returnValue = $scope.isSurveyEmpty();
      expect(returnValue).toEqual(true);
    });
  });
  describe('VariableDialogController without createMode', function() {
    beforeEach(function() {
      isCreateMode = false;
      var locals = {
        '$scope': $scope,
        'entity': MockEntity,
        '$uibModalInstance': $uibModalInstance,
        'isCreateMode': isCreateMode,
        'FdzProjectCollection': FdzProjectCollection,
        'SurveyCollection': SurveyCollection
      };
      createController = function() {
        return $controller('VariableDialogController', locals);
      };
      createController();
    });
    it('should call $scope.$broadcast with variable.updated', function() {
      $scope.save();
      expect($scope.$broadcast).toHaveBeenCalledWith(
        'variable.updated');
    });
    it('$scope.isSurveyEmpty should be true', function() {
      $scope.allSurveysByFdzProjectId = [];
      var returnValue = $scope.isSurveyEmpty();
      expect(returnValue).toEqual(true);
    });
    it('$scope.isSurveyEmptyg should be false', function() {
      $scope.allSurveysByFdzProjectId = [{}];
      var returnValue = $scope.isSurveyEmpty();
      expect(returnValue).toEqual(false);
    });
    it('should call $uibModalInstance.dismiss', function() {
      $scope.clear();
      expect($uibModalInstance.dismiss).toHaveBeenCalled();
    });
  });
});
