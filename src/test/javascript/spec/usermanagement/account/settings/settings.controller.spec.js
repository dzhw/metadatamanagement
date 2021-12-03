'use strict';

describe('Controllers Tests ', function() {
  beforeEach(mockSso);
  beforeEach(mockApis);

  describe('SettingsController', function() {

    var $scope, $q; // actual implementations
    var MockPrincipal, MockAuth; // mocks
    var createController; // local utility functions

    beforeEach(inject(function($injector) {
      $q = $injector.get('$q');
      $scope = $injector.get("$rootScope").$new();
      MockAuth = jasmine.createSpyObj('MockAuth', ['updateAccount']);
      MockPrincipal = jasmine.createSpyObj('MockPrincipal', ['identity']);
      var locals = {
        '$scope': $scope,
        'Principal': MockPrincipal,
        'Auth': MockAuth
      };
      createController = function() {
        $injector.get('$controller')('SettingsController', locals);
      }
    }));

  });
});
