'use strict';

angular.module('metadatamanagementApp')
  .controller('DatapackageDetailController', function($scope,
    dataAcquisitionProject, $stateParams, $location) {
    $scope.dataAcquisitionProject = dataAcquisitionProject;
    $scope.tabs = ['Eckdaten', 'Informationen zur Studie', 'Datensätze',
      'Zitation', 'Dokumentationsmaterialien'
    ];
    var tab = $scope.tabs.indexOf($stateParams.tab);
    if (tab && tab !== -1) {
      $scope.selectedTab = tab;
    } else {
      $location.search('tab', $scope.tabs[0]);
      $scope.selectedTab = 0;
    }
    $scope.$watch('selectedTab', function(newValue) {
      $location.search('tab', $scope.tabs[newValue]);
    });
    $scope.dataList = [{
      name: 'gra2005-01_W1+2_c-1-0-0',
      variablen: '1.182',
      faelle: '1.179',
      format: 'Personendaten',
      zugangswege: 'Download-CUF'
    }, {
      name: 'gra2005-02_W1+2_c-1-0-0',
      variablen: '-',
      faelle: '-',
      format: 'Episodendaten',
      zugangswege: 'Download-CUF'
    }, {
      name: 'gra2005-01_W1+2_d-1-0-0',
      variablen: '1.260',
      faelle: '10.167',
      format: 'Personendaten',
      zugangswege: 'Download-SUF'
    }, {
      name: 'gra2005-02_W1+2_d-1-0-0',
      variablen: '7',
      faelle: '39.612',
      format: 'Episodendaten',
      zugangswege: 'Download-SUF'
    }, {
      name: 'gra2005-01_W1+2_r-1-0-0',
      variablen: '1.311',
      faelle: '10.167',
      format: 'Personendaten',
      zugangswege: 'Remote Access'
    }, {
      name: 'gra2005-02_W1+2_r-1-0-0',
      variablen: '7',
      faelle: '39.612',
      format: 'Episodendaten',
      zugangswege: 'Remote Access'
    }, {
      name: 'gra2005-01_W1+2_o-1-0-0',
      variablen: '1.369',
      faelle: '10.167',
      format: 'Personendaten',
      zugangswege: 'On-Site'
    }, {
      name: 'gra2005-02_W1+2_o-1-0-0',
      variablen: '7',
      faelle: '39.612',
      format: 'Episodendaten',
      zugangswege: 'On-Site'
    }];
    var init = function() {
      $scope.pageState = {
        currentPageNumber: 1,
        maxSize: 10,
        totalElements: 10
      };
      $scope.pageChanged();
    };
    $scope.pageChanged = function() {
      $scope.currentPage = $scope.dataList;
    };
    init();
  });
