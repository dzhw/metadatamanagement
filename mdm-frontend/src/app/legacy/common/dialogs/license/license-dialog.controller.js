'use strict';

angular.module('metadatamanagementApp')
  .controller('LicenseDialogController', [
  '$scope',
  '$rootScope',
  'content',
  'titleTranslateKey',
  '$translate',
  '$mdDialog',
    function($scope, $rootScope, content, titleTranslateKey, $translate, $mdDialog) {
        $scope.content = content;
        $scope.titleTranslateKey = titleTranslateKey

        /**
         * Method for closing the dialog
         */
        $scope.closeDialog = function() {
            $mdDialog.cancel();
        };

    }]);

