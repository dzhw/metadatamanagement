'use strict';

angular.module('metadatamanagementApp')
  .controller('NavbarController',
    function($scope, $state, Auth, Principal, ENV, NavbarService) {
      $scope.isAuthenticated = Principal.isAuthenticated;
      $scope.$state = $state;
      $scope.inProductionOrDev = ENV === 'prod' || ENV === 'dev';

      var vm = this;
      vm.menu = NavbarService;

      function isOpen(section) {
        return vm.menu.isSectionSelected(section);
      }

      function toggleOpen(section) {
        vm.menu.toggleSelectSection(section);
      }

      //functions for menu-link and menu-toggle
      vm.isOpen = isOpen;
      vm.toggleOpen = toggleOpen;
      vm.autoFocusContent = false;

      vm.status = {
        isFirstOpen: true,
        isFirstDisabled: false
      };

      $scope.vm = vm;

    })
  .filter('nospace', function() {
    return function(value) {
      return (!value) ? '' : value.replace(/ /g, '');
    };
  }) //replace uppercase to regular case
  .filter('authentication', function() {
    return function(section, $scope) {
      var filteredSections = [];
      var authenticated = $scope.isAuthenticated;

      console.log(authenticated);

      for (var i = 0; i < section.length; i++) {
        if (section[i].authentication === authenticated ||
          section[i].authentication === undefined) {
          if (section[i].type === 'toggle') {
            var filteredPages = [];
            for (var j = 0; j < section[i].pages.length; j++) {
              if (section[i].pages[j].authentication === authenticated ||
                section[i].pages[j].authentication === undefined) {
                filteredPages.push(section[i].pages[j]);
              }
            }
            section[i].pages = filteredPages;
          }
          filteredSections.push(section[i]);
        }
      }

      return filteredSections;
    };
  });
