/* global describe */
/* global beforeEach */
/* global inject */
/* global it */
/* global expect */
/* global jasmine */

'use strict';

describe('DataAcquisitionProject delete Controller', function() {
  var $scope;
  var $rootScope;
  var $uibModalInstance;
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
      $injector.get('$controller')('DataAcquisitionProjectDeleteController',
      locals);
    };
  }));
  describe('DataAcquisitionProjectDeleteController', function() {
    it('should call $uibModalInstance.close', function() {
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
