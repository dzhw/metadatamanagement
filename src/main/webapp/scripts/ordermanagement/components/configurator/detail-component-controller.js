(function() {

  'use strict';

  function DataPacketController($scope,
                                $rootScope,
                                $location,
                                DataAcquisitionProjectReleasesResource,
                                $state,
                                $transitions,
                                LanguageService,
                                ProjectReleaseService,
                                ShoppingCartService,
                                MessageBus,
                                StudySearchService,
                                StudyAccessWaysResource, $mdDialog) {
    var $ctrl = this;
    var initReady = false;
    $ctrl.studyIdVersion = {};
    $ctrl.accessWays = [];
    $ctrl.lang = LanguageService.getCurrentInstantly();
    $ctrl.onDataPackageChange = MessageBus;
    $ctrl.noFinalRelease = false;
    $ctrl.dataNotAvailable = false;
    $ctrl.variableNotAccessible = false;
    $ctrl.disabled = false;
    $scope.bowser = $rootScope.bowser;

    function init() {
      var search = $location.search();
      if (search['access-way']) {
        $ctrl.selectedAccessWay = search['access-way'];
      }
      if (!$ctrl.selectedAccessWay) {
        $ctrl.selectedAccessWay = '';
      }
      $ctrl.selectedVersion = $ctrl.studyIdVersion.version;
      loadStudy($ctrl.studyIdVersion.masterId, $ctrl.studyIdVersion.version);
      initReady = true;
    }

    function loadVersion(dataAcquisitionProjectId, id) {
      DataAcquisitionProjectReleasesResource.get(
        {
          id: ProjectReleaseService.stripVersionSuffix(
            dataAcquisitionProjectId
          )
        })
        .$promise
        .then(
        function(releases) {
          $ctrl.releases = releases;
          if (releases.length === 0) {
            $ctrl.noFinalRelease = true;
          }
          loadAccessWays(id);
        });
    }

    function loadStudy(id, version) {
      $rootScope.$broadcast('start-ignoring-404');
      $ctrl.dataNotAvailable = false;
      $ctrl.noFinalRelease = false;
      var excludes = ['nested*','variables','questions',
        'surveys','instruments', 'dataSets', 'relatedPublications',
        'concepts'];
      StudySearchService.findShadowByIdAndVersion(id, version, excludes)
        .promise.then(function(data) {
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
        }).finally(function() {
          $rootScope.$broadcast('stop-ignoring-404');
        });
    }

    function loadAccessWays(id) {
      StudyAccessWaysResource.get({id: id})
        .$promise
        .then(function(data) {
          $ctrl.accessWays = data;
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
    var unregisterTransitionHook = $transitions.onStart({}, function(trans) {
      $ctrl.disabled = trans.$to().name === 'relatedPublicationDetail' ||
        trans.$to().name === 'conceptDetail';
    });

    $scope.$on('$destroy', unregisterTransitionHook);

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
      if (initReady && newVal !== search['access-way']) {
        search['access-way'] = $ctrl.selectedAccessWay;
        if (search['access-way'] === '') {
          delete search['access-way'];
        }
        $location.search(search).replace();
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

    $ctrl.showVersionHelp = function($event) {
      $mdDialog.show($mdDialog.alert()
        .clickOutsideToClose(true)
        .title('Version auswählen')
        .textContent('Sie müssen sich für eine konkrete Version der Daten ' +
        'entscheiden...')
        .ariaLabel('Version auswählen')
        .ok('Ok')
        .fullscreen(true)
        .targetEvent($event)
      );
      console.log('help version');
    };

    $ctrl.showAccessWayHelp = function($event) {
      $mdDialog.show($mdDialog.alert()
        .clickOutsideToClose(true)
        .title('Zugangsweg auswählen')
        .textContent('Sie müssen festlegen, wie Sie mit den Daten arbeiten ' +
        'wollen...')
        .ariaLabel('Zugangsweg auswählen')
        .ok('Ok')
        .fullscreen(true)
        .targetEvent($event)
      );
    };
  }

  angular
    .module('metadatamanagementApp')
    .controller('DataPacketController', DataPacketController);

})();
