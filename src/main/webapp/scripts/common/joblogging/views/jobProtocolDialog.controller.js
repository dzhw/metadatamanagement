/* Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').controller(
  'JobProtocolDialogController', function(
    $scope, $mdDialog, JobLoggingService, $translate, FileSaver, Blob) {
    $scope.job = JobLoggingService.getCurrentJob();
    $scope.closeDialog = function() {
      $mdDialog.hide();
    };

    /* Save Protocol */
    $scope.saveProtocol = function() {
      var protocol = '';

      //Add Headline Summary
      protocol += $translate
        .instant('global.joblogging.protocol-dialog.success') + ': ' +
        $scope.job.successes + '\n';
      protocol += $translate
        .instant('global.joblogging.protocol-dialog.warning') + ': ' +
        $scope.job.warnings + '\n';
      protocol += $translate
        .instant('global.joblogging.protocol-dialog.error') + ': ' +
        $scope.job.errors + '\n';
      protocol += '\n';
      protocol += $translate
        .instant('global.joblogging.protocol-dialog.title') + ': \n';

      //Add all Messages
      $scope.job.logMessages.forEach(function(logMessage) {
          //Write Type of Message before the Message itself
          switch (logMessage.type) {
            case 'error': protocol += '(' + $translate
              .instant('global.joblogging.type.error') + ') ';
              break;
            case 'warning': protocol += '(' + $translate
              .instant('global.joblogging.type.warning') + ') ';
              break;
            case 'info': protocol += '(' + $translate
              .instant('global.joblogging.type.info') +
              ') ';
              break;
          }
          protocol += $translate
            .instant(logMessage.message, logMessage.translationParams);
          protocol += '\n';
        });

      //Save Log File
      var data = new Blob([protocol], {type: 'text/plain;charset=utf-8'});
      FileSaver.saveAs(data, 'text.txt');
    };
  });
