/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */
/* global spyOn */

'use strict';

describe('VariableListController', function() {
  var $scope;
  var $rootScope;
  var VariableCollection;
  var createController;
  var params;
  var result;

  beforeEach(inject(function($injector) {
    $rootScope = $injector.get('$rootScope');
    $scope = $rootScope.$new();
    result = {
      page: {
        totalElements: 10
      }
    };
    params = {
      dataAcquisitionProjectId: 'rdcId'
    };
    $scope.params = params;
    $scope.init = function() {

    };
    VariableCollection = {
      query: function(param, callback) {
        callback(result);
      }
    };
    var locals = {
      '$scope': $scope,
      'VariableCollection': VariableCollection
    };
    createController = function() {
      $injector.get('$controller')('VariableListController',
        locals);
    };
    spyOn(VariableCollection, 'query').and.callThrough();
  }));

  it('should set $scope.pageState.totalElements', function() {
    createController();
    expect($scope.pageState.totalElements).toEqual(10);
  });
  it('should VariableCollection.query after broadcast', function() {
    createController();
    $rootScope.$broadcast('variables-uploaded');
    expect(VariableCollection.query).toHaveBeenCalled();
  });
});
