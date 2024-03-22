/* globals _, bowser */
'use strict';

angular.module('metadatamanagementApp').service('OutdatedVersionNotifier', ['SimpleMessageToastService', '$document', '$mdToast', 'Principal', '$location', 
  function(SimpleMessageToastService, $document, $mdToast, Principal,
    $location) {

    var showPublicUserMessage = function(oldVersion, newVersion) {
      var messageParams = {
        oldVersion: oldVersion,
        newVersion: newVersion
      };
      SimpleMessageToastService.openAlertMessageToast('data-acquisition' +
        '-project-management.outdated-version-alert', messageParams);
    };

    var showLoggedInUserMessage = function(id, version, hidden) {
      var messageParams = {
        id: id,
        version: version,
        hidden: hidden,
        'show-master-link': true
      };
      SimpleMessageToastService.openAlertMessageToast('data-acquisition' +
        '-project-management.not-master-alert', messageParams);
    };

    var showOrderViewUserMessage = function(id, version, hidden) {
      var messageParams = {
        id: id,
        version: version,
        hidden: hidden,
        'show-master-link': false
      };
      SimpleMessageToastService.openAlertMessageToast('data-acquisition' +
        '-project-management.no-order-allowed-alert', messageParams);
    };

    var checkVersionAndNotify = function(item, fetchFn) {
      var versionFromUrl = $location.search().version;
      if (Principal.loginName() && item.shadow) {
        var version = _.get(item, 'release.version');        
        // prevent notification when in order view (data will always be an older version than the current, unreleased version)
        // but allow notifications for hidden datasets
        if (Principal.isProviderActive()) {
          showLoggedInUserMessage(item.masterId, version, item.hidden);
        } else {
          if (item.hidden) {
            showOrderViewUserMessage(item.masterId, version, item.hidden);
          } else {
            // Close hidden dataset notification if it is still visible.
            // As notifications do not close by themselves this ensures 
            // the notification disappears when the user chooses a
            // version in the sidenav that is available for ordering without reloading the page.
            if ($document.find('[data-translate="data-acquisition' +
              '-project-management.no-order-allowed-alert"]').length > 0) {
              $mdToast.hide();
            }
          }          
        }
      } else if (item.shadow && angular.isDefined(item.successorId)) {
        fetchFn().promise.then(function(result) {
          var oldVersion = _.get(item, 'release.version');
          var newVersion = _.get(result, 'release.version');
          showPublicUserMessage(oldVersion, newVersion);
        });
      } else if (item.shadow && versionFromUrl && bowser.compareVersions(
        [versionFromUrl, item.release.version]) !== 0) {
        SimpleMessageToastService.openAlertMessageToast('data-acquisition' +
            '-project-management.version-not-found-alert',
          {
            oldVersion: versionFromUrl,
            newVersion: item.release.version
          });
      } else {
        if ($document.find('[data-translate="data-acquisition' +
          '-project-management.outdated-version-alert"]').length > 0 ||
          $document.find('[data-translate="data-acquisition' +
            '-project-management.not-master-alert"]').length > 0) {
          $mdToast.hide();
        }
      }
    };

    return {
      checkVersionAndNotify: checkVersionAndNotify
    };
  }]);

