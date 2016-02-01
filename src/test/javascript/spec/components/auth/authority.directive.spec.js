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

  beforeEach(inject(function(_$compile_, _$rootScope_, _Principal_){
    $compile = _$compile_;
    $scope = _$rootScope_.$new();
    Principal = _Principal_;
    var html =   '<li  has-authority="ROLE_ADMIN"> <a class="dropdown-toggle" data-toggle="dropdown" href=""><span><span class="glyphicon glyphicon-tower"></span><span class="hidden-sm" data-translate="global.menu.admin.main">Administration</span><b class="caret"></b></span></a></li>';
    element = $compile(html)($scope);
    html_Auth =   '<li has-any-authority></li>';
    element_Auth = $compile(html_Auth)($scope);
  }));
  /*beforeEach(function(){
    spyOn(Principal, 'hasAuthority').and.callFake(function() {
      return {
        then: function(callback) { return callback(true); }
      };
    });
  });*/
  describe('Authority', function () {
      it("hshould be defined", function() {
        element.find('a').click();
        $scope.$digest();
        expect(element.find('a').hasClass('dropdown-toggle')).toBe(true);
        expect(element_Auth.length).toBe(1);
  });
});
});
