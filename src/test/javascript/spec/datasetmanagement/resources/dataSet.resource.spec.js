/* global describe */
/* global beforeEach */
/* global inject */
/* global it */
/* global expect */
/* global mockApis */

'use strict';

describe('DataSet', function() {
  var mockDataSetResource;
  var $httpBackend;
  var data = {
    id: 1,
    name: 'DataSetResource'
  };
  beforeEach(mockApis);
  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
    mockDataSetResource = $injector.get('DataSetResource');
    $httpBackend.expectGET(
      /api\/data-sets\?cacheBuster=\d+&projection=complete/)
      .respond(data);
  }));
  it('should return DataSet resource ', function() {
    var resource =  mockDataSetResource.get(1);
    $httpBackend.flush();
    expect(resource.name).toEqual('DataSetResource');
  });
});
