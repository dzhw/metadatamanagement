'use strict';

angular.module('metadatamanagementApp')
.factory('ConceptAttachmentResource', ['$resource', 'CleanJSObjectService', 
  function($resource, CleanJSObjectService) {
      return $resource('/api/concepts/:conceptId/attachments/:fileName', {
        conceptId: '@conceptId',
        fileName: '@fileName'
      }, {
        'findByConceptId': {
          method: 'GET',
          isArray: true
        },
        'save': {
          method: 'PUT',
          params: {
            conceptId: '@conceptId',
            fileName: function(data) {
              var value = '';
              if (data.fileName.charAt(0) === '.') {
                value = '\\';
              }
              return value + data.fileName;
            }
          },
          transformRequest: function(attachment) {
            var copy = angular.copy(attachment);
            CleanJSObjectService.deleteEmptyStrings(copy);
            CleanJSObjectService.removeEmptyJsonObjects(copy);
            return angular.toJson(copy);
          }
        },
        'delete': {
          method: 'DELETE',
          params: {
            conceptId: '@conceptId',
            fileName: function(data) {
              var value = '';
              if (data.fileName.charAt(0) === '.') {
                value = '\\';
              }
              return value + data.fileName;
            }
          }
        }
      });
    }]);

