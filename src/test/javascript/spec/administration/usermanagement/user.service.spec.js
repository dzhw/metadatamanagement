/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */
/* global mockApis */

'use strict';

describe('User', function() {
  var mockUserResource;
  var $httpBackend;
  var data = {
    login: 'test',
    name: 'test'
  };
  beforeEach(mockApis);
  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
    mockUserResource = $injector.get('UserResource');
  }));
  describe('getUser', function() {
    it('should get User', function() {
      $httpBackend.expectGET(
        /api\/users/).respond(
        data);

      var result = mockUserResource.get(1);
      try {
        $httpBackend.flush();
      } catch (e) {

      }
      expect(result.name).toEqual('test');
    });
  });
});
