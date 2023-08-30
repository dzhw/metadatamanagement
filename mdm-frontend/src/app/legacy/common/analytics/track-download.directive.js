'use strict';

angular.module('metadatamanagementApp').directive('fdzTrackDownload', ['$analytics', '$rootScope', 
    function($analytics, $rootScope) {
        return {
          restrict: 'A',
          /* jshint -W098 */
          link: function($scope, $element, $attrs) {
            $element.addClass('piwik_ignore');
            /* jshint +W098 */
            angular.element($element[0]).on('click', function() {
              var href = $attrs.href.indexOf($rootScope.baseUrl) >= 0 ?
                $attrs.href : $rootScope.baseUrl + $attrs.href;
              $analytics.trackLink(href, 'download');
            });
          }
        };
      }]);

