'use strict';

angular.module('metadatamanagementApp').controller('ProjectCockpitController',
  function($scope, $state, $mdDialog, LanguageService, $translate, UserResource, Principal, PageTitleService, ToolbarHeaderService, CurrentProjectService) {

    PageTitleService.setPageTitle('projectcockpit.title');
    ToolbarHeaderService.updateToolbarHeader({
      stateName: $state.current.name
    });

    // Show message and leave page if navigated here without a selected project
    $scope.selectedProject =  CurrentProjectService.getCurrentProject();
    if(!$scope.selectedProject) {
      var alert = $mdDialog.alert({
        title: $translate.instant('projectcockpit.alert.title'),
        textContent:  $translate.instant('projectcockpit.alert.noproject'),
        ok: $translate.instant('projectcockpit.alert.close')
      });
      var dialog = $mdDialog
        .show(alert)
        .finally(function() {
          alert = undefined;
      });
      dialog.then(function(){
        $state.go('search', {
          lang: LanguageService.getCurrentInstantly()
        });
      });
    }

    Principal.identity().then(function(account) {
      $scope.account = account;
    });

    $scope.saveChanges = function() {

    }

    $scope.changed = false;
    $scope.searchText = '';
    $scope.selectedUser = null;
    $scope.selectedUserChanged = function(user) {
      if(user) {
        $scope.activeUsers.push(user);
        $scope.changed = true;
        $scope.searchText = '';
        $scope.selectedUser = null;
        console.log("selected", user);
      }
    }
    $scope.activeUsers = [];

    $state.currentPromise = null;
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
            // filter out already added users
            return $scope.activeUsers.map(function(u){return u.login;}).indexOf(x.login) < 0;
          });
        });
      }
      console.log("searching for", search)
      return $state.currentPromise;
    }

    $scope.removeUser = function(user) {
      $scope.changed = true;
      $scope.activeUsers = $scope.activeUsers.filter(function(item) {
        return item.login != user.login;
      })
    }
  });
