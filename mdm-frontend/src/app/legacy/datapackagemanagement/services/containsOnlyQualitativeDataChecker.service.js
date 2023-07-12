'use strict';
angular.module('metadatamanagementApp').service(
  'ContainsOnlyQualitativeDataChecker',
  function() {
    var check = function(searchDocument) {
      var onlyQualitativeData;
      if (searchDocument.surveys && searchDocument.surveys.length > 0) {
        onlyQualitativeData = true;
        searchDocument.surveys.forEach(function(survey) {
          if (survey.dataType.en !== 'Qualitative Data') {
            onlyQualitativeData = false;
          }
        });
      }
      return onlyQualitativeData;
    };
    return {
      check: check
    };
  });
