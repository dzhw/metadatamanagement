/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .service('RelatedPublicationBuilderService',
    function(RelatedPublicationResource, CleanJSObjectService,
      RelatedPublicationIdBuilderService) {
      var getStudySerieses = function(desiredStudySerieses,
        availableStudySeries) {
          var result = [];
          _.forEach(desiredStudySerieses, function(desiredSeries) {
            var match = _.find(availableStudySeries, function(availableSeries) {
              return desiredSeries.toLowerCase() ===
                availableSeries.de.toLowerCase();
            });
            if (match) {
              result.push(match);
            } else {
              result.push({
                de: desiredSeries
              });
            }
          });
          return result;
        };
      var getRelatedPublications = function(relatedPublications,
        availableStudySerieses) {
        var relatedPublicationsObjArray = [];
        for (var i = 0; i < relatedPublications.length; i++) {
          var data = relatedPublications[i];
          var relatedPublicationObj = {
            id: RelatedPublicationIdBuilderService.buildRelatedPublicationId(
              data.id),
            sourceReference: data.sourceReference,
            publicationAbstract: data.publicationAbstract,
            doi: data.doi,
            sourceLink: data.sourceLink,
            title: data.title,
            questionIds: CleanJSObjectService
              .removeWhiteSpace(data.questionIds),
            surveyIds: CleanJSObjectService.removeWhiteSpace(data.surveyIds),
            variableIds: CleanJSObjectService
              .removeWhiteSpace(data.variableIds),
            dataSetIds: CleanJSObjectService.removeWhiteSpace(data.dataSetIds),
            instrumentIds: CleanJSObjectService
              .removeWhiteSpace(data.instrumentIds),
            authors: data.authors,
            year: parseInt(data.year),
            abstractSource: {
              en: data['abstractSource.en'],
              de: data['abstractSource.de']
            },
            language: data.language,
            studySerieses: getStudySerieses(
              CleanJSObjectService.parseAndTrim(data['studySerieses.de']),
              availableStudySerieses)
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
