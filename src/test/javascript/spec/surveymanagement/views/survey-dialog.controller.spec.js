/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */
/* global mockApis */
/* global spyOn */
/* global jasmine */

'use strict';

describe('Controllers Tests ', function() {
  var $scope;
  var createController;
  var $uibModalInstance;
  var MockEntity;
  var DataAcquisitionProjectCollectionResource;
  var $q;
  var result = {
    'page': {
      'totalElements': 2
    },
    '_embedded': {
      'dataAcquisitionProjects': []
    }
  };
  beforeEach(mockApis);
  beforeEach(function() {
    inject(function($controller, _$rootScope_, _$q_) {
      $scope = _$rootScope_.$new();
      $q = _$q_;
      MockEntity = {
        $save: function(success, error) {
          success();
          error();
        }
      };
      DataAcquisitionProjectCollectionResource = {
        query: function(callback) {
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
      var locals = {
        '$scope': $scope,
        'entity': MockEntity ,
        '$uibModalInstance': $uibModalInstance,
        'isCreateMode': true,
        'DataAcquisitionProjectCollectionResource':
                             DataAcquisitionProjectCollectionResource
      };
      createController = function() {
        return $controller('SurveyDialogController', locals);
      };
      spyOn(DataAcquisitionProjectCollectionResource, 'query').and
      .callThrough();
    });
  });
  describe('SurveyDialogController',function() {
     beforeEach(function() {
       createController();
     });
     it('$scope.isSaving should be false',function() {
       $scope.save();
       expect($scope.isSaving).toEqual(false);
     });
     it('should call $uibModalInstance.dismiss',function() {
       $scope.clear();
       expect($uibModalInstance.dismiss).toHaveBeenCalled();
     });
     it('should set $scope.allDataAcquisitionProjects',function() {
       DataAcquisitionProjectCollectionResource.query.and.
       returnValue($q.resolve());
       expect($scope.allDataAcquisitionProjects.$$state.value).toEqual(result);
     });

   });
});
