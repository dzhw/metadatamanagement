'use strict';
/* @author Daniel Katzberg */

angular.module('metadatamanagementApp')
  .service('RelatedPublicationBuilderService',
  function(RelatedPublicationResource, CleanJSObjectService) {
    var getRelatedPublications = function(relatedPublications) {
      var relatedPublicationsObjArray = [];
      for (var i = 0; i < relatedPublications.length; i++) {
        var data = relatedPublications[i];
        var relatedPublicationObj = {
          id: data.id,
          sourceReference: data.sourceReference,
          publicationAbstract: data.RelatedPublication,
          doi: data.doi,
          sourceLink: data.sourceLink,
          title: data.title
        };
        var cleanedRelatedPublicationObject = CleanJSObjectService
          .removeEmptyJsonObjects(relatedPublicationObj);
        relatedPublicationsObjArray[i] =
          new RelatedPublicationResource(cleanedRelatedPublicationObject);
      }
      return relatedPublicationsObjArray;
    };
    return {
      getRelatedPublications: getRelatedPublications
    };
  });
