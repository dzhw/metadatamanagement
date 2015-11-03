'use strict';

angular.module('metadatamanagementApp')
    .controller('SurveyController', function ($scope, Survey, SurveySearch, ParseLinks) {
        $scope.surveys = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Survey.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.surveys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Survey.get({id: id}, function(result) {
                $scope.survey = result;
                $('#deleteSurveyConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Survey.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSurveyConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            SurveySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.surveys = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.survey = {
                title: null,
                begin: null,
                endDate: null,
                id: null
            };
        };
    });
