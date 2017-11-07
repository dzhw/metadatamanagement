'use strict';

angular.module('metadatamanagementApp').factory(
  'SearchTypeToDetailsStateMapper',
  function($state) {
    function getDetailStateUrl(type, stateParams) {
      switch (type) {
        case 'studies': return $state.href('studyDetail', stateParams);
        case 'surveys': return $state.href('surveyDetail', stateParams);
        case 'instruments':
          return $state.href('instrumentDetail', stateParams);
        case 'questions': return $state.href('questionDetail', stateParams);
        case 'data_sets': return $state.href('dataSetDetail', stateParams);
        case 'variables': return $state.href('variableDetail', stateParams);
        case 'related_publications':
          return $state.href('relatedPublicationDetail', stateParams);
      }
    }

    return {
      getDetailStateUrl: getDetailStateUrl
    };
  }
);
