describe('Unit testing Authority', function() {
  var $compile,
    $scope,
    Principal,
    html,
    html_Auth,
    element,
    element_Auth;
  beforeEach(mockApiAccountCall);
  beforeEach(mockI18nCalls);

  describe('when Principal dont retun a value', function() {
    beforeEach(inject(function(_$compile_, _$rootScope_, _Principal_) {
      $compile = _$compile_;
      $scope = _$rootScope_.$new();
      Principal = _Principal_;
      spyOn(Principal, 'hasAuthority').and.callFake(function() {
        return {
          then: function(callback) {
            return callback(false);
          }
        };
      });
      spyOn(Principal, 'hasAnyAuthority').and.callFake(function() {
        return false;
      });
      var html =
        '<li  has-authority="ROLE_ADMIN"> <a class="dropdown-toggle" data-toggle="dropdown" href=""><span><span class="glyphicon glyphicon-tower"></span><span class="hidden-sm" data-translate="global.menu.admin.main">Administration</span><b class="caret"></b></span></a></li>';
      element = $compile(html)($scope);
      html_Auth =
        '<li has-any-authority> <a class="dropdown-toggle" data-toggle="dropdown" href=""></a></li>';
      element_Auth = $compile(html_Auth)($scope);
    }));
    describe('Authority', function() {
      it("should be defined", function() {
        $scope.$digest();
        expect(element.find('a').hasClass('dropdown-toggle')).toBe(
          true);
        expect(element.length).toBe(1);
      });
      it("should have hidden class", function() {
        $scope.$digest();
        expect(element_Auth.hasClass('hidden')).toBe(true);
      });
    });
  });
  describe('when Principal retun a value', function() {
    beforeEach(inject(function(_$compile_, _$rootScope_, _Principal_) {
      $compile = _$compile_;
      $scope = _$rootScope_.$new();
      Principal = _Principal_;
      spyOn(Principal, 'hasAuthority').and.callFake(function() {
        return {
          then: function(callback) {
            return callback(true);
          }
        };
      });
      spyOn(Principal, 'hasAnyAuthority').and.callFake(function() {
        return true;
      });
      var html =
        '<li  has-authority="ROLE_ADMIN"> <a class="dropdown-toggle" data-toggle="dropdown" href=""><span><span class="glyphicon glyphicon-tower"></span><span class="hidden-sm" data-translate="global.menu.admin.main">Administration</span><b class="caret"></b></span></a></li>';
      element = $compile(html)($scope);
      html_Auth =
        '<li has-any-authority> <a class="dropdown-toggle" data-toggle="dropdown" href=""></a></li>';
      element_Auth = $compile(html_Auth)($scope);
    }));
    describe('Authority', function() {
      it("should be defined", function() {
        $scope.$digest();
        expect(element.find('a').hasClass('dropdown-toggle'))
          .toBe(
            true);
        expect(element.length).toBe(1);
      });
      it("should not have class hidden", function() {
        $scope.$digest();
        expect(element_Auth.hasClass('hidden')).toBe(false);
      });
    });
  });



});
