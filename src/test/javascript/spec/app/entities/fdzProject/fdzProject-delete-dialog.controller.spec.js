'use strict';

describe('FdzProject delete Controller', function() {
  var $scope, $rootScope,$uibModalInstance;
  var MockEntity;
  var createController;

  beforeEach(inject(function($injector) {
    $rootScope = $injector.get('$rootScope');
    $scope = $rootScope.$new();
    $uibModalInstance = {
          dismiss: jasmine.createSpy('$uibModalInstance.cancel'),
          close: jasmine.createSpy('$uibModalInstance.close'),
          result: {
            then: jasmine.createSpy('$uibModalInstance.result.then')
          }
        };
    MockEntity = {
          $delete: function() {
            return {
              then: function(callback) {
                 return callback();
               }
            };
          }
        };

    var locals = {
      '$scope': $scope,
      '$rootScope': $rootScope,
      'entity': MockEntity ,
      '$uibModalInstance': $uibModalInstance
    };
    createController = function() {
      $injector.get('$controller')('FdzProjectDeleteController', locals);
    };
  }));
  describe('FdzProjectDeleteController', function() {
    it('should call FdzProject.delete', function() {
      createController();
      $scope.confirmDelete();
      expect($uibModalInstance.close).toHaveBeenCalled();
    });
    it('should call $uibModalInstance.dismiss', function() {
          createController();
          $scope.clear();
          expect($uibModalInstance.dismiss).toHaveBeenCalled();
        });
  });
});
