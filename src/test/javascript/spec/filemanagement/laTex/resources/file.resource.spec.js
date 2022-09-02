/* global describe, it, beforeEach, inject, mockApiAccountCall, mockI18nCalls */
/* @author Daniel Katzberg */
'use strict';

describe('Test File Resource: ', function() {
  var fileResource;
  var $httpBackend;

  var data = 'Latex Example';

  beforeEach(mockApis);
  beforeEach(mockSso);
  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
    fileResource = $injector.get('FileResource');
  }));

  it('Download Method', function() {
    var headers = {
      'content-type': 'application/x-tex'
    };
    $httpBackend.expectGET('/public/files/FileName').respond(
      data, headers);
    var result = fileResource.download('FileName');
    $httpBackend.flush();
  });

});
