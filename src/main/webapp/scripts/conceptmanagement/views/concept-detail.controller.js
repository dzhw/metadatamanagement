'use strict';

angular.module('metadatamanagementApp')
  .controller('ConceptDetailController',
    function(entity, PageTitleService, LanguageService,
             $state, ToolbarHeaderService, Principal,
             ConceptAttachmentResource, SearchResultNavigatorService,
             $stateParams, $log, blockUI) {
      blockUI.start();
      SearchResultNavigatorService
        .setSearchIndex($stateParams['search-result-index']);

      SearchResultNavigatorService.registerCurrentSearchResult();

      var getTags = function(concept) {
        if (concept.tags) {
          var language = LanguageService.getCurrentInstantly();
          return concept.tags[language];
        } else {
          return [];
        }
      };

      var isPublisherOrDataProvider = Principal.hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER']);

      var isLatestShadow = function(item) {
        return item.shadow && angular.isUndefined(item.successorId);
      };

      var isMaster = function(item) {
        return item.shadow === false
      };

      var ctrl = this;
      ctrl.isAuthenticated = Principal.isAuthenticated;
      ctrl.hasAuthority = Principal.hasAuthority;
      ctrl.searchResultIndex = SearchResultNavigatorService.getSearchIndex();
      ctrl.counts = {};
      ctrl.jsonExcludes = [
        'nestedStudies',
        'nestedDataSets',
        'nestedVariables',
        'nestedSurveys',
        'nestedQuestions',
        'nestedInstruments'
      ];
      ctrl.enableJsonView = Principal
        .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_ADMIN']);

      ctrl.loadAttachments = function() {
        ConceptAttachmentResource.findByConceptId({
          conceptId: ctrl.concept.id
        }).$promise.then(
          function(attachments) {
            if (attachments.length > 0) {
              ctrl.attachments = attachments;
            }
          });
      };

      entity.promise.then(function(result) {
        PageTitleService.setPageTitle('concept-management.detail.title', {
          title: result.title[LanguageService.getCurrentInstantly()],
          conceptId: result.id
        });
        ToolbarHeaderService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': result.id
        });

        var predicate = isPublisherOrDataProvider ? isMaster : isLatestShadow;

        result.studies = _.filter(result.studies, predicate);
        result.surveys = _.filter(result.surveys, predicate);
        result.dataSets = _.filter(result.dataSets, predicate);
        result.variables = _.filter(result.variables, predicate);
        result.questions = _.filter(result.questions, predicate);
        result.instruments = _.filter(result.instruments, predicate);

        ctrl.concept = result;
        ctrl.counts.studiesCount = result.studies.length;
        if (ctrl.counts.studiesCount === 1) {
          ctrl.study = result.studies[0];
        }
        ctrl.counts.surveysCount = result.surveys.length;
        if (ctrl.counts.surveysCount === 1) {
          ctrl.survey = result.surveys[0];
        }
        ctrl.counts.dataSetsCount = result.dataSets.length;
        if (ctrl.counts.dataSetsCount === 1) {
          ctrl.dataSet = result.dataSets[0];
        }
        ctrl.counts.variablesCount = result.variables.length;
        if (ctrl.counts.variablesCount === 1) {
          ctrl.variable = result.variables[0];
        }
        ctrl.counts.questionsCount = result.questions.length;
        if (ctrl.counts.questionsCount === 1) {
          ctrl.question = result.questions[0];
        }
        ctrl.counts.instrumentsCount = result.instruments.length;
        if (ctrl.counts.instrumentsCount === 1) {
          ctrl.instrument = result.instruments[0];
        }
        ctrl.loadAttachments();

        ctrl.conceptTags = getTags(result);
      }, $log.error).finally(blockUI.stop);

      ctrl.conceptEdit = function() {
        $state.go('conceptEdit', {id: ctrl.concept.id});
      };
    });
