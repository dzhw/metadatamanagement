(function() {
  'use strict';

  function CarouselController($timeout, $attrs, $interval, $window) {

    var that = this;
    that.currentIndex = 0;
    that.currentMarginLeftValue = 0;
    that.radioButtonIndex = 0;
    that.transitionsTime = 500;
    that.transitionsEnabled = true;

    $attrs.$observe('data', function() {
      that.onDataChange();
    });

    that.registerElement = function(element) {
      that.element = element;
      that.elementParent = that.element.parent();
      that.slidesContainer = angular.element(that.element.find('div')[0]);
      $window.addEventListener('resize', function() {
        that.updateSlidesContainerWidth();
      });
    };

    that.onDataChange = function() {
      if (!that.data || that.data.length === 0) {
        return;
      }
      that.executeCloneData();
      $timeout(function() {
        that.updateSlidesContainerWidth();
        that.restartFromFirstItem();
      });
    };

    that.updateSlidesContainerWidth = function() {
      that.scaleContent();
      that.currentWidth = that.element.prop('offsetWidth');
      that.currentHeight = that.element.prop('offsetHeight');
      that.resizeSlides();
      var newSlidesContainerWidth = that.currentWidth * that.cloneData.length;
      that.slidesContainer.css('width', newSlidesContainerWidth + 'px');
      that.scaleMarginLeft(newSlidesContainerWidth);
      that.currentSlidesContainerWidth = newSlidesContainerWidth;
    };

    that.scaleContent = function() {
      that.maxWidth = that.maxWidth ? parseInt(that.maxWidth): 0;
      if( that.maxWidth === 0 ){
        that.maxWidth = that.element.prop('offsetWidth');
      }
      that.maxHeight = that.maxHeight ? parseInt(that.maxHeight): 0;
      if( that.maxHeight === 0 ){
        that.maxHeight = that.element.prop('offsetHeight');
      }
      var currentElementParentWidth = that.elementParent.prop('offsetWidth');
      if( currentElementParentWidth < that.maxWidth ){
        var newHeight = (that.maxHeight * currentElementParentWidth) / that.maxWidth;
        that.element.css('width', currentElementParentWidth + 'px');
        that.element.css('height', newHeight + 'px');
      }else if( currentElementParentWidth >= that.maxWidth ){
        that.element.css('width', that.maxWidth + 'px');
        that.element.css('height', that.maxHeight + 'px');
      }
    };

    that.resizeSlides = function(){
      var slides = that.element[0].querySelectorAll('.slide');
      for( var index=0; index < slides.length; index++ ){
        var slide = angular.element(slides[index]);
        slide.css('width', that.currentWidth + 'px');
        slide.css('height', that.currentHeight + 'px');
      }
    };

    that.scaleMarginLeft = function(newSlidesContainerWidth){
      if(
        that.currentSlidesContainerWidth &&
        that.currentSlidesContainerWidth !== newSlidesContainerWidth
      ){
        that.currentMarginLeftValue = that.currentMarginLeftValue * newSlidesContainerWidth;
        that.currentMarginLeftValue = that.currentMarginLeftValue / that.currentSlidesContainerWidth;
        that.disableTransitions();
        that.applyMarginLeft();
        that.enableTransitions();
      }
    };

    that.restartFromFirstItem = function() {
      if (!that.currentWidth) {
        return;
      }
      that.disableTransitions();
      that.currentMarginLeftValue = that.currentWidth * -1;
      that.applyMarginLeft();
      that.currentIndex = 0;
      that.radioButtonIndex = that.currentIndex;
      that.enableTransitions();
    };

    that.executeCloneData = function() {
      var cloneArray = [];
      for (var index = 0; index < that.data.length; index++) {
        var item = that.data[index];
        cloneArray.push(item);
      }
      that.cloneFirstItem(cloneArray);
      that.cloneLastItem(cloneArray);
      that.cloneData = cloneArray;
    };

    that.cloneFirstItem = function(cloneArray) {
      var firstItem = cloneArray[0];
      var firstItemClone = angular.copy(firstItem);
      cloneArray.push(firstItemClone);
    };

    that.cloneLastItem = function(cloneArray) {
      var lastItem = cloneArray[that.data.length - 1];
      var lastItemClone = angular.copy(lastItem);
      cloneArray.unshift(lastItemClone);
    };

    that.validateAutoSlide = function() {
      if( typeof(that.autoSlide) === 'string' ){
        that.autoSlide = that.autoSlide === 'true' ? true : false;
      }
      if (!that.autoSlide) {
        that.stopAutoSlide();
      } else {
        that.startAutoSlide();
      }
    };
	
    that.validateAutoSlideStopOnAction = function() { 
      if( typeof(that.autoSlideStopOnAction) === 'string' ){ 
        that.autoSlideStopOnAction = that.autoSlideStopOnAction === 'true' ? true : false; 
      } 
    }; 
 	
    that.restartAutoSlide = function() {
      if (!that.autoSlide) {
        return;
      }
      if (that.transitionsEnabled) {
        $timeout(function() {
          that.stopAutoSlide();
          that.startAutoSlide();
        }, that.transitionsTime);
      } else {
        that.stopAutoSlide();
        that.startAutoSlide();
      }
    };

    that.startAutoSlide = function() {
      if (!angular.isDefined(that.autoSlideInterval)) {
        that.autoSlideInterval = $interval(function() {
          that.navigateRight(true);
        }, that.autoSlideTime);
      }
    };

    that.stopAutoSlide = function() {
      if (angular.isDefined(that.autoSlideInterval)) {
        $interval.cancel(that.autoSlideInterval);
        that.autoSlideInterval = undefined;
      }
    };

    that.onNavigateLeft = function() {
      that.navigateLeft();
      that.restartAutoSlide();
    };

    that.navigateLeft = function() {
      if (that.isDataInvalidOrTooSmall()) {
        return;
      }
      that.currentIndex--;
      that.radioButtonIndex = that.currentIndex;
      that.currentMarginLeftValue += that.currentWidth;
      that.applyMarginLeft();
      if (that.autoSlideStopOnAction) { 
        that.stopAutoSlide(); 
      } else {         
        that.restartAutoSlide(); 
      }
      if (that.currentIndex === -1) {
        that.restartFromLastItem();
      }
    };

    that.restartFromLastItem = function() {
      $timeout(function() {
        that.disableTransitions();
        that.currentMarginLeftValue = (that.currentWidth * that.data.length) * -1;
        that.applyMarginLeft();
        that.currentIndex = that.data.length - 1;
        that.radioButtonIndex = that.currentIndex;
        that.enableTransitions();
      }, that.transitionsTime);
    };

    that.onNavigateRight = function() {
      that.navigateRight();
      that.restartAutoSlide();
    };

    that.navigateRight = function(autoSlide) {
      if (that.isDataInvalidOrTooSmall()) {
        return;
      }
      that.currentIndex++;
      that.radioButtonIndex = that.currentIndex;
      that.currentMarginLeftValue -= that.currentWidth;
      that.applyMarginLeft();
      if (!autoSlide && that.autoSlideStopOnAction) { 
        that.stopAutoSlide(); 
      } else {         
        that.restartAutoSlide(); 
      }
      if (that.currentIndex === that.data.length) {
        $timeout(function() {
          that.restartFromFirstItem();
        }, that.transitionsTime);
      }
    };

    that.applyMarginLeft = function() {
      that.slidesContainer.css('margin-left', that.currentMarginLeftValue + 'px');
    };

    that.disableTransitions = function() {
      that.slidesContainer.css('transition', 'none');
      that.transitionsEnabled = false;
    };

    that.enableTransitions = function() {
      $timeout(function() {
        that.slidesContainer.css('transition', 'margin 0.5s ease-in-out');
        that.transitionsEnabled = true;
      }, 200);
    };

    that.onRadioButtonClick = function() {
      var multiplier;
      if (that.radioButtonIndex > that.currentIndex) {
        multiplier = that.radioButtonIndex - that.currentIndex;
        that.currentMarginLeftValue -= (that.currentWidth * multiplier);
      } else {
        multiplier = that.currentIndex - that.radioButtonIndex;
        that.currentMarginLeftValue += (that.currentWidth * multiplier);
      }
      that.currentIndex = that.radioButtonIndex;
      that.applyMarginLeft();
      if (that.autoSlideStopOnAction) { 
        that.stopAutoSlide(); 
      } else {         
        that.restartAutoSlide(); 
      }         
	};

    that.isDataInvalidOrTooSmall = function() {
      if (!that.data || that.data.length <= 1) {
        return true;
      }
      return false;
    };
  }

  angular
    .module('jkAngularCarousel')
    .controller('JKCarouselController', [
      '$timeout', '$attrs', '$interval', '$window',
      CarouselController
    ]);

}());
