/* global Plotly*/
'use strict';

angular.module('metadatamanagementApp').factory('Plotly',
  function($rootScope, LanguageService) {
    var plotlyService = Plotly;
    var language = LanguageService.getCurrentInstantly();
    if (language === 'en') {
      language = 'en-US';
    }
    Plotly.setPlotConfig({locale: language});
    $rootScope.$on('current-language-changed',
      function(event, newLanguage) { // jshint ignore:line
        if (newLanguage === 'en') {
          newLanguage = 'en-US';
        }
        Plotly.setPlotConfig({locale: newLanguage});
      });
    return plotlyService;
  });
