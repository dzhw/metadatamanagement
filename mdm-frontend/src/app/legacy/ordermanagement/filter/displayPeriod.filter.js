'use strict';

angular.module('metadatamanagementApp').filter('displayPeriod',
  function() {
    var extractYear = function(date) {
      var year = 0;
      if (date) {
        year = new Date(date).getFullYear();
      }
      return year;
    };
    return function(period) {
      var startYear = extractYear(period.start);
      var endYear = extractYear(period.end);
      if (endYear - startYear === 0) {
        return '' + startYear;
      } else if (endYear - startYear === 1) {
        return startYear + '/' + endYear;
      } else {
        return startYear + '-' + endYear;
      }
    };
  });
