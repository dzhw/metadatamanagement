/* global bowser */
/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('EndOfLineService',
  function() {

    //Check for windows. Windows has an another linebreak then unix based os'
    var getOsDependingEndOfLine = function() {
        if (bowser.windows || bowser.windowsphone) {
          return '\r\n';
        }

        //default for all other os then windows
        return '\n';
      };
    return {
      getOsDependingEndOfLine: getOsDependingEndOfLine
    };
  });
