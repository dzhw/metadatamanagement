/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('UploadCompleteToastService',
  function($mdToast) {
    var toastParent = angular.element('#toast-container');

    //The Toast for the upload complete
    function openUploadCompleteToast(resultMessage) {
      $mdToast.show({
        controller: 'UploadCompleteToastController',
        templateUrl: 'scripts/searchmanagement/' +
          'views/upload-complete-toast.html.tmpl',
        hideDelay: 0,
        position: 'top right',
        parent: toastParent,
        locals: {
          resultMessage: resultMessage
        }
      });
    }

    return {
      openUploadCompleteToast: openUploadCompleteToast
    };

  });
