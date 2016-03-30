'use strict';

angular.module('metadatamanagementApp').service('UploadDataSet',
function(CustomModal, ExcelReader, DataSetsParser, DataSetDeleteResource,
  $translate, UploadEntities, JobLog) {
  var upload = function(file, dataAcquisitionProjectId) {
    if (file !== null) {
      CustomModal.getModal($translate.instant(
          'metadatamanagementApp.dataAcquisitionProject.detail.' +
          'deleteMessages.deleteDataSets', {
            id: dataAcquisitionProjectId
          })).then(function(returnValue) {
            if (returnValue) {
              ExcelReader.readFileAsync(file).then(function(data) {
                var objects  = DataSetsParser.getDatasets(data,
                  dataAcquisitionProjectId);
                JobLog.uploadState.itemsToUpload = objects.length;
                JobLog.uploadState.uploadedDomainObject = 'dataSets-uploaded';
                DataSetDeleteResource.deleteByDataAcquisitionProjectId({
                    dataAcquisitionProjectId: dataAcquisitionProjectId},
                    UploadEntities.upload(objects), function(error) {
                      JobLog.uploadState.pushError(error);
                      JobLog.uploadState.disableButton = false;
                    });
              });
            }
          });
    }else {
      JobLog.uploadState.pushError({});
    }
  };
  JobLog.initUploadState();
  return {
      upload: upload
    };
});
