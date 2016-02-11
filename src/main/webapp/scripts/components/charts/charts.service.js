/* @Author Daniel Katzberg */
'use strict';

/* A service for sharing a entity between controller. */
angular.module('metadatamanagementApp').factory('ShareEntity', function() {

  //actual entity
  var entity;

  return {
    getEntity: function() {
      return entity;
    },
    setEntity: function(entityValue) {
      entity = entityValue;
    }
  };
});
