/*global it:false */
/*global beforeEach:false */
/*global expect:false */
/*global spyOn:false */
/*global jasmine:false */
/*global inject:false */
/*global afterEach:false */
/*global describe:false */

/* @author: Daniel Katzberg */
'use strict';

describe('Test User Factory', function() {
  var $httpBackend;
  var user;

  //Before each Test
  beforeEach(module('metadatamanagementApp'));
  beforeEach(inject(function($injector, User) {
    $httpBackend = $injector.get('$httpBackend');
    user = User;
  }));

  //After each Test
  afterEach(function() {});

  //TESTS
  it('getAllUser', function() {
    $httpBackend.expectGET('/api/users/').respond(200, 'success');
  });

  it('getUser', function() {
    $httpBackend.expectGET('/api/users/admin', {transformRequest: angular.test}).respond(200, 'success');
  });

});
