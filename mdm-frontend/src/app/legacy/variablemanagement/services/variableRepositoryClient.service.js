'use strict';

angular.module('metadatamanagementApp').factory('VariableRepositoryClient', ['$http', 
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

    var findAllByRepeatedMeasurementIdentifierAndDataSetIdAndIdNot = function(
        repeatedMeasurementIdentifier, dataSetId, variableId) {
      return $http({
        method: 'GET',
        url: '/api/variables/search/' +
          'findAllByRepeatedMeasurementIdentifierAndDataSetIdAndIdNotOrderBy' +
          'IndexInDataSet',
        params: {
          repeatedMeasurementIdentifier: repeatedMeasurementIdentifier,
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
      findAllByRepeatedMeasurementIdentifierAndDataSetIdAndIdNot:
        findAllByRepeatedMeasurementIdentifierAndDataSetIdAndIdNot,
      findAllByDerivedVariablesIdentifierAndDataSetIdAndIdNot:
        findAllByDerivedVariablesIdentifierAndDataSetIdAndIdNot
    };
  }]);

