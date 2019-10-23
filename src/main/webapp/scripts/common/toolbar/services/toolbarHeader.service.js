/*global _*/
'use strict';

angular.module('metadatamanagementApp').factory('ToolbarHeaderService',
  function($rootScope, $log) {
    var stripVersionSuffix = function(id) {
      if (!id) {
        return id;
      }
      var match = id.match(/-[0-9]+\.[0-9]+\.[0-9]+$/);
      if (match !== null) {
        return id.substr(0, match.index);
      } else {
        return id;
      }
    };
    var appendReleaseVersionToSurveys = function(surveys, version) {
      var clones = _.cloneDeep(surveys);
      return clones.map(function(survey) {
        survey.version = version;
        return survey;
      });
    };
    var translationStringsMap = {
      'conceptDetail': {
        'type': 'concept-management.detail.label.concept',
        'translateString': 'global.tooltips.toolbarHeader.concept',
        'iconType': 'svg',
        'icon': 'assets/images/icons/concept.svg'
      },
      'questionDetail': {
        'type': 'question-management.detail.label.question',
        'translateString': 'global.tooltips.toolbarHeader.question',
        'iconType': 'svg',
        'icon': 'assets/images/icons/question.svg'
      },
      'variableDetail': {
        'type': 'variable-management.detail.label.variable',
        'translateString': 'global.tooltips.toolbarHeader.variable',
        'iconType': 'svg',
        'icon': 'assets/images/icons/variable.svg'
      },
      'instrumentDetail': {
        'type': 'instrument-management.detail.label.instrument',
        'translateString': 'global.tooltips.toolbarHeader.instrument',
        'iconType': 'svg',
        'icon': 'assets/images/icons/instrument.svg'
      },
      'dataSetDetail': {
        'type': 'data-set-management.detail.label.data-set',
        'translateString': 'global.tooltips.toolbarHeader.data-set',
        'iconType': 'svg',
        'icon': 'assets/images/icons/data-set.svg'
      },
      'disclosure': {
        'type': 'disclosure.title'
      },
      'relatedPublicationDetail': {
        'type': 'related-publication-management.detail.label.publication',
        'translateString': 'global.tooltips.toolbarHeader.publication',
        'iconType': 'svg',
        'icon': 'assets/images/icons/related-publication.svg'
      },
      'search': {
        'type': 'search-management.detail.search',
        'translateString': 'global.tooltips.toolbarHeader.search',
        'tab': 'search-management.tabs.',
        'iconType': 'font',
        'icon': 'search'
      },
      'shoppingCart': {
        'type': 'shopping-cart.title'
      },
      'studyCreate': {
        'type': 'study-management.detail.label.study',
        'translateString': 'global.tooltips.toolbarHeader.study',
        'iconType': 'svg',
        'icon': 'assets/images/icons/study.svg'
      },
      'studyEdit': {
        'type': 'study-management.detail.label.study',
        'translateString': 'global.tooltips.toolbarHeader.study',
        'iconType': 'svg',
        'icon': 'assets/images/icons/study.svg'
      },
      'studyDetail': {
        'type': 'study-management.detail.label.study',
        'translateString': 'global.tooltips.toolbarHeader.study',
        'iconType': 'svg',
        'icon': 'assets/images/icons/study.svg'
      },
      'surveyDetail': {
        'type': 'survey-management.detail.label.survey',
        'types': 'survey-management.detail.label.surveys',
        'translateString': 'global.tooltips.toolbarHeader.survey',
        'translateStrings': 'global.tooltips.toolbarHeader.surveys',
        'iconType': 'svg',
        'icon': 'assets/images/icons/survey.svg'
      },
      'publicationAssignment': {
        'type': 'related-publication-management.assign.header'
      },
      'projectCockpit': {
        'type': 'data-acquisition-project-management.project-cockpit.header'
      },
      'projectOverview': {
        'type': 'data-acquisition-project-management.project-overview.header'
      },
      'login': {
        'type': 'global.toolbar.buttons.login'
      },
      'health': {
        'type': 'global.menu.admin.health'
      },
      'configuration': {
        'type': 'global.menu.admin.configuration'
      },
      'logs': {
        'type': 'global.menu.admin.logs'
      },
      'settings': {
        'type': 'global.menu.account.settings'
      },
      'password': {
        'type': 'global.menu.account.password'
      },
      'register': {
        'type': 'global.toolbar.buttons.register'
      },
      'activate': {
        'type': 'user-management.activate.title'
      },
      'requestReset': {
        'type': 'global.menu.account.password'
      },
      'finishReset': {
        'type': 'global.menu.account.password'
      },
      'user-management': {
        'type': 'global.menu.admin.user-management'
      },
      'user-management-detail': {
        'type': 'user-management.home.title'
      },
      'error': {
        'type': 'global.error.title'
      }
    };
    var searchItem = {
      item: {
        'type': translationStringsMap.search.type,
        'tooltip': translationStringsMap.search.translateString,
        'state': 'search({"page": 1, "size": 10})',
        'tabName': 'search-management.tabs.all',
        'iconType': translationStringsMap.search.iconType,
        'icon': translationStringsMap.search.icon
      },
      get: function() {
        return _.cloneDeep(this.item);
      },
      set: function(item) {
        this.item.type = translationStringsMap.search.type;
        this.item.tooltip = translationStringsMap.search.translateString;
        this.item.iconType = translationStringsMap.search.iconType;
        this.item.icon = translationStringsMap.search.icon;
        if (_.size(item.searchParams) > 1) {
          this.item.tabName = item.tabName;
        } else {
          this.item.tabName = 'search-management.tabs.all';
        }
        this.item.state = 'search(' + JSON.stringify(item.searchParams) +
        ')';
      },
      setCurrentPage: function(currentPage) {
        this.item.state = this.item.state
          .replace(/"page":"\d+"/, '"page":"' + currentPage + '"');
      }
    };
    var createRelatedStudyItem = function(item) {
      var studyItem = {
        'type': translationStringsMap.studyDetail.type,
        'iconType': translationStringsMap.studyDetail.iconType,
        'icon': translationStringsMap.studyDetail.icon
      };
      if (item.studyIsPresent) {
        var stateParams = {id: stripVersionSuffix(item.studyId)};
        if (item.version) {
          stateParams.version = item.version;
        }
        studyItem.state = 'studyDetail(' + JSON.stringify(stateParams) + ')';
        studyItem.tooltip = translationStringsMap.studyDetail.translateString;
        studyItem.projectId = stripVersionSuffix(item.projectId);
        studyItem.enableLastItem = item.enableLastItem;
      } else {
        studyItem.notFound = '?';
      }
      return studyItem;
    };
    var createRelatedInstrumentItem = function(item, type) {
      var instrumentItem = {
        'type': translationStringsMap.instrumentDetail.type,
        'iconType': translationStringsMap.instrumentDetail.iconType,
        'icon': translationStringsMap.instrumentDetail.icon
      };
      if (type === 'instrument') {
        instrumentItem.state = 'instrumentDetail';
        instrumentItem.tooltip = translationStringsMap.instrumentDetail.
        translateString;
        instrumentItem.number = item.number;
      } else {
        if (item.instrumentIsPresent) {
          var stateParams = {id: stripVersionSuffix(item.instrumentId)};
          if (item.version) {
            stateParams.version = item.version;
          }
          instrumentItem.state =
          'instrumentDetail(' + JSON.stringify(stateParams) + ')';
          instrumentItem.tooltip = translationStringsMap
          .instrumentDetail.translateString;
          instrumentItem.number = item.instrumentNumber;
          instrumentItem.enableLastItem = item.enableLastItem;
        } else {
          instrumentItem.notFound = '?';
        }
      }
      return instrumentItem;
    };
    var createRelatedDataSetItem = function(dataSet, type) {
      var dataSetItem = {
        'type': translationStringsMap.dataSetDetail.type,
        'iconType': translationStringsMap.dataSetDetail.iconType,
        'icon': translationStringsMap.dataSetDetail.icon
      };
      if (type === 'data-set') {
        dataSetItem.state = 'dataSetDetail';
        dataSetItem.tooltip = translationStringsMap.dataSetDetail
        .translateString;
        dataSetItem.number = dataSet.number;
      } else {
        if (dataSet.dataSetIsPresent) {
          var stateParams = {id: stripVersionSuffix(dataSet.dataSetId)};
          if (dataSet.version) {
            stateParams.version = dataSet.version;
          }
          dataSetItem.state =
          'dataSetDetail(' + JSON.stringify(stateParams) + ')';
          dataSetItem.tooltip = translationStringsMap
          .dataSetDetail.translateString;
          dataSetItem.number = dataSet.dataSetNumber;
          dataSetItem.enableLastItem = dataSet.enableLastItem;
        } else {
          dataSetItem.notFound = '?';
        }
      }
      return dataSetItem;
    };
    var createRelatedSurveyItem = function(surveys, itemTyp, itemId) {
      var stateParams = {};
      var surveyItem = {
        'iconType': translationStringsMap.surveyDetail.iconType,
        'icon': translationStringsMap.surveyDetail.icon
      };
      if (surveys.length === 1) {
        stateParams = {id: stripVersionSuffix(surveys[0].id)};
        if (surveys[0].version) {
          stateParams.version = surveys[0].version;
        }
        surveyItem.state = 'surveyDetail(' + JSON.stringify(stateParams) + ')';
        surveyItem.type = translationStringsMap.surveyDetail.type;
        surveyItem.tooltip = translationStringsMap.surveyDetail.
        translateString;
        surveyItem.number = surveys[0].number;
        surveyItem.enableLastItem = surveys[0].enableLastItem;
      }
      if (surveys.length > 1) {
        stateParams = {'type': 'surveys'};
        stateParams[itemTyp] = itemId;
        surveyItem.state = 'search(' + JSON.stringify(stateParams) + ')';
        surveyItem.type = translationStringsMap.surveyDetail.types;
        surveyItem.tooltip = translationStringsMap.surveyDetail.
        translateStrings;
        surveyItem.numbers = surveys.length > 2 ? surveys[0].number +
        ', ..., ' + _.last(surveys).number : surveys[0].number + ', ' +
        surveys[1].number;
      }
      if (surveys.length === 0) {
        surveyItem.type = translationStringsMap.surveyDetail.type;
        surveyItem.notFound = '?'; // survey undefined...
      }
      return surveyItem;
    };
    var createRelatedConceptItem = function(item) {
      var conceptItem = {
        enableLastItem: item.enableLastItem,
        tooltip: translationStringsMap.conceptDetail.translateString,
        iconType: translationStringsMap.conceptDetail.iconType,
        icon: translationStringsMap.conceptDetail.icon,
        type: translationStringsMap.conceptDetail.type
      };

      if (item.id) {
        var stateParams = {
          id: item.id
        };
        conceptItem.id = item.id;
        conceptItem.state = 'conceptDetail(' + JSON.stringify(stateParams) +
          ')';
      } else {
        conceptItem.notFound = '?';
      }

      return conceptItem;
    };
    var updateToolbarHeader = function(item) {
      $rootScope.toolbarHeaderItems = [];
      var instrumentItem = {};
      var questionItem = {};
      var dataSetItem = {};
      var variableItem = {};
      var publicationItem = {};
      var surveyItem = {};
      var studyItem = createRelatedStudyItem(item);
      switch (item.stateName) {
        case 'studyDetail':
          $rootScope.toolbarHeaderItems.push(searchItem.get(), studyItem);
          break;
        case 'studyEdit':
          $rootScope.toolbarHeaderItems.push(searchItem.get(), studyItem);
          break;
        case 'studyCreate':
          $rootScope.toolbarHeaderItems.push(searchItem.get(), studyItem);
          break;
        case 'questionDetail':
          questionItem = {
            'state': 'questionDetail',
            'type': translationStringsMap.questionDetail.type,
            'tooltip': translationStringsMap.questionDetail.translateString,
            'iconType': translationStringsMap.instrumentDetail.iconType,
            'icon': translationStringsMap.questionDetail.icon,
            'number': item.questionNumber
          };
          surveyItem = createRelatedSurveyItem(
            appendReleaseVersionToSurveys(item.surveys, item.version),
            'question', item.id);
          instrumentItem = createRelatedInstrumentItem(item, 'question');
          $rootScope.toolbarHeaderItems.push(searchItem.get(), studyItem,
          surveyItem, instrumentItem, questionItem);
          break;
        case 'variableDetail':
          variableItem = {
            'state': 'variableDetail',
            'type': translationStringsMap.variableDetail.type,
            'tooltip': translationStringsMap.variableDetail.translateString,
            'iconType': translationStringsMap.instrumentDetail.iconType,
            'icon': translationStringsMap.variableDetail.icon,
            'name': item.name
          };
          dataSetItem = createRelatedDataSetItem(item, 'variable');
          surveyItem = createRelatedSurveyItem(
            appendReleaseVersionToSurveys(item.surveys, item.version),
            'variable', item.id);
          $rootScope.toolbarHeaderItems.push(searchItem.get(), studyItem,
          surveyItem, dataSetItem, variableItem);
          break;
        case 'surveyDetail':
          surveyItem = createRelatedSurveyItem([item]);
          $rootScope.toolbarHeaderItems.push(searchItem.get(), studyItem,
          surveyItem);
          break;
        case 'surveyEdit':
          surveyItem = createRelatedSurveyItem([item]);
          $rootScope.toolbarHeaderItems.push(searchItem.get(), studyItem,
          surveyItem);
          break;
        case 'surveyCreate':
          surveyItem = createRelatedSurveyItem([item]);
          $rootScope.toolbarHeaderItems.push(searchItem.get(), studyItem,
          surveyItem);
          break;
        case 'instrumentEdit':
          instrumentItem = createRelatedInstrumentItem(item);
          surveyItem = createRelatedSurveyItem([], 'instrument',
          item.id);
          $rootScope.toolbarHeaderItems.push(searchItem.get(), studyItem,
          surveyItem, instrumentItem);
          break;
        case 'instrumentCreate':
          instrumentItem = createRelatedInstrumentItem(item);
          surveyItem = createRelatedSurveyItem([], 'instrument',
          item.id);
          $rootScope.toolbarHeaderItems.push(searchItem.get(), studyItem,
            surveyItem, instrumentItem);
          break;
        case 'dataSetDetail':
          dataSetItem = createRelatedDataSetItem(item, 'data-set');
          surveyItem = createRelatedSurveyItem(
            appendReleaseVersionToSurveys(item.surveys, item.version),
            'data-set', item.id);
          $rootScope.toolbarHeaderItems.push(searchItem.get(), studyItem,
          surveyItem, dataSetItem);
          break;
        case 'dataSetEdit':
          dataSetItem = createRelatedDataSetItem(item);
          surveyItem = createRelatedSurveyItem([], 'data-set',
          item.id);
          $rootScope.toolbarHeaderItems.push(searchItem.get(), studyItem,
          surveyItem, dataSetItem);
          break;
        case 'dataSetCreate':
          dataSetItem = createRelatedDataSetItem(item);
          surveyItem = createRelatedSurveyItem([], 'data-set',
          item.id);
          $rootScope.toolbarHeaderItems.push(searchItem.get(), studyItem,
          surveyItem, dataSetItem);
          break;
        case 'instrumentDetail':
          instrumentItem = createRelatedInstrumentItem(item, 'instrument');
          surveyItem = createRelatedSurveyItem(
            appendReleaseVersionToSurveys(item.surveys, item.version),
            'instrument', item.id);
          $rootScope.toolbarHeaderItems.push(searchItem.get(), studyItem,
            surveyItem, instrumentItem);
          break;
        case 'relatedPublicationDetail':
          publicationItem = {
            'state': 'relatedPublicationDetail',
            'type': translationStringsMap.relatedPublicationDetail.type,
            'tooltip': translationStringsMap.relatedPublicationDetail.
            translateString,
            'iconType': translationStringsMap.instrumentDetail.iconType,
            'icon': translationStringsMap.relatedPublicationDetail.icon,
            'id': item.id
          };
          $rootScope.toolbarHeaderItems.push(searchItem.get(), publicationItem);
          break;
        case 'publicationAssignment':
          var anyItem = {
            'state': 'publicationAssignment',
            'type': translationStringsMap.publicationAssignment.type
          };
          $rootScope.toolbarHeaderItems.push(anyItem);
          break;
        case 'search':
          searchItem.set(item);
          $rootScope.toolbarHeaderItems.push(searchItem.get());
          break;
        case 'shoppingCart':
        case 'restoreShoppingCart':
          var shoppingCartItem = {
            'state': 'shoppingCart',
            'type': translationStringsMap.shoppingCart.type
          };
          $rootScope.toolbarHeaderItems.push(shoppingCartItem);
          break;
        case 'disclosure':
          var disclosureItem = {
            'state': 'disclosure',
            'type': translationStringsMap.disclosure.type
          };
          $rootScope.toolbarHeaderItems.push(disclosureItem);
          break;
        case 'project-cockpit':
          var cockpitItem = {
            'state': 'project-cockpit',
            'type': translationStringsMap.projectCockpit.type
          };
          $rootScope.toolbarHeaderItems.push(cockpitItem);
          break;
        case 'project-overview': {
          var projectOverviewItem = {
            'state': 'project-overview',
            'type': translationStringsMap.projectOverview.type
          };
          $rootScope.toolbarHeaderItems.push(projectOverviewItem);
          break;
        }
        case 'user-management':
          var managementItem = {
            'state': 'user-management',
            'type': translationStringsMap['user-management'].type
          };
          $rootScope.toolbarHeaderItems.push(managementItem);
          break;
        case 'user-management-detail':
          var userDetailItem = {
            'state': 'user-management-detail',
            'type': translationStringsMap['user-management-detail'].type
          };
          $rootScope.toolbarHeaderItems.push(userDetailItem);
          break;
        case 'login':
          var loginItem = {
            'state': 'login',
            'type': translationStringsMap.login.type
          };
          $rootScope.toolbarHeaderItems.push(loginItem);
          break;
        case 'health':
          var healthItem = {
            'state': 'health',
            'type': translationStringsMap.health.type
          };
          $rootScope.toolbarHeaderItems.push(healthItem);
          break;
        case 'configuration':
          var configItem = {
            'state': 'configuration',
            'type': translationStringsMap.configuration.
            type
          };
          $rootScope.toolbarHeaderItems.push(configItem);
          break;
        case 'logs':
          var logsItem = {
            'state': 'logs',
            'type': translationStringsMap.logs.type
          };
          $rootScope.toolbarHeaderItems.push(logsItem);
          break;
        case 'settings':
          var settingsItem = {
            'state': 'settings',
            'type': translationStringsMap.settings.type
          };
          $rootScope.toolbarHeaderItems.push(settingsItem);
          break;
        case 'password':
          var passwordItem = {
            'state': 'password',
            'type': translationStringsMap.password.type
          };
          $rootScope.toolbarHeaderItems.push(passwordItem);
          break;
        case 'register':
          var registerItem = {
            'state': 'register',
            'type': translationStringsMap.register.type
          };
          $rootScope.toolbarHeaderItems.push(registerItem);
          break;
        case 'activate':
          var activateItem = {
            'state': 'activate',
            'type': translationStringsMap.activate.type
          };
          $rootScope.toolbarHeaderItems.push(activateItem);
          break;
        case 'finishReset':
          var finishResetItem = {
            'state': 'finishReset',
            'type': translationStringsMap.finishReset
            .type
          };
          $rootScope.toolbarHeaderItems.push(finishResetItem);
          break;
        case 'requestReset':
          var requestResetItem = {
            'state': 'requestReset',
            'type': translationStringsMap.requestReset.
            type
          };
          $rootScope.toolbarHeaderItems.push(requestResetItem);
          break;
        case 'error':
          var errorItem = {
            'state': 'error',
            'type': translationStringsMap.error.type
          };
          $rootScope.toolbarHeaderItems.push(errorItem);
          break;
        case 'conceptCreate':
          var itemCopy = _.extend({}, item);
          itemCopy.id = null;
          $rootScope.toolbarHeaderItems.push(searchItem.get(),
            createRelatedConceptItem(itemCopy));
          break;
        case 'conceptDetail':
        case 'conceptEdit':
          $rootScope.toolbarHeaderItems.push(searchItem.get(),
            createRelatedConceptItem(item));
          break;
        default:
          if (item.stateName) {
            $log.debug(item.stateName + ': coming...');
          } else {
            $log.warn('toolbarHeaderService: Given item has no state name ' +
              'unable to generate appropriate toolbar items');
          }
          break;
      }
    };

    var setCurrentSearchPage = function(currentPage) {
      if ($rootScope.toolbarHeaderItems &&
        $rootScope.toolbarHeaderItems.length > 0 &&
        $rootScope.toolbarHeaderItems[0].type ===
          translationStringsMap.search.type) {
        searchItem.setCurrentPage(currentPage);
        $rootScope.toolbarHeaderItems[0] = searchItem.get();
      }
    };

    return {
      updateToolbarHeader: updateToolbarHeader,
      setCurrentSearchPage: setCurrentSearchPage
    };
  });
