'use strict';

angular.module('metadatamanagementApp')
  .service('RelatedPublicationBuilderService',
    function(RelatedPublicationResource, CleanJSObjectService,
      RelatedPublicationIdBuilderService) {
      var getRelatedPublications = function(relatedPublications) {
        var relatedPublicationsObjArray = [];
        for (var i = 0; i < relatedPublications.length; i++) {
          var data = relatedPublications[i];
          var bibTexKey = getPropertyByKeyPattern(data, /^BibTeX Key$/i);
          var relatedPublicationObj = {
            /* jshint -W069 */
            id: bibTexKey ?
              RelatedPublicationIdBuilderService.buildRelatedPublicationId(
                bibTexKey) : undefined,
            sourceReference:
              getPropertyByKeyPattern(data, /^sourceReference.*$/i),
            doi: getPropertyByKeyPattern(data, /^DOI$/i),
            title: getPropertyByKeyPattern(data, /^Titel$/i),
            authors: getPropertyByKeyPattern(data, /^Autor.*$/i),
            language: getPropertyByKeyPattern(data, /^Sprache$/i),
            year: parseInt(getPropertyByKeyPattern(data, /^Jahr.*$/i)),
            annotations: {
              de: getPropertyByKeyPattern(data, /^annotations\.de.*$/i),
              en: getPropertyByKeyPattern(data, /^annotations\.en.*$/i),
            },
            abstractSource:
              getPropertyByKeyPattern(data, /^abstractSource.*$/i),
            dataPackageIds: CleanJSObjectService.removeWhiteSpace(
              getPropertyByKeyPattern(data, /^dataPackageIds.*$/i)),
            analysisPackageIds: CleanJSObjectService.removeWhiteSpace(
              getPropertyByKeyPattern(data, /^analysisPackageIds.*$/i)),
            sourceLink: getPropertyByKeyPattern(data, /^Online-Adresse$/i),
            publicationAbstract: getPropertyByKeyPattern(data, /^Abstract$/i)
            /* jshint +W069 */
          };
          var cleanedRelatedPublicationObject = CleanJSObjectService
            .removeEmptyJsonObjects(relatedPublicationObj);
          relatedPublicationsObjArray[i] =
            new RelatedPublicationResource(cleanedRelatedPublicationObject);
        }
        return relatedPublicationsObjArray;
      };

      var getPropertyByKeyPattern = function(object, regex) {
        return object[Object.keys(object).filter(function(key) {
          return regex.test(key);
        })[0]];
      };

      return {
        getRelatedPublications: getRelatedPublications
      };
    });
