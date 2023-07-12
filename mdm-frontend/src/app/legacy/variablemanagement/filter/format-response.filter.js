/* global _,$ */
'use strict';

angular.module('metadatamanagementApp').filter('formatResponse',
  function($filter, LanguageService) {
    return function(value, variable, meanValue) {

      //Data Type is Numeric
      if ($.isNumeric(value) && variable.dataType.en === 'numeric') {
        if (!meanValue && variable.storageType === 'integer') {
          if (variable.doNotDisplayThousandsSeparator) {
            return value;
          } else {
            return $filter('number')(value, 0);
          }
        } else {
          if (variable.doNotDisplayThousandsSeparator) {
            if (LanguageService.getCurrentInstantly() === 'de') {
              return _.replace(
                $filter('number')(value, meanValue ? 2 :
                  variable.distribution.maxNumberOfDecimalPlaces),
                new RegExp('\\.','g'), '');
            } else {
              return _.replace(
                $filter('number')(value, meanValue ? 2 :
                  variable.distribution.maxNumberOfDecimalPlaces),
                new RegExp(',','g'), '');
            }
          } else {
            return $filter('number')(value, meanValue ? 2 :
              variable.distribution.maxNumberOfDecimalPlaces);
          }
        }
      }

      //Date Type is Date
      if (variable.dataType.en === 'date') {
        return $filter('date')(value, 'mediumDate');
      }

      return value;
    };
  });
