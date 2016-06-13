'use strict';

xdescribe('NavbarController', function() {
  var $scope, $state, Auth, Principal, $rootScope, createController;

  beforeEach(inject(function($injector) {
    $rootScope = $injector.get('$rootScope');
    $scope = $rootScope.$new();
    Principal = $injector.get('Principal');
    $state = $injector.get('$state');
    Auth = $injector.get('Auth');

    var locals = {
      '$scope': $scope,
      'Principal': Principal,
      '$state': $state,
      'Auth': Auth,
      'ENV': 'prod'
    };
    createController = function() {
      $injector.get('$controller')('NavbarController', locals);
    };
    spyOn($state, 'go').and.callThrough();
  }));
  it('should set $scope.survey', function() {
    createController();
    $scope.logout();
    expect($state.go).toHaveBeenCalledWith('search');
  });
});
