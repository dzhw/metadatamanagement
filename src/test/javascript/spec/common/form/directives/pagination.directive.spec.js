describe('Unit testing metadatamanagementAppPagination', function() {
  var $compile,
    $rootScope,
    $scope,
    $httpBackend,
    element;
  beforeEach(mockApis);
  beforeEach(inject(function(_$compile_, _$rootScope_, _$httpBackend_) {
    $compile = _$compile_;
    $scope = _$rootScope_.$new();
    $rootScope = _$rootScope_;
    $httpBackend = _$httpBackend_;
    var html =
      '<metadatamanagement-app-pagination></metadatamanagement-app-pagination>';
    $httpBackend.expectGET(
      'scripts/common/form/directives/pagination.html.tmpl').respond(200, {});
    element = $compile(html)($scope);
    $scope.$digest();
  }));

  describe('metadatamanagementAppPagination', function() {
    it("Should display metadatamanagementAppPagination", function() {
      expect(element).toBeDefined();
    });
  });
});
