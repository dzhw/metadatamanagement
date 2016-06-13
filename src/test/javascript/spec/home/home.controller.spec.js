'use strict';

describe('SearchController', function() {
  var $scope, $rootScope, Principal, createController;

  beforeEach(inject(function($injector) {
    $rootScope = $injector.get('$rootScope');
    $scope = $rootScope.$new();

    Principal = {
      identity: function() {
        return {
          then: function(callback) {
            return callback();
          }
        };
      },
      isAuthenticated: function() {
        return {
          then: function(callback) {
            return callback();
          }
        };
      }
    };


    var locals = {
      '$scope': $scope,
      'Principal': Principal
    };
    createController = function() {
      $injector.get('$controller')('SearchController', locals);
    };
    spyOn(Principal, 'identity').and.callThrough();
  }));
  describe('', function() {
    it('should call Principal.identity', function() {
      createController();
      expect(Principal.identity).toHaveBeenCalled();
    });
  });
});
