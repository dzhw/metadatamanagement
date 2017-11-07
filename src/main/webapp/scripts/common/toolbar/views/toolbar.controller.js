'use strict';

angular.module('metadatamanagementApp').controller(
  'ToolbarController',
  function($scope, $mdSidenav, $document, $location) {
    //Toggle Function
    $scope.toggleLeft = function() {
      $mdSidenav('SideNavBar').toggle();
    };

    $scope.$location = $location;

    $scope.$watch(function() {
      return $document.find('#toolbar')[0].clientHeight;
    }, function(newHeight) {
      $document.find('.fdz-content').css(
        'margin-top', newHeight);
      $document.find('#toast-container').css(
        'margin-top', newHeight);
    });
  });
