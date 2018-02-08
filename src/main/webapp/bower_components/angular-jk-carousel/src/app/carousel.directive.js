(function() {

  'use strict';

  function CarouselDirective() {

    function link(scope, element, attrs, ctrl) {
      if (attrs.autoSlide === undefined) {
        ctrl.autoSlide = false;
      }
      if (attrs.autoSlideTime === undefined) {
        ctrl.autoSlideTime = 5000;
      }
      if (attrs.autoSlideStopOnAction === undefined) { 
        ctrl.autoSlideStopOnAction = false; 
      }       	  
      ctrl.registerElement(element);
      scope.$on('$destroy', function() {
        ctrl.stopAutoSlide();
      });
      scope.$watch('ctrl.autoSlide', function() {
        ctrl.validateAutoSlide();
      });
      scope.$watch('ctrl.autoSlideTime', function() {
        ctrl.restartAutoSlide();
      });
      scope.$watch('ctrl.autoSlideStopOnAction', function() { 
        ctrl.validateAutoSlideStopOnAction(); 
      });
      scope.$watch('ctrl.data', function () {
        ctrl.onDataChange();
      });
      
    }

    return {
      restrict: 'E',
      replace: true,
      templateUrl: 'carousel-directive.html',
      scope: {},
      controller: 'JKCarouselController',
      controllerAs: 'ctrl',
      bindToController: {
        data: '=',
        currentIndex: '=',
        itemTemplateUrl: '=',
        maxWidth: '@?',
        maxHeight: '@?',
        autoSlide: '@?',
        autoSlideTime: '@?', 
        autoSlideStopOnAction: '@?'
      },
      link: link
    };
  }

  angular
    .module('jkAngularCarousel')
    .directive('jkCarousel', [
    CarouselDirective
  ]);

}());
