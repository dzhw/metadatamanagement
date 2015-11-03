'use strict';

angular.module('metadatamanagementApp').controller('VariableDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Variable', 'Survey',
        function($scope, $stateParams, $modalInstance, entity, Variable, Survey) {

        $scope.variable = entity;
        $scope.surveys = Survey.query();
        $scope.load = function(id) {
            Variable.get({id : id}, function(result) {
                $scope.variable = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('metadatamanagementApp:variableUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.variable.id != null) {
                Variable.update($scope.variable, onSaveFinished);
            } else {
                Variable.save($scope.variable, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
