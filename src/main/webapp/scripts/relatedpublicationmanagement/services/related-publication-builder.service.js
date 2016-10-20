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
          publicationAbstract: data.publicationAbstract,
          doi: data.doi,
          sourceLink: data.sourceLink,
          title: data.title,
          questionIds: CleanJSObjectService.removeWhiteSpace(data.questionIds),
          surveyIds: CleanJSObjectService.removeWhiteSpace(data.surveyIds),
          variableIds: CleanJSObjectService.removeWhiteSpace(data.variableIds),
          dataSetIds: CleanJSObjectService.removeWhiteSpace(data.dataSetIds),
          studyIds: CleanJSObjectService.removeWhiteSpace(data.studyIds),
          instrumentIds: CleanJSObjectService
          .removeWhiteSpace(data.instrumentIds)
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
