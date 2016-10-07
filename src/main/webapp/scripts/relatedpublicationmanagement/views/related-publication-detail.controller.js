'use strict';

angular.module('metadatamanagementApp')
  .controller('RelatedPublicationDetailController',
    function(entity) {

      var ctrl = this;
      entity.$promise.then(function(relatedPublication) {
        ctrl.relatedPublication = relatedPublication;
      });

    });
