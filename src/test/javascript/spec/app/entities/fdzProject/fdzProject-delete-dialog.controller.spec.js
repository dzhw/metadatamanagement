'use strict';

xdescribe('FdzProject delete Controller', function() {
  var $scope, $rootScope,$uibModalInstance, FdzProject;
  var MockEntity;
  var createController;

  beforeEach(inject(function($injector) {
    $rootScope = $injector.get('$rootScope');
    $scope = $rootScope.$new();
    MockEntity = jasmine.createSpy('MockEntity');
    $uibModalInstance = {
          dismiss: jasmine.createSpy('$uibModalInstance.cancel'),
          result: {
            then: jasmine.createSpy('$uibModalInstance.result.then')
          }
        };
    FdzProject = {
          delete: function() {
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
      'FdzProject': FdzProject,
      '$uibModalInstance': $uibModalInstance
    };
    createController = function() {
      $injector.get('$controller')('FdzProjectDeleteController', locals);
    };
    spyOn(FdzProject, 'delete').and.callThrough();
  }));
  describe('FdzProjectDeleteController', function() {
    it('should call FdzProject.delete', function() {
      createController();
      $scope.confirmDelete();
      expect(FdzProject.delete).toHaveBeenCalled();
    });
    it('should call $uibModalInstance.dismiss', function() {
          createController();
          $scope.clear();
          expect($uibModalInstance.dismiss).toHaveBeenCalled();
        });
  });
});
