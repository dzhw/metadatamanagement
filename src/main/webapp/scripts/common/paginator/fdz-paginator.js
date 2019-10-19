/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, { enumerable: true, get: getter });
/******/ 		}
/******/ 	};
/******/
/******/ 	// define __esModule on exports
/******/ 	__webpack_require__.r = function(exports) {
/******/ 		if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 			Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 		}
/******/ 		Object.defineProperty(exports, '__esModule', { value: true });
/******/ 	};
/******/
/******/ 	// create a fake namespace object
/******/ 	// mode & 1: value is a module id, require it
/******/ 	// mode & 2: merge all properties of value into the ns
/******/ 	// mode & 4: return value when already ns object
/******/ 	// mode & 8|1: behave like require
/******/ 	__webpack_require__.t = function(value, mode) {
/******/ 		if(mode & 1) value = __webpack_require__(value);
/******/ 		if(mode & 8) return value;
/******/ 		if((mode & 4) && typeof value === 'object' && value && value.__esModule) return value;
/******/ 		var ns = Object.create(null);
/******/ 		__webpack_require__.r(ns);
/******/ 		Object.defineProperty(ns, 'default', { enumerable: true, value: value });
/******/ 		if(mode & 2 && typeof value != 'string') for(var key in value) __webpack_require__.d(ns, key, function(key) { return value[key]; }.bind(null, key));
/******/ 		return ns;
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = "./src/index.ts");
/******/ })
/************************************************************************/
/******/ ({

/***/ "./src/fdzPaginatorComponent.ts":
/*!**************************************!*\
  !*** ./src/fdzPaginatorComponent.ts ***!
  \**************************************/
/*! exports provided: FdzPaginatorComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "FdzPaginatorComponent", function() { return FdzPaginatorComponent; });
/* harmony import */ var _fdzPaginatorComponentController__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./fdzPaginatorComponentController */ "./src/fdzPaginatorComponentController.ts");

var FdzPaginatorComponent = /** @class */ (function () {
    function FdzPaginatorComponent() {
        this.controller = _fdzPaginatorComponentController__WEBPACK_IMPORTED_MODULE_0__["FdzPaginatorComponentController"];
        this.bindings = {
            options: '<',
            onUpdate: '&?'
        };
        this.templateUrl = function ($attrs) {
            return $attrs.templateUrl || './fdzPaginatorTemplate.html';
        };
    }
    FdzPaginatorComponent.NAME = 'fdzPaginator';
    return FdzPaginatorComponent;
}());



/***/ }),

/***/ "./src/fdzPaginatorComponentController.ts":
/*!************************************************!*\
  !*** ./src/fdzPaginatorComponentController.ts ***!
  \************************************************/
/*! exports provided: FdzPaginatorComponentController */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "FdzPaginatorComponentController", function() { return FdzPaginatorComponentController; });
var FdzPaginatorComponentController = /** @class */ (function () {
    /* @ngInject */
    function FdzPaginatorComponentController() {
    }
    FdzPaginatorComponentController.prototype.$onInit = function () {
    };
    FdzPaginatorComponentController.prototype.previous = function () {
        if (this.options.pageObject.page > 1) {
            this.options.pageObject.page -= 1;
        }
        this.onUpdate({ $event: 'previous', $option: this.options });
    };
    ;
    FdzPaginatorComponentController.prototype.next = function () {
        if (this.options.pageObject.page < this.getPages()) {
            this.options.pageObject.page += 1;
        }
        this.onUpdate({ $event: 'next', $option: this.options });
    };
    ;
    FdzPaginatorComponentController.prototype.pageSize = function () {
        this.options.pageObject.page = 1;
        this.onUpdate({ $event: '', $option: this.options });
        this.calculateCurrentPage();
    };
    FdzPaginatorComponentController.prototype.calculateCurrentPage = function () {
        var currentPages = this.options.pageObject.page * this.options.pageObject.size;
        // this.options.pageObject.page =1;
        return (currentPages >= this.options.pageObject.totalHits) ? this.options.pageObject.totalHits : currentPages;
    };
    FdzPaginatorComponentController.prototype.getPages = function () {
        return this.options.pageObject.totalHits / this.options.pageObject.size;
    };
    FdzPaginatorComponentController.NAME = 'fdzPaginatorController';
    return FdzPaginatorComponentController;
}());



/***/ }),

/***/ "./src/index.ts":
/*!**********************!*\
  !*** ./src/index.ts ***!
  \**********************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _fdzPaginatorComponent__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./fdzPaginatorComponent */ "./src/fdzPaginatorComponent.ts");
/// <reference path="./globals.d.ts" />

// @ts-ignore
var fdzPaginator = angular
    .module('fdzPaginatorModule', [])
    .component(_fdzPaginatorComponent__WEBPACK_IMPORTED_MODULE_0__["FdzPaginatorComponent"].NAME, new _fdzPaginatorComponent__WEBPACK_IMPORTED_MODULE_0__["FdzPaginatorComponent"]());
/* harmony default export */ __webpack_exports__["default"] = (fdzPaginator);


/***/ })

/******/ });
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIndlYnBhY2s6Ly8vd2VicGFjay9ib290c3RyYXAiLCJ3ZWJwYWNrOi8vLy4vc3JjL2ZkelBhZ2luYXRvckNvbXBvbmVudC50cyIsIndlYnBhY2s6Ly8vLi9zcmMvZmR6UGFnaW5hdG9yQ29tcG9uZW50Q29udHJvbGxlci50cyIsIndlYnBhY2s6Ly8vLi9zcmMvaW5kZXgudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IjtRQUFBO1FBQ0E7O1FBRUE7UUFDQTs7UUFFQTtRQUNBO1FBQ0E7UUFDQTtRQUNBO1FBQ0E7UUFDQTtRQUNBO1FBQ0E7UUFDQTs7UUFFQTtRQUNBOztRQUVBO1FBQ0E7O1FBRUE7UUFDQTtRQUNBOzs7UUFHQTtRQUNBOztRQUVBO1FBQ0E7O1FBRUE7UUFDQTtRQUNBO1FBQ0EsMENBQTBDLGdDQUFnQztRQUMxRTtRQUNBOztRQUVBO1FBQ0E7UUFDQTtRQUNBLHdEQUF3RCxrQkFBa0I7UUFDMUU7UUFDQSxpREFBaUQsY0FBYztRQUMvRDs7UUFFQTtRQUNBO1FBQ0E7UUFDQTtRQUNBO1FBQ0E7UUFDQTtRQUNBO1FBQ0E7UUFDQTtRQUNBO1FBQ0EseUNBQXlDLGlDQUFpQztRQUMxRSxnSEFBZ0gsbUJBQW1CLEVBQUU7UUFDckk7UUFDQTs7UUFFQTtRQUNBO1FBQ0E7UUFDQSwyQkFBMkIsMEJBQTBCLEVBQUU7UUFDdkQsaUNBQWlDLGVBQWU7UUFDaEQ7UUFDQTtRQUNBOztRQUVBO1FBQ0Esc0RBQXNELCtEQUErRDs7UUFFckg7UUFDQTs7O1FBR0E7UUFDQTs7Ozs7Ozs7Ozs7OztBQ2xGQTtBQUFBO0FBQUE7QUFBb0Y7QUFDcEY7QUFDQTtBQUNBLDBCQUEwQixnR0FBK0I7QUFDekQ7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSxDQUFDO0FBQ2dDOzs7Ozs7Ozs7Ozs7O0FDZmpDO0FBQUE7QUFBQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLHVCQUF1Qiw0Q0FBNEM7QUFDbkU7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0EsdUJBQXVCLHdDQUF3QztBQUMvRDtBQUNBO0FBQ0E7QUFDQTtBQUNBLHVCQUF1QixvQ0FBb0M7QUFDM0Q7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0EsQ0FBQztBQUMwQzs7Ozs7Ozs7Ozs7OztBQ3BDM0M7QUFBQTtBQUFBO0FBQ2dFO0FBQ2hFO0FBQ0E7QUFDQTtBQUNBLGVBQWUsNEVBQXFCLFdBQVcsNEVBQXFCO0FBQ3JELDJFQUFZLEVBQUMiLCJmaWxlIjoiZmR6LXBhZ2luYXRvci5qcyIsInNvdXJjZXNDb250ZW50IjpbIiBcdC8vIFRoZSBtb2R1bGUgY2FjaGVcbiBcdHZhciBpbnN0YWxsZWRNb2R1bGVzID0ge307XG5cbiBcdC8vIFRoZSByZXF1aXJlIGZ1bmN0aW9uXG4gXHRmdW5jdGlvbiBfX3dlYnBhY2tfcmVxdWlyZV9fKG1vZHVsZUlkKSB7XG5cbiBcdFx0Ly8gQ2hlY2sgaWYgbW9kdWxlIGlzIGluIGNhY2hlXG4gXHRcdGlmKGluc3RhbGxlZE1vZHVsZXNbbW9kdWxlSWRdKSB7XG4gXHRcdFx0cmV0dXJuIGluc3RhbGxlZE1vZHVsZXNbbW9kdWxlSWRdLmV4cG9ydHM7XG4gXHRcdH1cbiBcdFx0Ly8gQ3JlYXRlIGEgbmV3IG1vZHVsZSAoYW5kIHB1dCBpdCBpbnRvIHRoZSBjYWNoZSlcbiBcdFx0dmFyIG1vZHVsZSA9IGluc3RhbGxlZE1vZHVsZXNbbW9kdWxlSWRdID0ge1xuIFx0XHRcdGk6IG1vZHVsZUlkLFxuIFx0XHRcdGw6IGZhbHNlLFxuIFx0XHRcdGV4cG9ydHM6IHt9XG4gXHRcdH07XG5cbiBcdFx0Ly8gRXhlY3V0ZSB0aGUgbW9kdWxlIGZ1bmN0aW9uXG4gXHRcdG1vZHVsZXNbbW9kdWxlSWRdLmNhbGwobW9kdWxlLmV4cG9ydHMsIG1vZHVsZSwgbW9kdWxlLmV4cG9ydHMsIF9fd2VicGFja19yZXF1aXJlX18pO1xuXG4gXHRcdC8vIEZsYWcgdGhlIG1vZHVsZSBhcyBsb2FkZWRcbiBcdFx0bW9kdWxlLmwgPSB0cnVlO1xuXG4gXHRcdC8vIFJldHVybiB0aGUgZXhwb3J0cyBvZiB0aGUgbW9kdWxlXG4gXHRcdHJldHVybiBtb2R1bGUuZXhwb3J0cztcbiBcdH1cblxuXG4gXHQvLyBleHBvc2UgdGhlIG1vZHVsZXMgb2JqZWN0IChfX3dlYnBhY2tfbW9kdWxlc19fKVxuIFx0X193ZWJwYWNrX3JlcXVpcmVfXy5tID0gbW9kdWxlcztcblxuIFx0Ly8gZXhwb3NlIHRoZSBtb2R1bGUgY2FjaGVcbiBcdF9fd2VicGFja19yZXF1aXJlX18uYyA9IGluc3RhbGxlZE1vZHVsZXM7XG5cbiBcdC8vIGRlZmluZSBnZXR0ZXIgZnVuY3Rpb24gZm9yIGhhcm1vbnkgZXhwb3J0c1xuIFx0X193ZWJwYWNrX3JlcXVpcmVfXy5kID0gZnVuY3Rpb24oZXhwb3J0cywgbmFtZSwgZ2V0dGVyKSB7XG4gXHRcdGlmKCFfX3dlYnBhY2tfcmVxdWlyZV9fLm8oZXhwb3J0cywgbmFtZSkpIHtcbiBcdFx0XHRPYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgbmFtZSwgeyBlbnVtZXJhYmxlOiB0cnVlLCBnZXQ6IGdldHRlciB9KTtcbiBcdFx0fVxuIFx0fTtcblxuIFx0Ly8gZGVmaW5lIF9fZXNNb2R1bGUgb24gZXhwb3J0c1xuIFx0X193ZWJwYWNrX3JlcXVpcmVfXy5yID0gZnVuY3Rpb24oZXhwb3J0cykge1xuIFx0XHRpZih0eXBlb2YgU3ltYm9sICE9PSAndW5kZWZpbmVkJyAmJiBTeW1ib2wudG9TdHJpbmdUYWcpIHtcbiBcdFx0XHRPYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgU3ltYm9sLnRvU3RyaW5nVGFnLCB7IHZhbHVlOiAnTW9kdWxlJyB9KTtcbiBcdFx0fVxuIFx0XHRPYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgJ19fZXNNb2R1bGUnLCB7IHZhbHVlOiB0cnVlIH0pO1xuIFx0fTtcblxuIFx0Ly8gY3JlYXRlIGEgZmFrZSBuYW1lc3BhY2Ugb2JqZWN0XG4gXHQvLyBtb2RlICYgMTogdmFsdWUgaXMgYSBtb2R1bGUgaWQsIHJlcXVpcmUgaXRcbiBcdC8vIG1vZGUgJiAyOiBtZXJnZSBhbGwgcHJvcGVydGllcyBvZiB2YWx1ZSBpbnRvIHRoZSBuc1xuIFx0Ly8gbW9kZSAmIDQ6IHJldHVybiB2YWx1ZSB3aGVuIGFscmVhZHkgbnMgb2JqZWN0XG4gXHQvLyBtb2RlICYgOHwxOiBiZWhhdmUgbGlrZSByZXF1aXJlXG4gXHRfX3dlYnBhY2tfcmVxdWlyZV9fLnQgPSBmdW5jdGlvbih2YWx1ZSwgbW9kZSkge1xuIFx0XHRpZihtb2RlICYgMSkgdmFsdWUgPSBfX3dlYnBhY2tfcmVxdWlyZV9fKHZhbHVlKTtcbiBcdFx0aWYobW9kZSAmIDgpIHJldHVybiB2YWx1ZTtcbiBcdFx0aWYoKG1vZGUgJiA0KSAmJiB0eXBlb2YgdmFsdWUgPT09ICdvYmplY3QnICYmIHZhbHVlICYmIHZhbHVlLl9fZXNNb2R1bGUpIHJldHVybiB2YWx1ZTtcbiBcdFx0dmFyIG5zID0gT2JqZWN0LmNyZWF0ZShudWxsKTtcbiBcdFx0X193ZWJwYWNrX3JlcXVpcmVfXy5yKG5zKTtcbiBcdFx0T2JqZWN0LmRlZmluZVByb3BlcnR5KG5zLCAnZGVmYXVsdCcsIHsgZW51bWVyYWJsZTogdHJ1ZSwgdmFsdWU6IHZhbHVlIH0pO1xuIFx0XHRpZihtb2RlICYgMiAmJiB0eXBlb2YgdmFsdWUgIT0gJ3N0cmluZycpIGZvcih2YXIga2V5IGluIHZhbHVlKSBfX3dlYnBhY2tfcmVxdWlyZV9fLmQobnMsIGtleSwgZnVuY3Rpb24oa2V5KSB7IHJldHVybiB2YWx1ZVtrZXldOyB9LmJpbmQobnVsbCwga2V5KSk7XG4gXHRcdHJldHVybiBucztcbiBcdH07XG5cbiBcdC8vIGdldERlZmF1bHRFeHBvcnQgZnVuY3Rpb24gZm9yIGNvbXBhdGliaWxpdHkgd2l0aCBub24taGFybW9ueSBtb2R1bGVzXG4gXHRfX3dlYnBhY2tfcmVxdWlyZV9fLm4gPSBmdW5jdGlvbihtb2R1bGUpIHtcbiBcdFx0dmFyIGdldHRlciA9IG1vZHVsZSAmJiBtb2R1bGUuX19lc01vZHVsZSA/XG4gXHRcdFx0ZnVuY3Rpb24gZ2V0RGVmYXVsdCgpIHsgcmV0dXJuIG1vZHVsZVsnZGVmYXVsdCddOyB9IDpcbiBcdFx0XHRmdW5jdGlvbiBnZXRNb2R1bGVFeHBvcnRzKCkgeyByZXR1cm4gbW9kdWxlOyB9O1xuIFx0XHRfX3dlYnBhY2tfcmVxdWlyZV9fLmQoZ2V0dGVyLCAnYScsIGdldHRlcik7XG4gXHRcdHJldHVybiBnZXR0ZXI7XG4gXHR9O1xuXG4gXHQvLyBPYmplY3QucHJvdG90eXBlLmhhc093blByb3BlcnR5LmNhbGxcbiBcdF9fd2VicGFja19yZXF1aXJlX18ubyA9IGZ1bmN0aW9uKG9iamVjdCwgcHJvcGVydHkpIHsgcmV0dXJuIE9iamVjdC5wcm90b3R5cGUuaGFzT3duUHJvcGVydHkuY2FsbChvYmplY3QsIHByb3BlcnR5KTsgfTtcblxuIFx0Ly8gX193ZWJwYWNrX3B1YmxpY19wYXRoX19cbiBcdF9fd2VicGFja19yZXF1aXJlX18ucCA9IFwiXCI7XG5cblxuIFx0Ly8gTG9hZCBlbnRyeSBtb2R1bGUgYW5kIHJldHVybiBleHBvcnRzXG4gXHRyZXR1cm4gX193ZWJwYWNrX3JlcXVpcmVfXyhfX3dlYnBhY2tfcmVxdWlyZV9fLnMgPSBcIi4vc3JjL2luZGV4LnRzXCIpO1xuIiwiaW1wb3J0IHsgRmR6UGFnaW5hdG9yQ29tcG9uZW50Q29udHJvbGxlciB9IGZyb20gJy4vZmR6UGFnaW5hdG9yQ29tcG9uZW50Q29udHJvbGxlcic7XG52YXIgRmR6UGFnaW5hdG9yQ29tcG9uZW50ID0gLyoqIEBjbGFzcyAqLyAoZnVuY3Rpb24gKCkge1xuICAgIGZ1bmN0aW9uIEZkelBhZ2luYXRvckNvbXBvbmVudCgpIHtcbiAgICAgICAgdGhpcy5jb250cm9sbGVyID0gRmR6UGFnaW5hdG9yQ29tcG9uZW50Q29udHJvbGxlcjtcbiAgICAgICAgdGhpcy5iaW5kaW5ncyA9IHtcbiAgICAgICAgICAgIG9wdGlvbnM6ICc8JyxcbiAgICAgICAgICAgIG9uVXBkYXRlOiAnJj8nXG4gICAgICAgIH07XG4gICAgICAgIHRoaXMudGVtcGxhdGVVcmwgPSBmdW5jdGlvbiAoJGF0dHJzKSB7XG4gICAgICAgICAgICByZXR1cm4gJGF0dHJzLnRlbXBsYXRlVXJsIHx8ICcuL2ZkelBhZ2luYXRvclRlbXBsYXRlLmh0bWwnO1xuICAgICAgICB9O1xuICAgIH1cbiAgICBGZHpQYWdpbmF0b3JDb21wb25lbnQuTkFNRSA9ICdmZHpQYWdpbmF0b3InO1xuICAgIHJldHVybiBGZHpQYWdpbmF0b3JDb21wb25lbnQ7XG59KCkpO1xuZXhwb3J0IHsgRmR6UGFnaW5hdG9yQ29tcG9uZW50IH07XG4iLCJ2YXIgRmR6UGFnaW5hdG9yQ29tcG9uZW50Q29udHJvbGxlciA9IC8qKiBAY2xhc3MgKi8gKGZ1bmN0aW9uICgpIHtcbiAgICAvKiBAbmdJbmplY3QgKi9cbiAgICBmdW5jdGlvbiBGZHpQYWdpbmF0b3JDb21wb25lbnRDb250cm9sbGVyKCkge1xuICAgIH1cbiAgICBGZHpQYWdpbmF0b3JDb21wb25lbnRDb250cm9sbGVyLnByb3RvdHlwZS4kb25Jbml0ID0gZnVuY3Rpb24gKCkge1xuICAgIH07XG4gICAgRmR6UGFnaW5hdG9yQ29tcG9uZW50Q29udHJvbGxlci5wcm90b3R5cGUucHJldmlvdXMgPSBmdW5jdGlvbiAoKSB7XG4gICAgICAgIGlmICh0aGlzLm9wdGlvbnMucGFnZU9iamVjdC5wYWdlID4gMSkge1xuICAgICAgICAgICAgdGhpcy5vcHRpb25zLnBhZ2VPYmplY3QucGFnZSAtPSAxO1xuICAgICAgICB9XG4gICAgICAgIHRoaXMub25VcGRhdGUoeyAkZXZlbnQ6ICdwcmV2aW91cycsICRvcHRpb246IHRoaXMub3B0aW9ucyB9KTtcbiAgICB9O1xuICAgIDtcbiAgICBGZHpQYWdpbmF0b3JDb21wb25lbnRDb250cm9sbGVyLnByb3RvdHlwZS5uZXh0ID0gZnVuY3Rpb24gKCkge1xuICAgICAgICBpZiAodGhpcy5vcHRpb25zLnBhZ2VPYmplY3QucGFnZSA8IHRoaXMuZ2V0UGFnZXMoKSkge1xuICAgICAgICAgICAgdGhpcy5vcHRpb25zLnBhZ2VPYmplY3QucGFnZSArPSAxO1xuICAgICAgICB9XG4gICAgICAgIHRoaXMub25VcGRhdGUoeyAkZXZlbnQ6ICduZXh0JywgJG9wdGlvbjogdGhpcy5vcHRpb25zIH0pO1xuICAgIH07XG4gICAgO1xuICAgIEZkelBhZ2luYXRvckNvbXBvbmVudENvbnRyb2xsZXIucHJvdG90eXBlLnBhZ2VTaXplID0gZnVuY3Rpb24gKCkge1xuICAgICAgICB0aGlzLm9wdGlvbnMucGFnZU9iamVjdC5wYWdlID0gMTtcbiAgICAgICAgdGhpcy5vblVwZGF0ZSh7ICRldmVudDogJycsICRvcHRpb246IHRoaXMub3B0aW9ucyB9KTtcbiAgICAgICAgdGhpcy5jYWxjdWxhdGVDdXJyZW50UGFnZSgpO1xuICAgIH07XG4gICAgRmR6UGFnaW5hdG9yQ29tcG9uZW50Q29udHJvbGxlci5wcm90b3R5cGUuY2FsY3VsYXRlQ3VycmVudFBhZ2UgPSBmdW5jdGlvbiAoKSB7XG4gICAgICAgIHZhciBjdXJyZW50UGFnZXMgPSB0aGlzLm9wdGlvbnMucGFnZU9iamVjdC5wYWdlICogdGhpcy5vcHRpb25zLnBhZ2VPYmplY3Quc2l6ZTtcbiAgICAgICAgLy8gdGhpcy5vcHRpb25zLnBhZ2VPYmplY3QucGFnZSA9MTtcbiAgICAgICAgcmV0dXJuIChjdXJyZW50UGFnZXMgPj0gdGhpcy5vcHRpb25zLnBhZ2VPYmplY3QudG90YWxIaXRzKSA/IHRoaXMub3B0aW9ucy5wYWdlT2JqZWN0LnRvdGFsSGl0cyA6IGN1cnJlbnRQYWdlcztcbiAgICB9O1xuICAgIEZkelBhZ2luYXRvckNvbXBvbmVudENvbnRyb2xsZXIucHJvdG90eXBlLmdldFBhZ2VzID0gZnVuY3Rpb24gKCkge1xuICAgICAgICByZXR1cm4gdGhpcy5vcHRpb25zLnBhZ2VPYmplY3QudG90YWxIaXRzIC8gdGhpcy5vcHRpb25zLnBhZ2VPYmplY3Quc2l6ZTtcbiAgICB9O1xuICAgIEZkelBhZ2luYXRvckNvbXBvbmVudENvbnRyb2xsZXIuTkFNRSA9ICdmZHpQYWdpbmF0b3JDb250cm9sbGVyJztcbiAgICByZXR1cm4gRmR6UGFnaW5hdG9yQ29tcG9uZW50Q29udHJvbGxlcjtcbn0oKSk7XG5leHBvcnQgeyBGZHpQYWdpbmF0b3JDb21wb25lbnRDb250cm9sbGVyIH07XG4iLCIvLy8gPHJlZmVyZW5jZSBwYXRoPVwiLi9nbG9iYWxzLmQudHNcIiAvPlxuaW1wb3J0IHsgRmR6UGFnaW5hdG9yQ29tcG9uZW50IH0gZnJvbSAnLi9mZHpQYWdpbmF0b3JDb21wb25lbnQnO1xuLy8gQHRzLWlnbm9yZVxudmFyIGZkelBhZ2luYXRvciA9IGFuZ3VsYXJcbiAgICAubW9kdWxlKCdmZHpQYWdpbmF0b3JNb2R1bGUnLCBbXSlcbiAgICAuY29tcG9uZW50KEZkelBhZ2luYXRvckNvbXBvbmVudC5OQU1FLCBuZXcgRmR6UGFnaW5hdG9yQ29tcG9uZW50KCkpO1xuZXhwb3J0IGRlZmF1bHQgZmR6UGFnaW5hdG9yO1xuIl0sInNvdXJjZVJvb3QiOiIifQ==