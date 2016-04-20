describe('Unit testing activeMenu', function() {
  var $compile,
      $scope,
      $translate,
      element,
      element_link;
beforeEach(mockApis);

  beforeEach(inject(function(_$compile_, _$rootScope_, _$translate_){
    $compile = _$compile_;
    $scope = _$rootScope_.$new();
    $translate = _$translate_;
    var html =   '<li active-menu="{{language}}" class="active">  <a href="" ng-click="changeLanguage(language)"></a></li>';
    element = $compile(html)($scope);
    /*var html_link =   '<active-link></active-link>';
    element_link = $compile(html_link)($scope);*/
  }));

  describe('activeMenu', function () {
      it("Should remove active class", function() {
        //$translate.use('en');
        $scope.language = 'en';
        $scope.$digest();
        expect(element.hasClass('active')).toBe(false);
      });
      xit("Should add active class", function() {
        $translate.use('en');
        $scope.language = 'en';
        element.find('a').click();
        $scope.$digest();
        expect(element.hasClass('active')).toBe(true);
      });
  });
});
