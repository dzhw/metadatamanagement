/* global describe */
/* global beforeEach */
/* global inject */
/* global spyOn */
/* global it */
/* global expect */
/* global mockApis */

'use strict';

describe('Controllers Tests ', function() {
  var $scope;
  var createController;
  var UploadService;

  beforeEach(mockApis);
  beforeEach(function() {
    inject(function($controller, _$rootScope_) {
      $scope = _$rootScope_.$new();
      UploadService = {
        uploadAtomicQuestions: function() {

        },
        uploadSurveys: function() {

        },
        uploadDataSets: function() {

        },
        uploadTexTemplate: function() {

        },
        uploadVariables: function() {

        }
      };
      var locals = {
        '$scope': $scope,
        'UploadService': UploadService,
        'entity': {
          id: 'fdzid'
        }
      };

      createController = function() {
        return $controller(
          'DataAcquisitionProjectDetailController', locals);
      };
      spyOn(UploadService, 'uploadAtomicQuestions').and.callThrough();
      spyOn(UploadService, 'uploadSurveys').and.callThrough();
      spyOn(UploadService, 'uploadDataSets').and.callThrough();
      spyOn(UploadService, 'uploadTexTemplate').and.callThrough();
      spyOn(UploadService, 'uploadVariables').and.callThrough();
    });
  });
  beforeEach(function() {
      createController();
    });
  it('',function() {
      $scope.uploadDataSets();
    });
  it('',function() {
      $scope.uploadSurveys();
    });
  it('',function() {
      $scope.uploadTexTemplate();
    });
  it('',function() {
      $scope.uploadVariables();
    });
  it('',function() {
      $scope.uploadAtomicQuestions();
    });

});
