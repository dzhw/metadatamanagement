/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */
/* global module */
/* global _ */
'use strict';

describe('VariableBuilderService', function() {
  var dataSet = {
    'dataAcquisitionProjectId': 'projectwithdefaultconfiguration',
    'dataSetName': 'ds4',
    'dataSetNumber': '4',
    'dataSetIndex': 0,
    'jsonFiles': {
      'pid': {'path': 'ds4/pid.json'},
      'begin_m': {'path': 'ds4/begin_m.json'},
      'end_y': {'path': 'ds4/end_y.json'},
      'end_m': {'path': 'ds4/end_m.json'},
      'status': {'path': 'ds4/status.json'},
      'eid': {'path': 'ds4/eid.json'},
      'begin_y': {'path': 'ds4/begin_y.json'}
    }
  };

  var variableFromJson = {
    'scaleLevel': {
      'en': 'nominal',
      'de': 'nominal'
    },
    'label': {
      'en': 'Personal ID',
      'de': 'Personennummer'
    },
    'derivedVariablesIdentifier': null,
    'doNotDisplayThousandsSeparator': false,
    'dataType': {
      'en': 'numeric',
      'de': 'numerisch'
    },
    'filterDetails': {
      'expression': null,
      'description': {
        'en': null,
        'de': null
      },
      'expressionLanguage': null
    },
    'panelIdentifier': null,
    'accessWays': ['download-cuf', 'download-suf', 'remote-desktop-suf',
      'onsite-suf'],
    'surveyNumbers': [3, 4],
    'indexInDataSet': 0,
    'generationDetails': {
      'description': {
        'en': null,
        'de': null
      },
      'rule': null,
      'ruleExpressionLanguage': null
    },
    'storageType': 'integer',
    'distribution': {
      'totalValidRelativeFrequency': 100,
      'statistics': {
        'mode': 'multimodal'
      },
      'totalAbsoluteFrequency': 5473,
      'validResponses': null,
      'totalValidAbsoluteFrequency': 5473,
      'missings': null
    },
    'relatedQuestions': null,
    'relatedVariables': null,
    'annotations': {
      'en': null,
      'de': null
    }
  };

  var VariableBuilderService;

  var expectedAccessWays = ['download-cuf', 'download-suf',
    'remote-desktop-suf', 'onsite-suf'];
  var expectedSurveyIds = ['sur-projectwithdefaultconfiguration-sy3$',
    'sur-projectwithdefaultconfiguration-sy4$'];
  var expectedSurveyNumbers = [3, 4];

  beforeEach(module('metadatamanagementApp'));

  beforeEach(inject(function(_VariableBuilderService_) {
    VariableBuilderService = _VariableBuilderService_;
  }));

  it('should build a variable', function() {
    var variable = VariableBuilderService
      .buildVariable(variableFromJson, dataSet);
    expect(_.difference(variable.accessWays, expectedAccessWays).length)
      .toBe(0);
    expect(variable.dataAcquisitionProjectId)
      .toEqual('projectwithdefaultconfiguration');
    expect(variable.dataSetId)
      .toEqual('dat-projectwithdefaultconfiguration-ds4$');
    expect(variable.dataSetNumber).toEqual('4');
    expect(variable.dataType.en).toEqual('numeric');
    expect(variable.dataType.de).toEqual('numerisch');
    expect(variable.distribution.maxNumberOfDecimalPlaces).toBe(0);
    expect(variable.distribution.statistics.mode).toEqual('multimodal');
    expect(variable.distribution.totalAbsoluteFrequency).toBe(5473);
    expect(variable.distribution.totalValidAbsoluteFrequency).toBe(5473);
    expect(variable.distribution.totalValidRelativeFrequency).toBe(100);
    expect(variable.doNotDisplayThousandsSeparator).toBe(false);
    expect(variable.id)
      .toEqual('var-projectwithdefaultconfiguration-ds4-undefined$'); //TODO Why is it undefined?
    expect(variable.indexInDataSet).toBe(0);
    expect(variable.label.en).toEqual('Personal ID');
    expect(variable.label.de).toEqual('Personennummer');
    expect(variable.scaleLevel.en).toEqual('nominal');
    expect(variable.scaleLevel.de).toEqual('nominal');
    expect(variable.storageType).toEqual('integer');
    expect(variable.dataPackageId).toEqual('stu-projectwithdefaultconfiguration$');
    expect(_.difference(variable.surveyIds, expectedSurveyIds).length).toBe(0);
    expect(_.difference(variable.surveyNumbers, expectedSurveyNumbers).length)
      .toBe(0);
  });
});
