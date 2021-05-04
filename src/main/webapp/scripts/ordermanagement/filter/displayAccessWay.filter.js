'use strict';

angular.module('metadatamanagementApp').filter('displayAccessWay',
  function() {
    var accessWayMap = {
      'download-cuf': 'CUF: Download',
      'download-suf': 'SUF: Download',
      'remote-desktop-suf': 'SUF: Remote-Desktop',
      'onsite-suf': 'SUF: On-Site',
      'not-accessible': 'Not accessible'
    };
    return function(accessWay) {
      if (!accessWay) {
        return '';
      }
      return accessWayMap[accessWay];
    };
  });
