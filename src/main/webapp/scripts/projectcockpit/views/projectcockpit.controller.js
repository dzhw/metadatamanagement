'use strict';

angular.module('metadatamanagementApp').controller('ProjectCockpitController',
  function($scope, UserResource, PageTitleService, $state, ToolbarHeaderService, CurrentProjectService) {

    PageTitleService.setPageTitle('projectcockpit.title');
    console.log("currentstate", $state.current.name);
    ToolbarHeaderService.updateToolbarHeader({
      'stateName': $state.current.name
    });

    $scope.selectedProject =  CurrentProjectService.getCurrentProject();

    $scope.searchText = '';
    $scope.selectedUser = null;
    $scope.selectedUserChanged = function(user) {
      if(user) {
        $scope.activeUsers.push(user);
        console.log("selected", user);
        $scope.selectedUser = null;
        $scope.searchText = '';
      }
    }
    $scope.activeUsers = [{login:"admin"}]

    $state.currentPromise = null
    $scope.searchUsers = function(search) {
      if(!search) {
        return [];
      }
      if(!$state.currentPromise) {
        $state.currentPromise = UserResource.search({
          login: search
        }).$promise.then(function(result) {
          $state.currentPromise = null;
          console.log("found", result);
          return result.filter(function(x) {
            return $scope.activeUsers.map(function(u){return u.login;}).indexOf(x.login) < 0;
          });
        });
      }
      console.log("searching for", search)
      return $state.currentPromise;
    }

    $scope.removeUser = function(user) {

    }
  });
