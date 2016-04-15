/* global Handlebars */
'use strict';

angular.module('metadatamanagementApp').factory(
    'HandlebarsService',
    function($translate) {
      Handlebars.registerHelper('translate', function(options) {
        var prefix = options.hash.prefix;
        var key = options.hash.key;
        if (prefix) {
          return $translate.instant(prefix + '.' + key);
        } else {
          return $translate.instant(key);
        }
      });

      Handlebars.registerHelper('encodeMissing', function(isMissing) {
        if (isMissing) {
          return 'M';
        } else {
          return '';
        }
      });

      return Handlebars;
    }
  );
