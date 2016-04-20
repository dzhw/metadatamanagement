/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */
/* global spyOn */

'use strict';

describe('SurveyListController', function() {
  var $scope;
  var $rootScope;
  var SurveyCollection;
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
    SurveyCollection = {
      query: function(param, callback) {
        callback(result);
      }
    };
    var locals = {
      '$scope': $scope,
      'SurveyCollection': SurveyCollection
    };
    createController = function() {
      $injector.get('$controller')('SurveyListController',
        locals);
    };
    spyOn(SurveyCollection, 'query').and.callThrough();
  }));

  it('should set $scope.pageState.totalElements', function() {
    createController();
    expect($scope.pageState.totalElements).toEqual(10);
  });
  it('should SurveyCollection.query after broadcast', function() {
    createController();
    $rootScope.$broadcast('surveys-uploaded');
    expect(SurveyCollection.query).toHaveBeenCalled();
  });
});
