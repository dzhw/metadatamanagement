'use strict';

angular.module('metadatamanagementApp').controller('MetricsController',
  function($scope, MonitoringService, $uibModal) {
    $scope.metrics = {};
    $scope.updatingMetrics = true;

    $scope.refresh = function() {
      $scope.updatingMetrics = true;
      MonitoringService.getMetrics().then(function(promise) {
        $scope.metrics = promise;
        $scope.updatingMetrics = false;
      }, function(promise) {
        $scope.metrics = promise.data;
        $scope.updatingMetrics = false;
      });
    };
    $scope.$watch('metrics', function(newValue) {
      $scope.servicesStats = {};
      angular.forEach(newValue, function(value, key) {
        var serviceKey;
        var valueKey;
        if (key.indexOf('eu.dzhw.fdz.metadatamanagement.') !== -1 &&
          key.indexOf('.snapshot.') !== -1) {
          serviceKey = key.substring(0, key.indexOf('.snapshot.'))
            .replace('eu.dzhw.fdz.metadatamanagement.', '');
          $scope.servicesStats[serviceKey] =
            $scope.servicesStats[serviceKey] || {};
          valueKey = key.substring(key.indexOf('.snapshot.'), key.length)
            .replace('.snapshot.', '');
          $scope.servicesStats[serviceKey][valueKey] = value;
        }
        if (key.indexOf('eu.dzhw.fdz.metadatamanagement.') !== -1 &&
          key.indexOf('.snapshot.') === -1) {
          serviceKey = key.substring(0, key.lastIndexOf('.'))
            .replace('eu.dzhw.fdz.metadatamanagement.', '');
          $scope.servicesStats[serviceKey] =
            $scope.servicesStats[serviceKey] || {};
          valueKey = key.substring(key.lastIndexOf('.'), key.length)
            .replace('.', '');
          $scope.servicesStats[serviceKey][valueKey] = value;
        }
      });
    });

    $scope.refresh();

    $scope.refreshThreadDumpData = function() {
      MonitoringService.threadDump().then(function(data) {
        $uibModal.open({
          templateUrl: 'scripts/administration/metrics/' +
            'metrics.modal.html.tmpl',
          controller: 'MetricsModalController',
          size: 'lg',
          resolve: {
            threadDump: function() {
              return data;
            }

          }
        });
      });
    };
  });
