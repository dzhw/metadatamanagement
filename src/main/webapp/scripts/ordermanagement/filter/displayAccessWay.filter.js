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
    var accessWayMapForCitationHint = {
      'download-cuf': 'Download-CUF',
      'download-suf': 'Download-SUF',
      'remote-desktop-suf': 'Remote-Desktop-SUF',
      'onsite-suf': 'On-Site-SUF',
      'not-accessible': 'Not accessible'
    };
    return function(accessWay, citationHintFormat) {
      if (!accessWay) {
        return '';
      }
      if (!citationHintFormat) {
        return accessWayMap[accessWay];
      } else {
        return accessWayMapForCitationHint[accessWay];
      }
    };
  });
