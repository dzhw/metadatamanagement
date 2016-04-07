/* global xdescribe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */
/* global spyOn */
/* global xit */
/* global  mockApis*/
'use strict';

xdescribe('UploadService', function() {
  var UploadService;
  var CustomModal;
  var file;
  var $q;
  var $translate;
  var $rootScope;
  var ExcelReader;
  var ZipReader;
  var $httpBackend;
  var AtomicQuestionDeleteResource;
  var DataSetDeleteResource;
  var SurveyDeleteResource;
  var VariableDeleteResource;

  beforeEach(module(function($provide) {
    $provide.value('CustomModal', {
      getModal: function() {
        return {
          then: function(callback) {
            callback(true);
          }
        };
      }
    });
    $provide.value('ExcelReader', {
      readFileAsync: function() {
        return {
          then: function(callback) {
            callback([{id: 'testId'}]);
          }
        };
      }
    });
    $provide.value('ZipReader', {
      readZipFileAsync: function() {
        return {
          then: function(callback) {
            callback([{id: 'testId'}]);
          }
        };
      }
    });
    $provide.value('AtomicQuestionDeleteResource', {
      deleteByDataAcquisitionProjectId: function(project, error) {
        error({});
      }
    });
    $provide.value('SurveyDeleteResource', {
      deleteByDataAcquisitionProjectId: function(project, error) {
        error({});
      }
    });
    $provide.value('VariableDeleteResource', {
      deleteByDataAcquisitionProjectId: function(project, error) {
        error({});
      }
    });
    $provide.value('DataSetDeleteResource', {
      deleteByDataAcquisitionProjectId: function(project, error) {
        error({});
      }
    });
  }));
  beforeEach(inject(function($injector) {
    file = {};
    UploadService = $injector.get('UploadService');
    CustomModal = $injector.get('CustomModal');
    ExcelReader = $injector.get('ExcelReader');
    ZipReader = $injector.get('ZipReader');
    $translate = $injector.get('$translate');
    $rootScope = $injector.get('$rootScope');
    AtomicQuestionDeleteResource = $injector.
    get('AtomicQuestionDeleteResource');
    DataSetDeleteResource = $injector.get('DataSetDeleteResource');
    SurveyDeleteResource = $injector.get('SurveyDeleteResource');
    VariableDeleteResource = $injector.get('VariableDeleteResource');
    $q = $injector.get('$q');
    $httpBackend = $injector.get('$httpBackend');

    mockApis();
    $httpBackend.whenGET('scripts/components/modal/customModal.html.tmpl').
    respond({});
    $httpBackend.expectGET(/api\/account\?cacheBuster=\d+/).respond(
      200, '');
    $httpBackend.whenPUT(/api\/atomic_questions\/testId\?cacheBuster=\d+/)
    .respond(200,{});
    $httpBackend.whenPUT(/api\/data_sets\/testId\?cacheBuster=\d+/).respond(
      200,{});
    $httpBackend.whenPUT(/api\/variables\/testId\?cacheBuster=\d+/).respond(
      200,{});
    $httpBackend.whenPUT(/api\/surveys\/testId\?cacheBuster=\d+/).respond(
      200,{});
    spyOn($translate,'instant').and.callFake(function() {
      return 'Translated Messages';
    });
    spyOn(ExcelReader,'readFileAsync')
    .and.callThrough();
    spyOn(ZipReader,'readZipFileAsync')
    .and.callThrough();
    spyOn(AtomicQuestionDeleteResource,'deleteByDataAcquisitionProjectId')
    .and.callThrough();
    spyOn(SurveyDeleteResource,'deleteByDataAcquisitionProjectId')
    .and.callThrough();
    spyOn(DataSetDeleteResource,'deleteByDataAcquisitionProjectId')
    .and.callThrough();
    spyOn(VariableDeleteResource,'deleteByDataAcquisitionProjectId')
    .and.callThrough();
  }));
  xit('should call SurveyDeleteResource', function() {
    UploadService.uploadSurveys(file,'RDC');
    $rootScope.$digest();
    $httpBackend.flush();
    expect(SurveyDeleteResource.deleteByDataAcquisitionProjectId)
    .toHaveBeenCalled();
  });
  xit('should call DataSetDeleteResource', function() {
    UploadService.uploadDataSets(file,'RDC');
    $rootScope.$digest();
    $httpBackend.flush();
    expect(DataSetDeleteResource.deleteByDataAcquisitionProjectId)
    .toHaveBeenCalled();
  });
  it('should call AtomicQuestionDeleteResource', function() {
    UploadService.uploadAtomicQuestions(file,'RDC');
    $rootScope.$digest();
    $httpBackend.flush();
    expect(AtomicQuestionDeleteResource.deleteByDataAcquisitionProjectId)
    .toHaveBeenCalled();
  });
  xit('should call VariableDeleteResource', function() {
    UploadService.uploadVariables(file,'RDC');
    $rootScope.$digest();
    $httpBackend.flush();
    expect(VariableDeleteResource.deleteByDataAcquisitionProjectId)
    .toHaveBeenCalled();
  });
  xit('should', function() {
    UploadService.uploadTexTemplate();
  });
});
