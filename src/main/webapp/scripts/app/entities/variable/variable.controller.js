'use strict';

angular.module('metadatamanagementApp')
    .controller('VariableController', function ($scope, Variable, VariableSearch, ParseLinks) {
        $scope.variables = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Variable.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.variables = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Variable.get({id: id}, function(result) {
                $scope.variable = result;
                $('#deleteVariableConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Variable.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteVariableConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            VariableSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.variables = result;
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
            $scope.variable = {
                name: null,
                dataType: null,
                label: null,
                question: null,
                scaleLevel: null,
                id: null
            };
        };
    });
