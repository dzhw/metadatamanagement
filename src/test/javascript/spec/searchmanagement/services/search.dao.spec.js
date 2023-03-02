/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */
/* global jasmine */
/* global module */
/* global _ */
'use strict';

describe("search.dao.js", function() {
  // search parameters
  var searchTerm = 'elasticsearch bloopers';
  var dataAcquisitionProjectId = 'ela2018';
  var SearchDao, ElasticSearchClient, Principal;
  var filter = {};
  var elasticSearchType = 'data_packages';
  var page = 1;
  var pageSize = 5;

  // variables injected into search.dao service
  var clientId;
  var SearchHelperService;

  // captured parameter for ElasticSearchClient#search
  var capturedQuery = null;

  beforeEach(function() {
    module('metadatamanagementApp');

    inject(function(_SearchDao_, _ElasticSearchClient_, _Principal_, _clientId_, _SearchHelperService_, _LanguageService_) {
      SearchDao = _SearchDao_;
      ElasticSearchClient = _ElasticSearchClient_;
      Principal = _Principal_;
      clientId = _clientId_;
      SearchHelperService = _SearchHelperService_;

      spyOn(ElasticSearchClient, 'search').and.callFake(function(query) {
        capturedQuery = query;
      });

      spyOn(_LanguageService_, 'getCurrentInstantly').and.returnValue('en');
    });
  });

  it('should always apply default query settings', function() {
    SearchDao.search(searchTerm, page, dataAcquisitionProjectId, filter, elasticSearchType, pageSize);

    expect(capturedQuery.preference).toEqual(clientId);
    expect(capturedQuery.index).toEqual(elasticSearchType);
    expect(capturedQuery.body._source).toBeDefined();
    expect(capturedQuery.body.sort).toBeDefined();
    expect(capturedQuery.body.from).toEqual((page - 1) * pageSize);
    expect(capturedQuery.body.size).toEqual(pageSize);
  });

  it('should add the searched term if provided', function() {
    SearchDao.search(searchTerm, page, dataAcquisitionProjectId, filter, elasticSearchType, pageSize);

    var predicate = {
      constant_score: {
        filter: {
          match: {
            all: {
              query: searchTerm,
              operator: 'AND',
              minimum_should_match: '100%',
              zero_terms_query: 'NONE',
              boost: 1
            }
          }
        }
      }
    };

    var generatedFilter = _.find(capturedQuery.body.query.bool.must, predicate);
    expect(generatedFilter).toBeDefined();
  });

  it('should add a "match_all" clause if no search term is provided', function() {
    SearchDao.search(undefined, page, dataAcquisitionProjectId, filter, elasticSearchType, pageSize);

    var predicate = {
      match_all: {}
    };

    var generatedFilter = _.find(capturedQuery.body.query.bool.must, predicate);

    expect(generatedFilter).toBeDefined();
  });

  describe('::dataAcquisitionProjectId filter', function() {
    var predicate = {
      bool: {
        should: [{
          term: {
            dataAcquisitionProjectId: dataAcquisitionProjectId
          }
        }, {
          term: {
            dataPackageIds: 'stu-' + dataAcquisitionProjectId + '$'
          }
        }]
      }
    };

    it('should add a "dataAcquisitionProjectId" filter if a project id is provided', function() {
      SearchDao.search(undefined, page, dataAcquisitionProjectId, filter, elasticSearchType, pageSize);

      var generatedFilter = _.find(capturedQuery.body.query.bool.filter, predicate);
      expect(generatedFilter).toBeDefined();
    });

    it('should not add a "dataAcquisitionProjectId" filter if no project id is provided', function() {
      SearchDao.search(undefined, page, undefined, filter, elasticSearchType, pageSize);

      var generatedFilter = _.find(capturedQuery.body.query.bool.filter, predicate);
      expect(generatedFilter).toBeUndefined();
    });
  });

  it('should exclude ids if specified', inject(function() {
    SearchDao.search(searchTerm, page, dataAcquisitionProjectId, filter, elasticSearchType, pageSize, 'ela2005');

    expect(capturedQuery.body.query.bool.must_not.terms.id).toEqual('ela2005');
  }));

  it('should add a type clause if an elasticsearch type is not provided', function() {
    SearchDao.search(searchTerm, page, dataAcquisitionProjectId, filter, null, pageSize);
    expect(capturedQuery.body.aggs.countByType.terms.field).toEqual('_index')
  });

  it('should not add a type clause if an elasticsearch type is provided', function() {
    SearchDao.search(searchTerm, page, dataAcquisitionProjectId, filter, elasticSearchType, pageSize);
    var result = _.get(capturedQuery, 'body.aggs.countByType.terms.field');
    expect(result).toBeUndefined();
  });

  describe('::release filter', function() {
    var predicate = {
      exists: {
        field: 'release'
      }
    };

    it('should add a release filter if is not a publisher, admin or data provider', function() {
      spyOn(Principal, 'hasAnyAuthority').and.returnValue(false);
      SearchDao.search(undefined, page, dataAcquisitionProjectId, filter, elasticSearchType, pageSize);

      var generatedFilter = _.find(capturedQuery.body.query.bool.filter, predicate);

      expect(generatedFilter).toBeDefined();
    });

    it('should not add release filter if user is a publisher, admin or data provider', function() {
      spyOn(Principal, 'hasAnyAuthority').and.returnValue(true);
      SearchDao.search(undefined, page, dataAcquisitionProjectId, filter, elasticSearchType, pageSize);

      var generatedFilter = _.find(capturedQuery.body.query.bool.filter, predicate);

      expect(generatedFilter).toBeUndefined();
    });
  });

  describe('::dataProvider filter', function() {
    var predicate = {
      'bool': {
        'must': [{
          'term': {'configuration.dataProviders': 'dataProviderUser'}
        }]
      }
    };

    it('should add a data provider filter if user is only a data provider', function() {
      spyOn(Principal, 'hasAnyAuthority').and.returnValue(false);
      spyOn(Principal, 'loginName').and.returnValue('dataProviderUser');
      spyOn(Principal, 'isProviderActive').and.returnValue(true);
      spyOn(Principal, 'showAllData').and.returnValue(false);

      SearchDao.search(undefined, page, dataAcquisitionProjectId, filter, elasticSearchType, pageSize);

      var generatedFilter = _.find(capturedQuery.body.query.bool.filter, predicate);
      console.log(capturedQuery);
      expect(generatedFilter).toBeDefined();
    });

    it('should not add a data provider filter if user is admin or publisher', function() {
      spyOn(Principal, 'hasAnyAuthority').and.returnValue(true);

      SearchDao.search(undefined, page, dataAcquisitionProjectId, filter, elasticSearchType, pageSize);

      var generatedFilter = _.find(capturedQuery.body.query.bool.filter, predicate);

      expect(generatedFilter).toBeUndefined();
    });
  });

  describe('::apply filter', function() {
    it('should apply default filters if some were given', function() {
      spyOn(SearchHelperService, 'createTermFilters').and.callThrough();
      var surveyFilter = {
        survey: 'sur-' + dataAcquisitionProjectId + '-sy1'
      };
      SearchDao.search(undefined, page, dataAcquisitionProjectId, surveyFilter, elasticSearchType, pageSize);

      expect(SearchHelperService.createTermFilters).toHaveBeenCalled();
    });
  });

  describe('::should query filters', function() {
    var searchTerm = 'a-search-term';

    var buildPredicate = function(fieldName, searchTerm) {
      var predicate = {
        constant_score: {
          filter: {
            match: {}
          }
        }
      };

      predicate.constant_score.filter.match[fieldName] = {
        query: searchTerm
      };
      return predicate;
    };

    var findQueryItem = function(capturedQuery, predicate) {
      return _.find(capturedQuery.body.query.bool.should, predicate);
    };

    it('should set "should" filters for "dataPackage" type', function() {
      SearchDao.search(searchTerm, page, dataAcquisitionProjectId, filter, 'data_packages', pageSize);

      expect(findQueryItem(capturedQuery, buildPredicate('title.de.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('title.en.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('surveyDesign.de.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('surveyDesign.en.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('surveyDataTypes.de.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('surveyDataTypes.en.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('id.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('projectContributors.firstName.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('projectContributors.middleName.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('projectContributors.lastName.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('description.de.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('description.en.ngrams', searchTerm))).toBeDefined();
    });

    it('should set "should" filters for "surveys" type', function() {
      SearchDao.search(searchTerm, page, dataAcquisitionProjectId, filter, 'surveys', pageSize);

      expect(findQueryItem(capturedQuery, buildPredicate('title.de.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('title.en.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('dataType.de.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('dataType.en.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('surveyMethod.de.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('surveyMethod.en.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('id.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('population.description.de.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('population.description.en.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('sample.de.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('sample.en.ngrams', searchTerm))).toBeDefined();
    });

    it('should set "should" filters for "instruments" type', function() {
      SearchDao.search(searchTerm, page, dataAcquisitionProjectId, filter, 'instruments', pageSize);

      expect(findQueryItem(capturedQuery, buildPredicate('description.de.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('description.en.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('id.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('type.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('title.de.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('title.en.ngrams', searchTerm))).toBeDefined();
    });

    it('should set "should" filters for "questions" type', function() {
      SearchDao.search(searchTerm, page, dataAcquisitionProjectId, filter, 'questions', pageSize);

      expect(findQueryItem(capturedQuery, buildPredicate('instrument.description.de.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('instrument.description.en.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('id.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('number.edge_ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('type.de.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('type.en.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('questionText.de.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('questionText.en.ngrams', searchTerm))).toBeDefined();
    });

    it('should set "should" filters for "data_sets" type', function() {
      SearchDao.search(searchTerm, page, dataAcquisitionProjectId, filter, 'data_sets', pageSize);

      expect(findQueryItem(capturedQuery, buildPredicate('description.de.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('description.en.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('id.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('type.de.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('type.en.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('surveys.title.de.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('surveys.title.en.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('subDataSets.accessWays.ngrams', searchTerm))).toBeDefined();
    });

    it('should set "should" filters for "variables" type', function() {
      SearchDao.search(searchTerm, page, dataAcquisitionProjectId, filter, 'variables', pageSize);

      expect(findQueryItem(capturedQuery, buildPredicate('label.de.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('label.en.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('name.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('id.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('dataType.de.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('dataType.en.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('scaleLevel.de.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('scaleLevel.en.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('surveys.title.de.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('surveys.title.en.ngrams', searchTerm))).toBeDefined();
    });

    it('should set "should" filters for "related_publications" type', function() {
      SearchDao.search(searchTerm, page, dataAcquisitionProjectId, filter, 'related_publications', pageSize);

      expect(findQueryItem(capturedQuery, buildPredicate('title.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('authors.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('id.ngrams', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('year', searchTerm))).toBeDefined();
      expect(findQueryItem(capturedQuery, buildPredicate('sourceReference.ngrams', searchTerm))).toBeDefined();
    });
  });
});
