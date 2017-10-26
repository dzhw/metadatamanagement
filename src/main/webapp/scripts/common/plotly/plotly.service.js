'use strict';
/* global Plotly*/

angular.module('metadatamanagementApp').factory('Plotly',
  function($rootScope, LanguageService) {
    var locales = {
      de: Plotly.d3.locale({
        decimal: ',',
        thousands: '.',
        grouping: [3],
        currency: ['€', ''],
        dateTime: '%a %b %e %X %Y',
        date: '%m.%d.%Y',
        time: '%H:%M:%S',
        periods: ['AM', 'PM'],
        days: ['Sontag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag',
          'Freitag', 'Samstag'],
        shortDays: ['So', 'Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa'],
        months: ['Januar', 'Februar', 'März', 'April', 'Mai', 'Juni',
          'Juli', 'August', 'September', 'Oktober', 'November', 'Dezember'],
        shortMonths: ['Jan', 'Feb', 'Mär', 'Apr', 'Mai', 'Jun', 'Jul', 'Aug',
          'Sep', 'Okt', 'Nov', 'Dez']
      }),
      en: Plotly.d3.locale({
        decimal: '.',
        thousands: ',',
        grouping: [3],
        currency: ['$', ''],
        dateTime: '%a %b %e %X %Y',
        date: '%Y-%m-%d',
        time: '%H:%M:%S',
        periods: ['AM', 'PM'],
        days: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday',
          'Friday', 'Saturday'],
        shortDays: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
        months: ['January', 'February', 'March', 'April', 'May', 'June',
          'July', 'August', 'September', 'October', 'November', 'December'],
        shortMonths: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug',
          'Sep', 'Oct', 'Nov', 'Dec']
      }),
    };
    var plotlyService = Plotly;
    Plotly.d3.format = locales[LanguageService.getCurrentInstantly()]
      .numberFormat;
    $rootScope.$on('current-language-changed',
      function(event, newLanguage) { // jshint ignore:line
        Plotly.d3.format = locales[newLanguage].numberFormat;
      });
    return plotlyService;
  });
