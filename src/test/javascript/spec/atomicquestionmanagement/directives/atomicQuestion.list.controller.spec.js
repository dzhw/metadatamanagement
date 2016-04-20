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
  var AtomicQuestionCollection;
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
    AtomicQuestionCollection = {
      query: function(param, callback) {
        callback(result);
      }
    };
    var locals = {
      '$scope': $scope,
      'AtomicQuestionCollection': AtomicQuestionCollection
    };
    createController = function() {
      $injector.get('$controller')('AtomicQuestionListController',
        locals);
    };
    spyOn(AtomicQuestionCollection, 'query').and.callThrough();
  }));

  it('should set $scope.pageState.totalElements', function() {
    createController();
    expect($scope.pageState.totalElements).toEqual(10);
  });
  it('should AtomicQuestionCollection.query after broadcast', function() {
    createController();
    $rootScope.$broadcast('atomicQuestions-uploaded');
    expect(AtomicQuestionCollection.query).toHaveBeenCalled();
  });
});
