/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */
/* global mockApis */

'use strict';

describe('DataAcquisitionProject', function() {
	var mockDataSetResource;
	var $httpBackend;
	var data = {
		id: 1,
		name: 'DataAcquisitionProjectResource'
	};
	beforeEach(mockSso);
	beforeEach(mockApis);
	beforeEach(inject(function($injector) {
		$httpBackend = $injector.get('$httpBackend');
		mockDataSetResource = $injector.get('DataAcquisitionProjectResource');
		$httpBackend.expectGET(
		  /api\/data-acquisition-projects/)
		  .respond(data);
	  }));
	it('should return DataAcquisitionProject resource ', function() {
		var resource =  mockDataSetResource.get(1);
		$httpBackend.flush();
		expect(resource.name).toEqual('DataAcquisitionProjectResource');
	});
});
