/* global _ */
'use strict';

angular.module('metadatamanagementApp').factory('ElsstSearchService', ['LanguageService',  
  function(LanguageService) {
    
    var findTagsElsst = async function(searchText, language) {
      language = language || LanguageService.getCurrentInstantly();
      try {
        const response = await fetch('https://thesauri.cessda.eu/rest/v1/search?query=' 
          + searchText + '*&lang=' + language + '&labellang=' + language + '&vocab=elsst-4&fields=altLabel', {
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
          altLabel: item.altLabel  || [],
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
        const response = await fetch('https://thesauri.cessda.eu/rest/v1/search?query=' 
          + prefLabel + '&lang=' + origLanguage + '&labellang=' + translateLang + '&vocab=elsst-4&fields=altLabel', {
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
          altLabel: item.altLabel  || [],
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

