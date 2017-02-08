/*Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').filter('variableDataType',
  //Filter Function
  function($filter) {
    //Just use the english translation for dataType
    //Input is the expression on the left side at the html
    return function(input, dataTypeEn) {

      //Data Type is Numeric
      if (dataTypeEn === 'numeric') {
        if (input % 1 === 0) {
          return input; //whole number. no filter is needed.
        } else { //commata number
          return $filter('number')(input, 2);
        }
      }

      //Date Type is Date
      if (dataTypeEn === 'date') {
        return $filter('date')(input, 'mediumDate');
      }

      //No Date and no numeric Data Type? Return the default String
      return input;
    };
  });
