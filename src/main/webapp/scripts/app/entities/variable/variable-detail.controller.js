'use strict';

angular.module('metadatamanagementApp')
    .controller('VariableDetailController', function ($scope, $rootScope, $stateParams, entity, Variable, Survey) {
        $scope.variable = entity;
        $scope.load = function (id) {
            Variable.get({id: id}, function(result) {
                $scope.variable = result;
            });
        };
        var unsubscribe = $rootScope.$on('metadatamanagementApp:variableUpdate', function(event, result) {
            $scope.variable = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
