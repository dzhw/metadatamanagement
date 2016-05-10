/* global describe */
/* global beforeEach */
/* global inject */
/* global it */
/* global expect */
/* global mockApis */

'use strict';

describe('DataSetsCollection', function() {
  var mockDataSetsCollectionResource;
  var $httpBackend;
  var data = {
    id: 1,
    name: 'DataSetsCollectionResource'
  };
  beforeEach(mockApis);
  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
    mockDataSetsCollectionResource =
    $injector.get('DataSetsCollectionResource');
    $httpBackend.expectGET(
      /api\/data-sets\?cacheBuster=\d+&projection=complete/)
      .respond(data);
  }));
  it('should return DataSetsCollectionResource ', function() {
    var resource =  mockDataSetsCollectionResource.get(1);
    $httpBackend.flush();
    expect(resource.name).toEqual('DataSetsCollectionResource');
  });
});
