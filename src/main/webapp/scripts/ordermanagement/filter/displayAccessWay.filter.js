'use strict';

angular.module('metadatamanagementApp').filter('displayAccessWay',
  function() {
    var accessWayMap = {
      'download-cuf': 'Download-CUF',
      'download-suf': 'Download-SUF',
      'remote-desktop-suf': 'Remote-Desktop-SUF',
      'onsite-suf': 'On-Site-SUF',
      'not-accessible': 'Not accessible'
    };
    return function(accessWay) {
      if (!accessWay) {
        return '';
      }
      return accessWayMap[accessWay];
    };
  });
