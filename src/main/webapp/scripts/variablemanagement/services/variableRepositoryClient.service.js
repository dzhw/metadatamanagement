'use strict';

angular.module('metadatamanagementApp').factory('VariableRepositoryClient',
  function($http) {
    var findByDataAcquisitionProjectId = function(projectId) {
      return $http({
        method: 'GET',
        url: '/api/variables/search/findByDataAcquisitionProjectId',
        params: {
          dataAcquisitionProjectId: projectId,
          projection: 'id-and-version'
        },
        transformResponse: function(data) {
          var response = angular.fromJson(data);
          if (response._embedded && response._embedded.variables) {
            return response._embedded.variables;
          }
          return response;
        }
      });
    };

    var findAllByPanelIdentifierAndDataSetIdAndIdNot = function(panelIdentifier,
       dataSetId, variableId) {
      return $http({
        method: 'GET',
        url: '/api/variables/search/' +
          'findAllByPanelIdentifierAndDataSetIdAndIdNotOrderByIndexInDataSet',
        params: {
          panelIdentifier: panelIdentifier,
          dataSetId: dataSetId,
          id: variableId,
          projection: 'id-and-version'
        },
        transformResponse: function(data) {
          var response = angular.fromJson(data);
          if (response._embedded && response._embedded.variables) {
            return response._embedded.variables;
          }
          return response;
        }
      });
    };

    var findAllByDerivedVariablesIdentifierAndDataSetIdAndIdNot =
      function(derivedVariablesIdentifier, dataSetId, variableId) {
      return $http({
        method: 'GET',
        url: '/api/variables/search/' +
          'findAllByDerivedVariablesIdentifierAndDataSetIdAndIdNot' +
          'OrderByIndexInDataSet',
        params: {
          derivedVariablesIdentifier: derivedVariablesIdentifier,
          dataSetId: dataSetId,
          id: variableId,
          projection: 'id-and-version'
        },
        transformResponse: function(data) {
          var response = angular.fromJson(data);
          if (response._embedded && response._embedded.variables) {
            return response._embedded.variables;
          }
          return response;
        }
      });
    };

    return {
      findByDataAcquisitionProjectId: findByDataAcquisitionProjectId,
      findAllByPanelIdentifierAndDataSetIdAndIdNot:
        findAllByPanelIdentifierAndDataSetIdAndIdNot,
      findAllByDerivedVariablesIdentifierAndDataSetIdAndIdNot:
        findAllByDerivedVariablesIdentifierAndDataSetIdAndIdNot
    };
  });
