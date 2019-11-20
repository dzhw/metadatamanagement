/* global _ */

'use strict';

function CTRL($scope,
              $rootScope,
              $location,
              DataAcquisitionProjectReleasesResource,
              $state,
              ProjectReleaseService,
              ShoppingCartService,
              StudyResource,
              StudyAccessWaysResource) {
  var $ctrl = this;
  var registerScope = null;

  $ctrl.studyIdVersion = {};
  $ctrl.study = {};
  $ctrl.accessWays = [];
  $ctrl.lang = '';

  $ctrl.noDataSets = false;
  $ctrl.noFinalRelease = false;
  $ctrl.dataNotAvailable = false;
  // $ctrl.$onInit = init;

  function init() {
    $ctrl.lang = $rootScope.currentLanguage;
    var search = $location.search();
    var createId = '';
    if ($ctrl.studyIdVersion.version &&
      search.version !== $ctrl.studyIdVersion.version) {
      createId = $ctrl.studyIdVersion.masterId + '-' +
        search.version;
    } else {
      createId = $ctrl.studyIdVersion.masterId + '-' +
        $ctrl.studyIdVersion.version;
    }
    if (createId) {
      loadStudy(createId);
      loadAccessWays(createId);
    }
  }
  function loadVersion(id) {
    DataAcquisitionProjectReleasesResource.get(
      {
        id: ProjectReleaseService.stripVersionSuffix(
          id
        )
      })
      .$promise.then(
      function(releases) {
        $ctrl.releases = releases;
        if (releases.length > 0) {
          if ($ctrl.version) {
            $ctrl.selectedVersion = $ctrl.version;
          } else {
            if (!$ctrl.selectedVersion) {
              $ctrl.selectedVersion = releases[0].version;
            }
          }
        } else {
          $ctrl.noFinalRelease = true;
        }
      });
  }

  function loadStudy(id) {
    StudyResource.get({id: id})
      .$promise
      .then(function(data) {
        $ctrl.study = data;
        if ($ctrl.study) {
          if ($ctrl.study.dataAvailability.en === 'Not available') {
            $ctrl.dataNotAvailable = true;
          }

          if ($ctrl.study.dataAvailability.en === 'In preparation') {
            $ctrl.noFinalRelease = true;
          }
          loadVersion($ctrl.study.dataAcquisitionProjectId);
        }
      });
  }
  function loadAccessWays(id) {
    StudyAccessWaysResource.get({id: id})
      .$promise
      .then(function(data) {
        $ctrl.accessWays = data;
        if ($ctrl.accessWays.length > 0) {
          if (_.includes($ctrl.accessWays, 'not-accessible')) {
            $ctrl.variableNotAccessible = true;
          } else {
            if (!$ctrl.selectedAccessWay) {
              $ctrl.selectedAccessWay = $ctrl.accessWays[0];
            }
          }
        } else {
          $ctrl.noDataSets = true;
        }
      });
  }

  $ctrl.addToShoppingCart = function() {
    ShoppingCartService.add({
      dataAcquisitionProjectId: $ctrl.study.dataAcquisitionProjectId,
      accessWay: $ctrl.selectedAccessWay,
      version: $ctrl.selectedVersion,
      study: {
        id: $ctrl.study.id
      }
    });
  };

  $scope.$watch(function() {
    return $ctrl.selectedVersion;
  }, function(newVal, oldVal) {
    if (newVal !== oldVal) {
      var search = $location.search();
      search.version = $ctrl.selectedVersion;
      search.id = $ctrl.study.masterId;
      search['search-result-index'] = null;
      search.lang = $rootScope.currentLanguage;
      //$location.search(search);
      $state.go($state.current, search, {reload: true});
    }
  });

  $scope.$watch(function() {
    return $ctrl.selectedAccessWay;
  }, function(newVal, oldVal) {
    if (newVal !== oldVal) {
      var search = $location.search();
      search['access-way'] = $ctrl.selectedAccessWay;
      $location.search(search);
    }
  });

  $scope.$watch(function() {
    return $rootScope.currentLanguage;
  },
    function(newVal, oldVal) {
    if (newVal !== oldVal) {
      $ctrl.lang = $rootScope.currentLanguage;
    }
  });

  // Event
  registerScope = $rootScope.$on('onDataPackageChange',
    function(event, args) { // jshint ignore:line
      if (args.masterId) {
        $ctrl.studyIdVersion.masterId = args.masterId;
        $ctrl.studyIdVersion.version = args.version;
        init();
      } else {
        $ctrl.studyIdVersion = {};
      }
    });

  $ctrl.$onDestroy = function() {
    //unregister rootScope event by calling the return function
    registerScope();
  };
}

angular
  .module('metadatamanagementApp')
  .controller('DataPacketController', CTRL);
