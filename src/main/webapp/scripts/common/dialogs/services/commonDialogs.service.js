/* global document */
'use strict';

angular.module('metadatamanagementApp').factory('CommonDialogsService',
  function($mdDialog, $translate) {
    var translationPrefix = 'global.common-dialogs.confirm-dirty.';
    var confirmOnDirtyDialog = $mdDialog.confirm({
      onComplete: function afterShowAnimation() {
        var $dialog = angular.element(document.querySelector('md-dialog'));
        var $actionsSection = $dialog.find('md-dialog-actions');
        var $cancelButton = $actionsSection.children()[0];
        var $confirmButton = $actionsSection.children()[1];
        angular.element($confirmButton).removeClass('md-focused');
        angular.element($cancelButton).addClass('md-focused');
        $cancelButton.focus();
      }
    }).title($translate.instant(translationPrefix + 'title'))
      .textContent($translate.instant(translationPrefix + 'content'))
      .ok($translate.instant('global.common-dialogs.yes'))
      .cancel($translate.instant('global.common-dialogs.no'));

    var showConfirmOnDirtyDialog = function() {
      return $mdDialog.show(confirmOnDirtyDialog);
    };

    return {
      showConfirmOnDirtyDialog: showConfirmOnDirtyDialog
    };
  });
