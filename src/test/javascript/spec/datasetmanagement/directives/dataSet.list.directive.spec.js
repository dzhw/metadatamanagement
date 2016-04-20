/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */

'use strict';

describe('Unit testing datasetlist', function() {
  var $compile;
  var $rootScope;
  var $scope;
  var element;
  beforeEach(inject(function(_$compile_, _$rootScope_, $httpBackend) {
    $httpBackend.
    whenGET('scripts/app/entities/dataSet/dataSet.list.html.tmpl')
    .respond({});
    $compile = _$compile_;
    $rootScope = _$rootScope_;
    $scope = _$rootScope_.$new();
    var html = '<datasetlist params="{dataAcquisitionProjectId: ' +
    'dataAcquisitionProject.id}"' +
    'current-language="de"></datasetlist>';
    element = $compile(html)($scope);
  }));
  it('Should display datasetlist directive', function() {
    expect(element).toBeDefined();
  });
});
