'use strict';

angular.module('metadatamanagementApp').controller('HealthController', [
  '$scope',
  'MonitoringService',
  '$uibModal',
  'ElasticSearchAdminService',
  'PageMetadataService',
  '$state',
  'BreadcrumbService',
  'SimpleMessageToastService',
  'DaraReleaseCustomResource',
  function($scope, MonitoringService, $uibModal, ElasticSearchAdminService,
    PageMetadataService, $state, BreadcrumbService, SimpleMessageToastService, DaraReleaseCustomResource) {
    PageMetadataService.setPageTitle('administration.health.title');
    $scope.isRecreatingIndices = false;
    $scope.updatingHealth = true;
    $scope.isUpdatingDara = false;
    $scope.separator = '.';

    $scope.refresh = function() {
      $scope.updatingHealth = true;
      MonitoringService.checkHealth().then(function(response) {
        $scope.healthData = $scope.transformHealthData(response);
        $scope.updatingHealth = false;
      }, function(response) {
        $scope.healthData = $scope.transformHealthData(response.data);
        $scope.updatingHealth = false;
      });
    };

    $scope.recreateAllElasticsearchIndices = function() {
      $scope.isRecreatingIndices = true;
      ElasticSearchAdminService.recreateAllElasticsearchIndices()
        .then(function() {
          SimpleMessageToastService.openSimpleMessageToast(
            'administration.health.elasticsearch.reindex-success');
          $scope.isRecreatingIndices = false;
        }, function() {
          $scope.isRecreatingIndices = false;
        });
    };

    /**
     * Method that calls the updateDaraProjects-method from DaraReleaseResource.
     * In this way, all projects are updated in dara.
     * In particular projects with surveys that consist of qualitative data are marked.
     * Potential errors are listed in details-modal.
     */
    $scope.updateDaraProjects = function(health) {
      $scope.isUpdatingDara = true;
      DaraReleaseCustomResource.updateDaraProjects().then(function(errorsList) {
        if (errorsList.length > 0) {
          // errors occurred during update
          SimpleMessageToastService.openAlertMessageToasts(
            [{messageId: 'administration.health.dara.update-failure'}]);
          let errorsString = errorsList.length + " errors occured during DARA update:\n" +
            "======================================================================\n\n";
          for (const errMsg of errorsList) {
            errorsString += errMsg + "\n\n======================================================================\n\n";
          }
          // set health.error, so that errors are shown in Details-Modal
          health.error = errorsString;
        } else {
          // no errors occurred during update
          SimpleMessageToastService.openSimpleMessageToast(
            'administration.health.dara.update-success');
        }
        $scope.isUpdatingDara = false;
      });
    }

    $scope.refresh();

    $scope.getLabelClass = function(statusState) {
      if (statusState === 'UP') {
        return 'label-success';
      } else {
        return 'label-danger';
      }
    };

    $scope.transformHealthData = function(data) {
      var response = [];
      $scope.flattenHealthData(response, null, data);
      return response;
    };

    $scope.flattenHealthData = function(result, path, data) {
      angular.forEach(data.components, function(value, key) {
        if ($scope.isHealthObject(value)) {
          if ($scope.hasSubSystem(value)) {
            $scope.addHealthObject(result, false, value, $scope
              .getModuleName(path, key));
            $scope.flattenHealthData(result, $scope.getModuleName(path,
                key),
              value);
          } else {
            $scope.addHealthObject(result, true, value, $scope.getModuleName(
              path, key));
          }
        }
      });
      return result;
    };

    $scope.getModuleName = function(path, name) {
      var result;
      if (path && name) {
        result = path + $scope.separator + name;
      } else if (path) {
        result = path;
      } else if (name) {
        result = name;
      } else {
        result = '';
      }
      return result;
    };

    $scope.showHealth = function(health) {
      $uibModal.open({
        templateUrl: 'scripts/administration/health/' +
          'health.modal.html.tmpl',
        controller: 'HealthModalController',
        size: 'lg',
        resolve: {
          currentHealth: function() {
            return health;
          },
          baseName: function() {
            return $scope.baseName;
          },
          subSystemName: function() {
            return $scope.subSystemName;
          }

        }
      });
    };

    $scope.addHealthObject = function(result, isLeaf, healthObject, name) {

      var healthData = {
        'name': name
      };
      var details = {};
      var hasDetails = false;

      angular.forEach(healthObject, function(value, key) {
        if (key === 'status' || key === 'error') {
          healthData[key] = value;
        } else {
          if (!$scope.isHealthObject(value)) {
            details[key] = value;
            hasDetails = true;
          }
        }
      });

      // Add the of the details
      if (hasDetails) {
        angular.extend(healthData, {
          'details': details
        });
      }

      // Only add nodes if they provide additional information
      if (isLeaf || hasDetails || healthData.error) {
        result.push(healthData);
      }
      return healthData;
    };

    $scope.hasSubSystem = function(healthObject) {
      var result = false;
      angular.forEach(healthObject, function(value) {
        if (value && value.status) {
          result = true;
        }
      });
      return result;
    };

    $scope.isHealthObject = function(healthObject) {
      var result = false;
      angular.forEach(healthObject, function(value, key) { // jshint ignore:line
        if (key === 'status') {
          result = true;
        }
      });
      return result;
    };

    $scope.baseName = function(name) {
      if (name) {
        var split = name.split('.');
        return split[0];
      }
    };

    $scope.subSystemName = function(name) {
      if (name) {
        var split = name.split('.');
        split.splice(0, 1);
        var remainder = split.join('.');
        return remainder ? ' - ' + remainder : '';
      }
    };
    BreadcrumbService.updateToolbarHeader({'stateName': $state.current.
    name});
  }]);

