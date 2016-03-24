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
  var uploadState;
  var $rootScope;

  beforeEach(mockApis);
  beforeEach(function() {
    inject(function($controller, _$rootScope_) {
      $scope = _$rootScope_.$new();
      $rootScope = _$rootScope_;
      uploadState = {
        errors: 0,
        successes: 0,
        hasFinished: false,
        itemsToUpload: 0,
        progress: 0,
        uploadedDomainObject: '',
        disableButton: false,
      };
      UploadService = {
        uploadAtomicQuestions: function() {
          uploadState.uploadedDomainObject = 'atomicQuestions-uploaded';
          uploadState.progress = 0;
          uploadState.hasFinished = true;
        },
        uploadSurveys: function() {
          uploadState.uploadedDomainObject = 'surveys-uploaded';
          uploadState.progress = 0;
          uploadState.hasFinished = true;
        },
        uploadDataSets: function() {
          uploadState.uploadedDomainObject = 'dataSets-uploaded';
          uploadState.progress = 0;
          uploadState.hasFinished = true;
        },
        uploadTexTemplate: function() {
        },
        uploadVariables: function() {
          uploadState.uploadedDomainObject = 'variables-uploaded';
          uploadState.progress = 0;
          uploadState.hasFinished = true;
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
      spyOn($scope, '$broadcast').and.callThrough();
    });
  });
  beforeEach(function() {
      createController();
      $scope.uploadState = uploadState;
    });
  it('should set uploadedDomainObject to dataSets-uploaded',function() {
      $scope.uploadDataSets();
      $scope.$apply();
      expect($scope.uploadState.uploadedDomainObject).
      toEqual('dataSets-uploaded');
    });
  it('should set uploadedDomainObject to surveys-uploaded',function() {
      $scope.uploadSurveys();
      $scope.$apply();
      expect($scope.uploadState.uploadedDomainObject).
      toEqual('surveys-uploaded');
    });
  it('',function() {
      $scope.uploadTexTemplate();
    });
  it('should set uploadedDomainObject to variables-uploaded',function() {
      $scope.uploadVariables();
      $scope.$apply();
      expect($scope.uploadState.uploadedDomainObject).
      toEqual('variables-uploaded');
    });
  it('should set uploadedDomainObject to atomicQuestions-uploaded',function() {
      $scope.uploadAtomicQuestions();
      $scope.$apply();
      expect($scope.uploadState.uploadedDomainObject).
      toEqual('atomicQuestions-uploaded');
    });
  it('should broadcast event after loading',function() {
    $scope.uploadAtomicQuestions();
    uploadState.progress = 1;
    $scope.uploadState = uploadState;
    $scope.$apply();
    expect($scope.$broadcast).toHaveBeenCalledWith('atomicQuestions-uploaded');
  });
  it('should not broadcast event after loading',function() {
    $scope.uploadAtomicQuestions();
    uploadState.hasFinished = false;
    uploadState.progress = 2;
    $scope.uploadState = uploadState;
    $scope.$apply();
    expect($scope.$broadcast).not.toHaveBeenCalledWith('atomicQuestions-uploaded');
  });
});
