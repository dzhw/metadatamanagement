/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */
'use strict';

describe('Variable Detail Controller', function() {
  var $scope;
  var $rootScope;
  var MockEntity;
  var createController;
  var Language;
  var $translate;

  beforeEach(inject(function($injector) {
    $rootScope = $injector.get('$rootScope');
    Language = $injector.get('Language');
    $translate = $injector.get('$translate');
    $scope = $rootScope.$new();
    MockEntity = {
      $promise: {
        then: function(callback) {
          callback();
        }
      }
    };

    var locals = {
      '$scope': $scope,
      'entity': MockEntity,
      'Language': Language,
      '$translate': $translate
    };
    createController = function() {
      $injector.get('$controller')('VariableDetailController',
        locals);
    };
  }));

  it('should set $scope.variable', function() {
    createController();
    expect($scope.variable).toBeDefined();

  });

  it('check options without language', function() {
    //define variables
    createController();
    var d = {};
    d.relativeFrequency = 86.76;
    d.absoluteFrequency = 345;

    //relative
    expect($scope.optionsRelative.chart.valueFormat(123.456)).toBe(
      '123.46');
    expect($scope.optionsRelative.chart.y(d)).toBe(86.76);

    //absolute
    expect($scope.optionsAbsolute.chart.valueFormat(123.456)).toBe(
      '123.46');
    expect($scope.optionsAbsolute.chart.y(d)).toBe(345);
  });

  it('check options for box plot', function() {
    //define variables
    createController();
    var d = {};
    d.label = 'A Lable';
    d.values = {};
    d.values.Q3 = 110;

    //Assert
    expect($scope.options.chart.x(d)).toBe('A Lable');
    expect($scope.options.chart.y(d)).toBe(110);
  });

  it('check options with german', function() {
    createController();
    var currentLanguage = Language.getCurrentInstantly();
    Language.setCurrent('de');
    var d = {};
    d.label = {};

    //check for code
    d.code = 'A Code';
    expect($scope.optionsRelative.chart.x(d)).toBe('A Code');
    expect($scope.optionsAbsolute.chart.x(d)).toBe('A Code');

    //check for label
    d.label.de = 'german';
    expect($scope.optionsRelative.chart.x(d)).toBe('german');
    expect($scope.optionsAbsolute.chart.x(d)).toBe('german');

    Language.setCurrent(currentLanguage);
  });

  it('check options with english', function() {
    createController();
    var currentLanguage = Language.getCurrentInstantly();
    Language.setCurrent('en');
    var d = {};
    d.label = {};

    //check for code
    d.code = 'Another Code';
    expect($scope.optionsRelative.chart.x(d)).toBe('Another Code');
    expect($scope.optionsAbsolute.chart.x(d)).toBe('Another Code');

    //check for label
    d.label.en = 'english';
    expect($scope.optionsRelative.chart.x(d)).toBe('english');
    expect($scope.optionsAbsolute.chart.x(d)).toBe('english');

    Language.setCurrent(currentLanguage);
  });
});
