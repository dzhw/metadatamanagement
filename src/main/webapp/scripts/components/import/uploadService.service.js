/*jshint loopfunc: true */
/* global saveAs */
'use strict';
angular.module('metadatamanagementApp').service('UploadService',
function($q, $translate, DataSetsParser, DataSetDeleteResource,
  SurveysParser, SurveyDeleteResource, AtomicQuestionsParser,
  AtomicQuestionDeleteResource, VariablesParser, VariableDeleteResource,
  FileResource, ExcelReader, CustomModal, Upload, ZipReader) {
  var objects = [];
  var uploadState = {};
  var initUploadState = function() {
    uploadState.errors = 0;
    uploadState.successes = 0;
    uploadState.hasFinished = false;
    uploadState.itemsToUpload = 0;
    uploadState.progress = 0;
    uploadState.uploadedDomainObject = '';
    uploadState.disableButton = false;
    uploadState.logMessages = [{
            message: $translate.instant('metadatamanagementApp.' +
                'dataAcquisitionProject.detail.logMessages.intro') +
              '\n'
          }];
    uploadState.getResult = function() {
      if (uploadState.errors > 0) {
        return 'danger';
      }
      return 'success';
    };
    uploadState.getProgressState = function() {
      return uploadState.progress + '/' + uploadState.itemsToUpload;
    };
    uploadState.pushError = function(error) {
       //add an empty line
       uploadState.logMessages.push({
         message: '\n'
       });

       // if the error is already a string simply display it
       if (typeof error === 'string' || error instanceof String) {
         uploadState.logMessages.push({
           message: error + '\n',
           type: 'error'
         });
       }

       // log the dataset id
       if (error.config && error.config.data && error.config.data.id) {
         uploadState.logMessages.push({
           message: $translate.instant('metadatamanagementApp' +
             '.dataAcquisitionProject.detail.' +
             'logMessages.dataSetNotSaved', {
               id: error.config.data.id
             }) + '\n',
           type: 'error'
         });
       }

       //create additional information for the unsaved dataset
       if (error.data && error.data.errors) {
         error.data.errors.forEach(function(error) {
           uploadState.logMessages.push({
             message: error.message + '\n',
             type: 'error'
           });
         });
       } else if (error.data && error.data.status === 500) {
         uploadState.logMessages.push({
           message: $translate.instant('metadatamanagementApp' +
             '.dataAcquisitionProject.detail.logMessages.' +
             'internalServerError') + '\n',
           type: 'error'
         });
       } else if (error.data && error.data.message) {
         uploadState.logMessages.push({
           message: error.data.message +
             '\n',
           type: 'error'
         });
       }
     };

  };
  var showModal = function(dataAcquisitionProjectId, objName) {
    var defer = $q.defer();
    CustomModal.getModal($translate.instant(
        'metadatamanagementApp.dataAcquisitionProject.detail.' +
        'deleteMessages.delete' + objName, {
          id: dataAcquisitionProjectId
        })).then(function(returnValue) {
          initUploadState();
          defer.resolve(returnValue);
        });
    return defer.promise;
  };
  var uploadObjects = function() {
    uploadState.disableButton = true;
    for (var i = 0; i < objects.length; i++) {

      if (!objects[i].id || objects[i].id === '') {
        uploadState.pushError($translate.instant(
          'metadatamanagementApp.dataAcquisitionProject.' +
          'detail.logMessages.' +
          'missingId', {
            index: i + 1
          }));
      } else {
        objects[i].$save().then(function() {
        uploadState.progress++;
        uploadState.successes++;
        if (uploadState.progress >= uploadState.itemsToUpload) {
          uploadState.hasFinished = true;
          uploadState.disableButton = false;
        }
      }).catch(function(error) {
        uploadState.progress++;
        uploadState.errors++;
        uploadState.pushError(error);
        if (uploadState.progress >= uploadState.itemsToUpload) {
          uploadState.hasFinished = true;
          uploadState.disableButton = false;
        }
      });
      }
    }
  };
  var uploadAtomicQuestions = function(file, dataAcquisitionProjectId) {
    if (file !== null) {
      showModal(dataAcquisitionProjectId, 'AtomicQuestions').then(
        function(returnValue) {
        if (returnValue) {
          ExcelReader.readFileAsync(file).then(function(data) {
            objects  = AtomicQuestionsParser.getAtomicQuestions(data,
              dataAcquisitionProjectId);
            uploadState.itemsToUpload = objects.length;
            uploadState.uploadedDomainObject = 'atomicQuestions-uploaded';
            AtomicQuestionDeleteResource.deleteByDataAcquisitionProjectId({
                dataAcquisitionProjectId: dataAcquisitionProjectId},
                uploadObjects, function(error) {
                  uploadState.pushError(error);
                  uploadState.disableButton = false;
                });
          });
        }
      });
    }else {
      uploadState.pushError({});
    }
  };
  var uploadDataSets = function(file, dataAcquisitionProjectId) {
    if (file !== null) {
      showModal(dataAcquisitionProjectId, 'DataSets').then(
        function(returnValue) {
        if (returnValue) {
          ExcelReader.readFileAsync(file).then(function(data) {
            objects  = DataSetsParser.getDatasets(data,
              dataAcquisitionProjectId);
            uploadState.itemsToUpload = objects.length;
            uploadState.uploadedDomainObject = 'dataSets-uploaded';
            DataSetDeleteResource.deleteByDataAcquisitionProjectId({
                dataAcquisitionProjectId: dataAcquisitionProjectId},
                uploadObjects, function(error) {
                  uploadState.pushError(error);
                  uploadState.disableButton = false;
                });
          });
        }
      });
    }else {
      uploadState.pushError({});
    }
  };
  var uploadSurveys = function(file, dataAcquisitionProjectId) {
    if (file !== null) {
      showModal(dataAcquisitionProjectId, 'Surveys').then(
        function(returnValue) {
        if (returnValue) {
          ExcelReader.readFileAsync(file).then(function(data) {
            objects  = SurveysParser.getSurveys(data,
              dataAcquisitionProjectId);
            uploadState.itemsToUpload = objects.length;
            uploadState.uploadedDomainObject = 'surveys-uploaded';
            SurveyDeleteResource.deleteByDataAcquisitionProjectId({
                dataAcquisitionProjectId: dataAcquisitionProjectId},
                uploadObjects, function(error) {
                  uploadState.pushError(error);
                  uploadState.disableButton = false;
                });
          });
        }
      });
    }else {
      uploadState.pushError({});
    }
  };
  var uploadVariables = function(file, dataAcquisitionProjectId) {
    if (file !== null) {
      showModal(dataAcquisitionProjectId, 'Variables').then(
        function(returnValue) {
        if (returnValue) {
          ZipReader.readZipFileAsync(file)
             .then(function(files) {
               objects = VariablesParser.getVariables(files,
                 dataAcquisitionProjectId);
               uploadState.itemsToUpload = objects.length;
               uploadState.uploadedDomainObject = 'variables-uploaded';
               VariableDeleteResource.deleteByDataAcquisitionProjectId({
                     dataAcquisitionProjectId: dataAcquisitionProjectId},
                     uploadObjects, function(error) {
                       uploadState.pushError(error);
                       uploadState.disableButton = false;
                     });
             });
        }
      });
    }else {
      uploadState.pushError({});
    }
  };
  var uploadTexTemplate = function(file, dataAcquisitionProjectId) {
    if (file !== null) {
      //Upload Tex-File with freemarker commands
      initUploadState();
      uploadState.itemsToUpload = 1;
      uploadState.disableButton = true;
      Upload.upload({
          url: 'api/data-sets/report',
          fields: {
            'id': dataAcquisitionProjectId
          },
          file: file
            //Upload and document could filled with data successfully
        }).success(function(gridFsFileName) {
          //Download automaticly data filled tex template
          FileResource.download(gridFsFileName).then(function(response) {
            saveAs(response.data.blob, file.name);
            uploadState.progress++;
            uploadState.successes++;
            uploadState.disableButton = false;
          }).catch(function(error) {
            uploadState.progress++;
            uploadState.errors++;
            uploadState.pushError(error);
            uploadState.disableButton = false;
          });
          //Server hat issues with the tex file, send error to error output
        }).error(function(error) {
          uploadState.progress++;
          uploadState.errors++;
          var endErrorIndex = error.message.indexOf('----');
          var messageShort = error.message.substr(0, endErrorIndex).trim();
          uploadState.pushError(messageShort);
          uploadState.disableButton = false;
        });
    }else {
      uploadState.pushError({});
    }

  };
  initUploadState();
  return {
    uploadTexTemplate: uploadTexTemplate,
    uploadVariables: uploadVariables,
    uploadSurveys: uploadSurveys,
    uploadDataSets: uploadDataSets,
    uploadAtomicQuestions: uploadAtomicQuestions,
    getUploadState: uploadState
  };
});
