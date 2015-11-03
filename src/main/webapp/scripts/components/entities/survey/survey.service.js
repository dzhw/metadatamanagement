'use strict';

angular.module('metadatamanagementApp')
    .factory('Survey', function ($resource, DateUtils) {
        return $resource('api/surveys/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.begin = DateUtils.convertLocaleDateFromServer(data.begin);
                    data.endDate = DateUtils.convertLocaleDateFromServer(data.endDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.begin = DateUtils.convertLocaleDateToServer(data.begin);
                    data.endDate = DateUtils.convertLocaleDateToServer(data.endDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.begin = DateUtils.convertLocaleDateToServer(data.begin);
                    data.endDate = DateUtils.convertLocaleDateToServer(data.endDate);
                    return angular.toJson(data);
                }
            }
        });
    });
