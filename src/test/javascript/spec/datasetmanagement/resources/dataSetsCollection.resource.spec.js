/* global describe */
/* global beforeEach */
/* global inject */
/* global it */
/* global expect */
/* global mockApis */

'use strict';

describe('DataSetsCollection', function() {
  var mockDataSetCollectionResource;
  var $httpBackend;
  var data = {
    id: 1,
    name: 'DataSetCollectionResource'
  };
  beforeEach(mockApis);
  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
    mockDataSetCollectionResource =
    $injector.get('DataSetCollectionResource');
    $httpBackend.expectGET(
      /api\/data-sets\?projection=complete/)
      .respond(data);
  }));
  it('should return DataSetCollectionResource ', function() {
    var resource =  mockDataSetCollectionResource.get(1);
    $httpBackend.flush();
    expect(resource.name).toEqual('DataSetCollectionResource');
  });
});
