'use strict';

angular.module('metadatamanagementApp').controller('SurveyDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Survey', 'Variable',
        function($scope, $stateParams, $modalInstance, entity, Survey, Variable) {

        $scope.survey = entity;
        $scope.variables = Variable.query();
        $scope.load = function(id) {
            Survey.get({id : id}, function(result) {
                $scope.survey = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('metadatamanagementApp:surveyUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.survey.id != null) {
                Survey.update($scope.survey, onSaveFinished);
            } else {
                Survey.save($scope.survey, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
