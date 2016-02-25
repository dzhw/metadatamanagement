'use strict';

describe('Controllers Tests ', function() {
    var $scope, createController, $translate,
        DataAcquisitionProjectExportService,
        ExcelParser, $q,
        surveys = [{
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
        }],
        file = {
          0: {
            name: 'foo',
            size: 500001
          }
        },
        error = {
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

    beforeEach(mockApiAccountCall);
    beforeEach(mockI18nCalls);
    beforeEach(function() {
        inject(function($controller, _$rootScope_, _$q_, _$translate_) {
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

            var locals = {
                '$scope': $scope,
                'entity': {
                    id: 'fdzid'
                },
                'DataAcquisitionProjectExportService': DataAcquisitionProjectExportService,
                'ExcelParser': ExcelParser,
                '$translate': $translate
            };
            createController = function() {
                return $controller('DataAcquisitionProjectDetailController', locals);
            };
            spyOn(DataAcquisitionProjectExportService, 'exportToODT').and.callThrough();
            spyOn(ExcelParser, 'readFileAsync').and.callThrough();
        });
    });
    describe('DataAcquisitionProjectDetailController', function() {
        beforeEach(function() {
            createController();
        });
        it('$scope.isSaving should be false', function() {
            $scope.exportToODT();
            expect(DataAcquisitionProjectExportService.exportToODT).toHaveBeenCalled();
        });
        it('should call initUploadStatus with 1', function() {
            spyOn($scope, 'initUploadStatus');
            $scope.onSurveyUpload(file);
            expect($scope.initUploadStatus).toHaveBeenCalledWith(1);
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
        it('should set number of progress ', function(){
          $scope.uploadStatus.errors = 1;
          $scope.uploadStatus.successes = 1;
          expect($scope.uploadStatus.getProgress()).toEqual(2);
        });
        it('should return success ', function(){
          expect($scope.uploadStatus.getResult()).toEqual('success');
        });
        it('should return danger ', function(){
          $scope.uploadStatus.errors = 1;
          expect($scope.uploadStatus.getResult()).toEqual('danger');
        });
        it('should set progress state ', function(){
          expect($scope.uploadStatus.getProgressState()).toEqual('0/0');
        });
        it('should set number of success ', function(){
          $scope.uploadStatus.pushSuccess();
          expect($scope.uploadStatus.successes).toEqual(1);
        });
        it('should set number of success ', function(){
          $scope.uploadStatus.pushSuccess();
          expect($scope.uploadStatus.successes).toEqual(1);
        });
        it('should set error message and number of errors', function(){
          $scope.uploadStatus.pushError('error');
          expect($scope.uploadStatus.logMessages.length).toEqual(3);
          expect($scope.uploadStatus.errors).toEqual(1);
        });
        it('should set error message and number of errors', function(){
          $scope.uploadStatus.pushError(error);
          expect($scope.uploadStatus.logMessages.length).toEqual(4);
          expect($scope.uploadStatus.errors).toEqual(1);
        });
        it('should set error message and number of errors', function(){
          error.data = {
              status: 500
          };
          $scope.uploadStatus.pushError(error);
          expect($scope.uploadStatus.logMessages.length).toEqual(4);
          expect($scope.uploadStatus.errors).toEqual(1);
        });
    });
});
