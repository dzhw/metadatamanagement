'use strict';

angular.module('metadatamanagementApp')
    .controller('DatapackageDetailController', function($scope,
      dataAcquisitionProject) {
      $scope.dataAcquisitionProject = dataAcquisitionProject;
      $scope.dataList = [
      {
        name: 'gra2005-01_W1+2_c-1-0-0',
        variablen: '1.223',
        faelle: '11.789',
        format: 'Personendaten',
        zugangswege: 'Download-CUF'
      },
      {
        name: 'gra2005-02_W1+2_c-1-0-0',
        variablen: '1.223',
        faelle: '6.459',
        format: 'Episodendaten',
        zugangswege: 'Download-CUF'
      },
      {
        name: 'gra2005-01_W1+2_d-1-0-0',
        variablen: '1.223',
        faelle: '11.789',
        format: 'Personendaten',
        zugangswege: 'Download-SUF'
      },
      {
        name: 'gra2005-02_W1+2_d-1-0-0',
        variablen: '1.223',
        faelle: '6.459',
        format: 'Episodendaten',
        zugangswege: 'Download-SUF'
      },
      {
        name: 'gra2005-01_W1+2_r-1-0-0',
        variablen: '1.223',
        faelle: '11.789',
        format: 'Personendaten',
        zugangswege: 'Remote Access'
      },
      {
        name: 'gra2005-02_W1+2_r-1-0-0',
        variablen: '1.223',
        faelle: '6.459',
        format: 'Episodendaten',
        zugangswege: 'Remote Access'
      },
      {
        name: 'gra2005-01_W1+2_o-1-0-0',
        variablen: '1.223',
        faelle: '11.789',
        format: 'Personendaten',
        zugangswege: 'On-Site'
      },
      {
        name: 'gra2005-02_W1+2_o-1-0-0',
        variablen: '1.223',
        faelle: '6.459',
        format: 'Episodendaten',
        zugangswege: 'On-Site'
      }
      ];
    });
