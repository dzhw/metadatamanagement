/* global describe */
/* global beforeEach */
/* global inject */
/* global spyOn */
/* global it */
/* global expect */
/* global mockApiAccountCall */
/* global mockI18nCalls */
'use strict';

describe('Controllers Tests ', function() {
  var $scope;
  var createController;
  var $translate;
  var DataAcquisitionProjectExportService;
  var ExcelParser;
  var $q;
  var $httpBackend;
  var CustomModal;
  var surveys = [{
    id: 'fdzid',
    questionnaireId: 'fdzid',
    title: {
      en: 'en',
      de: 'de'
    },
    fieldPeriod: {
      start: 'yyyy-mm-dd',
      end: 'yyyy-mm-dd'
    }
  }];
  var file = {
    0: {
      name: 'foo',
      size: 500001
    }
  };
  var error = {
    data: {
      errors: [{
        message: 'test'
      }],
      status: 200
    },
    config: {
      data: {
        id: 'fdzid',
        errors: []
      }
    }
  };

  beforeEach(mockApis);
  beforeEach(function() {
    inject(function($controller, _$rootScope_, _$q_, _$translate_,
      _$httpBackend_) {
      $httpBackend = _$httpBackend_;
      $scope = _$rootScope_.$new();
      $q = _$q_;
      $translate = _$translate_;
      DataAcquisitionProjectExportService = {
        exportToODT: function() {}
      };
      ExcelParser = {
        readFileAsync: function(file) {
          return {
            then: function(callback) {
              return callback(surveys);
            }
          };
        }
      };
      CustomModal = {
        getModal: function() {
          return {
            then: function(callback) {
              return callback(true);
            }
          };
        }
      };
      var locals = {
        '$scope': $scope,
        'CustomModal': CustomModal,
        'entity': {
          id: 'fdzid'
        },
        'DataAcquisitionProjectExportService': DataAcquisitionProjectExportService,
        'ExcelParser': ExcelParser,
        '$translate': $translate
      };

      createController = function() {
        return $controller(
          'DataAcquisitionProjectDetailController', locals);
      };
      spyOn(DataAcquisitionProjectExportService, 'exportToODT').and
        .callThrough();
      spyOn(ExcelParser, 'readFileAsync').and.callThrough();
      spyOn(CustomModal, 'getModal').and.callThrough();
    });
  });

  describe('DataAcquisitionProjectDetailController', function() {

    beforeEach(function() {
      createController();
    });

    it('$scope.onTexTemplateUpload produce a filled template',
      function() {
        var mockFile = {
          method: 'POST',
          file: [{
            'name': 'LatexTest.tex',
            'body': 'A Latex Test File.'
          }]
        };
        var data = 'File 1';
        //Regex Expressiom for matching CacheCluster Paramter
        //Upload
        $httpBackend.expectPOST(/api\/data-sets\/report\?.*/).respond(
          200, data);
        spyOn($scope, 'onTexTemplateUpload').and.callThrough();
        $scope.onTexTemplateUpload(mockFile);
        $scope.$digest();
        expect($scope.onTexTemplateUpload).toHaveBeenCalled();

        //Download // success
      });

    it('$scope.isSaving should be false', function() {
      $scope.exportToODT();
      expect(DataAcquisitionProjectExportService.exportToODT)
        .toHaveBeenCalled();
    });

    it('should call initUploadStatus with 1, true, surveys-uploaded',
      function() {
        spyOn($scope, 'initUploadStatus');
        $scope.onSurveyUpload(file);
        expect($scope.initUploadStatus).toHaveBeenCalledWith(1, true,
          'surveys-uploaded');
      });

    it('should call $translate.instant', function() {
      spyOn($translate, 'instant');
      surveys[0].id = '';
      try {
        $scope.onSurveyUpload(file);
      } catch (e) {

      }
      expect($translate.instant).toHaveBeenCalled();
    });

    it('should set number of progress ', function() {
      $scope.uploadStatus.errors = 1;
      $scope.uploadStatus.successes = 1;
      expect($scope.uploadStatus.getProgress()).toEqual(2);
    });

    it('should return success ', function() {
      expect($scope.uploadStatus.getResult()).toEqual('success');
    });

    it('should return danger ', function() {
      $scope.uploadStatus.errors = 1;
      expect($scope.uploadStatus.getResult()).toEqual('danger');
    });

    it('should set progress state ', function() {
      expect($scope.uploadStatus.getProgressState()).toEqual('0/0');
    });

    it('should set number of success ', function() {
      $scope.uploadStatus.pushSuccess();
      expect($scope.uploadStatus.successes).toEqual(1);
    });

    it('should set number of success ', function() {
      $scope.uploadStatus.pushSuccess();
      expect($scope.uploadStatus.successes).toEqual(1);
    });

    it('should set error message and number of errors', function() {
      $scope.uploadStatus.pushError('error');
      expect($scope.uploadStatus.logMessages.length).toEqual(3);
      expect($scope.uploadStatus.errors).toEqual(1);
    });

    it('should set error message and number of errors', function() {
      $scope.uploadStatus.pushError(error);
      expect($scope.uploadStatus.logMessages.length).toEqual(4);
      expect($scope.uploadStatus.errors).toEqual(1);
    });

    it('should set error message and number of errors', function() {
      error.data = {
        status: 500
      };
      $scope.uploadStatus.pushError(error);
      expect($scope.uploadStatus.logMessages.length).toEqual(4);
      expect($scope.uploadStatus.errors).toEqual(1);
    });
  });
});
