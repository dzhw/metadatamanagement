/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */
/* global spyOn */

'use strict';

describe('AtomicQuestionListController', function() {
  var $scope;
  var $rootScope;
  var AtomicQuestionCollectionResource;
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
    AtomicQuestionCollectionResource = {
      query: function(param, callback) {
        callback(result);
      }
    };
    var locals = {
      '$scope': $scope,
      'AtomicQuestionCollectionResource': AtomicQuestionCollectionResource
    };
    createController = function() {
      $injector.get('$controller')('AtomicQuestionListController',
        locals);
    };
    spyOn(AtomicQuestionCollectionResource, 'query').and.callThrough();
  }));

  it('should set $scope.pageState.totalElements', function() {
    createController();
    expect($scope.pageState.totalElements).toEqual(10);
  });
  it('should AtomicQuestionCollectionResource.query after broadcast',
  function() {
    createController();
    $rootScope.$broadcast('atomicQuestions-uploaded');
    expect(AtomicQuestionCollectionResource.query).toHaveBeenCalled();
  });
});
