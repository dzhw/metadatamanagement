/* global _ */
'use strict';

angular.module('metadatamanagementApp').factory('ElsstSearchService', ['LanguageService',  
  function(LanguageService) {
    
    var getAltLabel = function(item) {
      var preparedAltLabel = item.altLabel;
        if (!preparedAltLabel) { 
          preparedAltLabel = [];
        } else if (typeof preparedAltLabel == 'string') {
          // sometimes altLabel with only 1 entry is originally not given
          // as array with one entry, but simply as string, and 
          // we want it always formatted as array
          preparedAltLabel = [preparedAltLabel];
        }

        return preparedAltLabel;
    }

    var findTagsElsst = async function(searchText, language) {
      language = language || LanguageService.getCurrentInstantly();
      try {
        var url = 'https://thesauri.cessda.eu/rest/v1/search?query=' 
          + searchText + '*&lang=' + language + '&labellang=' + language + '&vocab=elsst-4&unique=true&fields=altLabel';
        const response = await fetch(url, {
          headers: {
            accept: 'application/json'
          }
        });
        if (!response.ok) {
          console.error('Could not request ELSST tags, network response: ', response);
          return [];
        }
        const data = await response.json();
        return data.results.map(item => ({
          prefLabel: item.prefLabel,
          altLabel: item.altLabel || [],
          localname: item.localname
        }));
      } catch (error) {
        console.error(error);
        return [];
      }
    }

    var findTagsElsstTranslation = async function(prefLabel, origLanguage, translateLang) {
      origLanguage = origLanguage || LanguageService.getCurrentInstantly();
      try {
        var url = 'https://thesauri.cessda.eu/rest/v1/search?query=' 
          + prefLabel + '&lang=' + origLanguage + '&labellang=' + translateLang + '&vocab=elsst-4&unique=true&fields=altLabel';
        const response = await fetch(url, {
          headers: {
            accept: 'application/json'
          }
        });
        if (!response.ok) {
          console.error('Could not request ELSST tags, network response: ', response);
          return [];
        }
        const data = await response.json();

        return data.results.map(item => ({
          prefLabel: item.prefLabel,
          altLabel: getAltLabel(item),
          localname: item.localname
        }));
      } catch (error) {
        console.error(error);
        return [];
      }
    }
  
    return {
      findTagsElsst: findTagsElsst,
      findTagsElsstTranslation: findTagsElsstTranslation
    };
  }]);

