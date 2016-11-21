'use strict';

describe('LanguageController', function() {
  var $scope, $rootScope, LanguageService, $location, $translate,
    createController;

  beforeEach(inject(function($injector) {
    $rootScope = $injector.get('$rootScope');
    $scope = $rootScope.$new();
    $translate = $injector.get('$translate');
    $location = $injector.get('$location');
    LanguageService = {
      getAll: function() {
        return {
          then: function(callback) {
            return callback();
          }
        };
      },
      setCurrent: function() {
        return {
          then: function(callback) {
            return callback();
          }
        };
      }
    };

    var locals = {
      '$rootScope': $rootScope,
      '$scope': $scope,
      '$location': $location,
      '$translate': $translate,
      'LanguageService': LanguageService
    };
    createController = function() {
      $injector.get('$controller')('LanguageController', locals);
    };
    spyOn(LanguageService, 'setCurrent').and.callThrough();

  }));
  describe('LanguageController', function() {
    it('should ', function() {
      createController();
      $scope.changeLanguage('');
      expect(LanguageService.setCurrent).toHaveBeenCalled();
    });
  });
});
