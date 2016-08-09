describe('Unit testing jhAlert', function() {
  var $compile,
      $rootScope,
      $scope,
      element,
      element_error,
      controller,
      controller_error;
  beforeEach(mockApis);
  beforeEach(inject(function(_$compile_, _$rootScope_, _AlertService_){
    $compile = _$compile_;
    $scope = _$rootScope_.$new();
    $rootScope = _$rootScope_;
    AlertService = _AlertService_;
    var html =   '<jh-alert></jh-alert>';
    var html_error =   '<jh-alert-error></jh-alert-error>';
    element = $compile(html)($scope);
    element_error = $compile(html_error)($scope);
    $scope.$digest();
    controller = element.controller;
    controller_error = element_error.controller;
  }));

  describe('jhAlert', function () {
      it("Should display jhAlert", function() {
          expect(element.find('div').length).toEqual(1);
      });
      it("Should set $scope.alerts", function() {
          $scope.$broadcast('$destroy');
          expect($scope.alerts).toEqual([]);
      });
      it("Should display jhAlertError", function() {
          expect(element_error.find('div').length).toEqual(1);
      });
      xit("Should display jhAlert", function() {
        var event = jasmine.createSpyObj('event', ['preventDefault', 'stopPropagation']);
          $rootScope.$broadcast('httpError');
          //expect($scope.alerts).toEqual([]);
          //$scope.addErrorAlert();
        //  alert(controller_error);
      });
  });
});
