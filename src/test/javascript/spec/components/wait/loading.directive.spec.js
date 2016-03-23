/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */

'use strict';

describe('Unit testing loading', function() {
  var $compile;
  var $rootScope;
  var $scope;
  var element;
  beforeEach(inject(function(_$compile_, _$rootScope_, $httpBackend) {
    $httpBackend.
    whenGET('scripts/ components/wait/loading.html.tmpl')
    .respond({});
    $compile = _$compile_;
    $rootScope = _$rootScope_;
    $scope = _$rootScope_.$new();
    var html = '<loading></loading>';
    element = $compile(html)($scope);
  }));
  it('Should display loading directive', function() {
    expect(element).toBeDefined();
  });
});
