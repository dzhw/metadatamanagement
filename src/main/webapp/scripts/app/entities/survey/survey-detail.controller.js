'use strict';

angular.module('metadatamanagementApp')
    .controller('SurveyDetailController', function ($scope, $rootScope, $stateParams, entity, Survey, Variable) {
        $scope.survey = entity;
        $scope.load = function (id) {
            Survey.get({id: id}, function(result) {
                $scope.survey = result;
            });
        };
        var unsubscribe = $rootScope.$on('metadatamanagementApp:surveyUpdate', function(event, result) {
            $scope.survey = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
