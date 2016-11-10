/*!
 * angular-katex v0.10.0
 * https://github.com/tfoxy/angular-katex
 *
 * Copyright 2015 Tomás Fox
 * Released under the MIT license
 */

(function() {
  'use strict';

  angular.module('katex', [])
      .constant('katex', katex)
      .constant('renderMathInElement', getRenderMathInElement())
      .provider('katexConfig', katexConfigProvider)
      .directive('katex', katexDirective)
      // katexBind is deprecated
      .directive('katexBind', katexBindDirective);


  function getRenderMathInElement() {
    return typeof renderMathInElement !== 'undefined' ?
        renderMathInElement :
        undefined;
  }


  katexConfigProvider.$inject = ['katex', 'renderMathInElement'];

  function katexConfigProvider(katex, renderMathInElement) {
    var service = {
      $get: function() {return service;},
      defaultOptions: {},
      errorElement: '<span class="katex-error"></span>',
      errorHandler: function(err, expr, element) {
        var span = angular.element(service.errorElement);
        span.text(err);
        element.children().remove();
        element.append(span);
      },
      render: function render(element, expr, scope, attrs) {
        try {
          var options = getOptions(scope, attrs);
          katex.render(expr || '', element[0], options);
        } catch (err) {
          getErrorHandler(scope, attrs)(err, expr, element);
        }
      },
      autoRender: function autoRender(element, scope, attrs) {
        try {
          var options = getOptions(scope, attrs);
          renderMathInElement(element[0], options);
        } catch (err) {
          getErrorHandler(scope, attrs)(err, null, element);
        }
      }
    };

    return service;

    function getOptions(scope, attrs) {
      var options = angular.extend({}, service.defaultOptions);
      if ('options' in attrs) {
        angular.extend(options, scope.$eval(attrs.options));
      }
      if ('katexOptions' in attrs) {
        angular.extend(options, scope.$eval(attrs.katexOptions));
      }
      if ('displayMode' in attrs) {
        angular.extend(options, {displayMode: true});
      }
      return options;
    }

    function getErrorHandler(scope, attrs) {
      if ('onError' in attrs || 'katexOnError' in attrs) {
        return function katexOnErrorFn(err, expr, element) {
          scope.$eval(attrs.onError || attrs.katexOnError, {
            $err: err,
            $expr: expr,
            $setText: function(text) {
              element.text(text);
            }
          });
        };
      } else {
        return service.errorHandler;
      }
    }
  }


  katexDirective.$inject = ['katexConfig', '$rootScope'];

  function katexDirective(katexConfig, $rootScope) {
    return {
      restrict: 'AE',
      compile: compile
    };

    function compile(element, attrs) {
      if ('bind' in attrs) {
        return link;
      } else {
        var expr;

        if ('expr' in attrs || attrs.katex) {
          expr = attrs.expr || attrs.katex;
          if (hasHtmlModeOn($rootScope, attrs)) {
            expr = angular.element('<div>' + expr + '</div>').html();
          }
          if ('autoRender' in attrs || 'katexAutoRender' in attrs) {
            element.text(expr);
            katexConfig.autoRender(element, $rootScope, attrs);
            return;
          }
        } else if ('autoRender' in attrs || 'katexAutoRender' in attrs) {
          katexConfig.autoRender(element, $rootScope, attrs);
          return;
        } else {
          expr = hasHtmlModeOn($rootScope, attrs) ?
              element.html() :
              element.text();
        }

        katexConfig.render(element, expr, $rootScope, attrs);
      }
    }

    function link(scope, element, attrs) {
      if ('autoRender' in attrs || 'katexAutoRender' in attrs) {
        scope.$watch(attrs.bind, function(expr) {
          if (hasHtmlModeOff(scope, attrs)) {
            element.text(expr);
          } else {
            element.html(expr);
          }
          katexConfig.autoRender(element, scope, attrs);
        });
      } else {
        scope.$watch(attrs.bind, function(expr) {
          katexConfig.render(element, expr, scope, attrs);
        });
      }
    }
  }


  katexBindDirective.$inject = ['katexConfig'];

  function katexBindDirective(katexConfig) {
    console.warn(
      'katex-bind directive is deprecated. ' +
      'Please use katex directive with bind attribute instead. ' +
      'e.g.: <span katex bind="expr"></span>'
    );
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        scope.$watch(attrs.katexBind, function(expr) {
          if ('autoRender' in attrs || 'katexAutoRender' in attrs) {
            element.text(expr);
            katexConfig.autoRender(element, scope, attrs);
          } else {
            katexConfig.render(element, expr, scope, attrs);
          }
        });
      }
    };
  }


  function hasHtmlModeOn(scope, attrs) {
    return 'htmlMode' in attrs && (!attrs.htmlMode || scope.$eval(attrs.htmlMode));
  }

  function hasHtmlModeOff(scope, attrs) {
    return 'htmlMode' in attrs && !scope.$eval(attrs.htmlMode);
  }
})();
