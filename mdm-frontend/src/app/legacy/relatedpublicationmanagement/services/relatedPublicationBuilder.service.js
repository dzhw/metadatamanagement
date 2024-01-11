'use strict';

angular.module('metadatamanagementApp')
  .service('RelatedPublicationBuilderService', ['RelatedPublicationResource', 'CleanJSObjectService',
      'RelatedPublicationIdBuilderService', '$http', '$q',
    function(RelatedPublicationResource, CleanJSObjectService,
      RelatedPublicationIdBuilderService, $http, $q) {

      const updatePublicationObj = function(publicationObj, doi) {
        if (doi) {
          return findByDOI(doi).then(function(data) {
            publicationObj.journal = data.data["container-title"] ? data.data["container-title"] : null; 
            if (data.data.hasOwnProperty("journal-issue")){
              publicationObj.issue = data.data["journal-issue"].issue ? data.data["journal-issue"].issue : null;  
            }
            return new RelatedPublicationResource(CleanJSObjectService.removeEmptyJsonObjects(publicationObj));
          });
        } else {
          return $q((resolve, reject) => {
            resolve(new RelatedPublicationResource(CleanJSObjectService.removeEmptyJsonObjects(publicationObj)))
          });
        }
      }

      var getRelatedPublications = async function(relatedPublications) {
        // var relatedPublicationsObjArray = [];
        var promises = [];
        for (var i = 0; i < relatedPublications.length; i++) {
          var data = relatedPublications[i];
          var doi = getPropertyByKeyPattern(data, /^DOI$/i);
          var bibTexKey = getPropertyByKeyPattern(data, /^BibTeX Key$/i);

          var publicationObj = {
            /* jshint -W069 */
            id: bibTexKey ?
              RelatedPublicationIdBuilderService.buildRelatedPublicationId(
                bibTexKey) : undefined,
            sourceReference:
              getPropertyByKeyPattern(data, /^sourceReference.*$/i),
            doi: doi,
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
          promises.push(updatePublicationObj(publicationObj, doi));
        };

        return await new Promise((resolve, reject) => {
          $q.all(promises).then(results => {
            resolve(results);
          });
        });
      }

      var getPropertyByKeyPattern = function(object, regex) {
        return object[Object.keys(object).filter(function(key) {
          return regex.test(key);
        })[0]];
      };

      /**
       * This function sends a request to http://doi.org/{doi}
       * and returns the response as Promise.
       * @param doi A DOI code, e.g. "10.5771/0038-6073-2023-2-173"
       */
       var findByDOI = function(doi) {
        return $http({
          method: 'GET',
          url: 'http://doi.org/' + doi,
          headers: {
            'Accept': 'application/json'
          }
        }).then(function(data) {
          var response = angular.fromJson(data);
          return response;
        });
      }

      return {
        getRelatedPublications: getRelatedPublications
      };
    }]);

