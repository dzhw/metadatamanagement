/* global _ */

'use strict';

function CTRL($scope,
              $rootScope,
              $location,
              DataAcquisitionProjectReleasesResource,
              $state,
              $transitions,
              LanguageService,
              ProjectReleaseService,
              ShoppingCartService,
              MessageBus,
              StudyResource,
              StudyAccessWaysResource,
              StudySearchService) {
  var $ctrl = this;

  $ctrl.studyIdVersion = {};
  $ctrl.study = {};
  $ctrl.accessWays = [];
  $ctrl.lang = LanguageService.getCurrentInstantly();
  $ctrl.onDataPackageChange = MessageBus;
  $ctrl.noDataSets = false;
  $ctrl.noFinalRelease = false;
  $ctrl.dataNotAvailable = false;
  $ctrl.disabled = false;

  function init() {
    $ctrl.selectedAccessWay = '';
    var search = $location.search();
    if (search['access-way'] && !$ctrl.selectedAccessWay) {
      $ctrl.selectedAccessWay = search['access-way'];
    }
    var createId = '';
    var version = '';
    if ($ctrl.studyIdVersion.version &&
      search.version !== $ctrl.studyIdVersion.version) {
      createId = $ctrl.studyIdVersion.masterId + '-' +
        search.version;
      version = search.version;
      $ctrl.selectedVersion = $ctrl.studyIdVersion.version;
    } else {
      createId = $ctrl.studyIdVersion.masterId + '-' +
        $ctrl.studyIdVersion.version;
      version = $ctrl.studyIdVersion.version;
    }
    findStudy($ctrl.studyIdVersion.masterId, version).then(function(value) {
      // return value;
      if (createId && value) {
        loadStudy(createId);
        $ctrl.selectedVersion = search.version;
      } else {
        $ctrl.study = null;
      }
    }
    );
  }
  function loadVersion(dataAcquisitionProjectId, id) {
    DataAcquisitionProjectReleasesResource.get(
      {
        id: ProjectReleaseService.stripVersionSuffix(
          dataAcquisitionProjectId
        )
      })
      .$promise.then(
      function(releases) {
        $ctrl.releases = releases;
        loadAccessWays(id);
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
          loadVersion($ctrl.study.dataAcquisitionProjectId, id);
        }
      }, function() {
        $ctrl.study = null;
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
          }
        } else {
          $ctrl.noDataSets = true;
        }
      });
  }

  function findStudy(id, version) {
    return StudySearchService.findShadowByIdAndVersion(id, version).promise
      .then(function(result) {
        return !!result;
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
  $transitions.onStart({}, function(trans) {
    $ctrl.disabled = trans.$to().name === 'relatedPublicationDetail' ||
      trans.$to().name === 'conceptDetail';
  });
  $scope.$watch(function() {
    return $ctrl.selectedVersion;
  }, function(newVal) {
    var search = $location.search();
    if (!newVal) { return; }
    if (newVal !== search.version) {
      search.version = $ctrl.selectedVersion;
      search.lang = $rootScope.currentLanguage;
      $state.go($state.current, search, {reload: true});
    }
  });

  $scope.$watch(function() {
    return $ctrl.selectedAccessWay;
  }, function(newVal) {
    var search = $location.search();
    if (newVal !== search.accessWay) {
      search['access-way'] = $ctrl.selectedAccessWay;
      if (search['access-way'] === '') {
        delete search['access-way'];
      }
      $location.replace(search);
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

  $scope.$watch(function() {
    return $ctrl.onDataPackageChange;
  },
    function() {
      var data = $ctrl.onDataPackageChange.get('onDataPackageChange', true);
      if (data) {
        $ctrl.studyIdVersion.masterId = data.masterId;
        $ctrl.studyIdVersion.version = data.version;
        init();
      }
    }, true);
}

angular
  .module('metadatamanagementApp')
  .controller('DataPacketController', CTRL);
