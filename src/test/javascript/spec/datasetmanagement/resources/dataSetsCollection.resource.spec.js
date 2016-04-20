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
    name: 'DataSetsCollection'
  };
  beforeEach(mockApis);
  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
    mockDataSetsCollectionResource =
    $injector.get('DataSetsCollection');
    $httpBackend.expectGET(
      /api\/data-sets\?cacheBuster=\d+&projection=complete/)
      .respond(data);
  }));
  it('should return DataSetsCollection resource ', function() {
    var resource =  mockDataSetsCollectionResource.get(1);
    $httpBackend.flush();
    expect(resource.name).toEqual('DataSetsCollection');
  });
});
