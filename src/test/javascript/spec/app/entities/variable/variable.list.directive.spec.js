/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */

'use strict';

describe('Unit testing variablelist', function() {
  var $compile;
  var $rootScope;
  var $scope;
  var element;
  beforeEach(inject(function(_$compile_, _$rootScope_, $httpBackend) {
    $httpBackend.
    whenGET('scripts/app/entities/variable/variable.list.html.tmpl')
    .respond({});
    $compile = _$compile_;
    $rootScope = _$rootScope_;
    $scope = _$rootScope_.$new();
    var html = '<variablelist params="{dataAcquisitionProjectId: ' +
    'dataAcquisitionProject.id}"' +
    'current-language="de"></variablelist>';
    element = $compile(html)($scope);
  }));
  it('Should display variablelist directive', function() {
    expect(element).toBeDefined();
  });
});
