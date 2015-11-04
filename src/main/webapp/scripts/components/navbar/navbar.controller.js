'use strict';

angular.module('metadatamanagementApp')
    .controller('NavbarController', function ($scope, $location, $state, Auth, Principal, ENV) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.$state = $state;
        $scope.inProductionOrDev = ENV === 'prod' || ENV === 'dev';

        $scope.logout = function () {
            Auth.logout();
            $state.go('home');
        };
    });
