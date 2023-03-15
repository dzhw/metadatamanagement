/* globals _, bowser */
'use strict';

angular.module('metadatamanagementApp').service('OutdatedVersionNotifier',
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

    var checkVersionAndNotify = function(item, fetchFn) {
      var versionFromUrl = $location.search().version;
      if (Principal.loginName() && item.shadow) {
        var version = _.get(item, 'release.version');
        // var href = createMasterRef(item);
        // prevent notification when in order view (data will always be older version)
        if (Principal.isProviderActive()) {
          showLoggedInUserMessage(item.masterId, version, item.hidden);
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
  });
