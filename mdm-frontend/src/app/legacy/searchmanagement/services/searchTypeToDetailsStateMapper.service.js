'use strict';

angular.module('metadatamanagementApp').factory(
  'SearchTypeToDetailsStateMapper',
  function() {
    function getDetailStateUrl(type) {
      switch (type) {
        case 'data_packages': return 'dataPackageDetail';
        case 'analysis_packages': return 'analysisPackageDetail';
        case 'surveys': return 'surveyDetail';
        case 'instruments': return 'instrumentDetail';
        case 'questions': return 'questionDetail';
        case 'data_sets': return 'dataSetDetail';
        case 'variables': return 'variableDetail';
        case 'related_publications': return 'relatedPublicationDetail';
        case 'concepts': return 'conceptDetail';
      }
    }

    return {
      getDetailStateUrl: getDetailStateUrl
    };
  }
);
