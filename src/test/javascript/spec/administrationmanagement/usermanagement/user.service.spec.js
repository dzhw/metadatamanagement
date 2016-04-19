describe('User', function() {
  var mockUserResource, $httpBackend;
  var data = {
    login: 'test',
    name: 'test'
  };
  beforeEach(mockApis);
  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
    mockUserResource = $injector.get('User');
  }));
  describe('getUser', function() {
    it('should get User', inject(function(Survey) {
      $httpBackend.expectGET(
        /api\/users\?cacheBuster=\d+/).respond(
        data);

      var result = mockUserResource.get(1);
      try {
        $httpBackend.flush();
      } catch (e) {

      }
      expect(result.name).toEqual('test');
    }));
  });
});
