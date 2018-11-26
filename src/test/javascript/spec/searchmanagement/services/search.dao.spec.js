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
  var elasticSearchType = 'studies';
  var page = 1;
  var pageSize = 5;

  // variables injected into search.dao service
  var clientId;

  // captured parameter for ElasticSearchClient#search
  var capturedQuery = null;

  beforeEach(function() {
    module('metadatamanagementApp');

    inject(function(_SearchDao_, _ElasticSearchClient_, _Principal_, _clientId_) {
      SearchDao = _SearchDao_;
      ElasticSearchClient = _ElasticSearchClient_;
      Principal = _Principal_;
      clientId = _clientId_;

      spyOn(ElasticSearchClient, 'search').and.callFake(function(query) {
        capturedQuery = query;
      });
    });
  });

  it('should always apply default query settings', function() {
    SearchDao.search(searchTerm, page, dataAcquisitionProjectId, filter, elasticSearchType, pageSize);

    expect(capturedQuery.preference).toEqual(clientId);
    expect(capturedQuery.index).toEqual(elasticSearchType);
    expect(capturedQuery.type).toEqual(elasticSearchType);
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
            studyIds: 'stu-' + dataAcquisitionProjectId + '$'
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
    expect(capturedQuery.body.aggs.countByType.terms.field).toEqual('_type')
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

      SearchDao.search(undefined, page, dataAcquisitionProjectId, filter, elasticSearchType, pageSize);

      var generatedFilter = _.find(capturedQuery.body.query.bool.filter, predicate);

      expect(generatedFilter).toBeDefined();
    });

    it('should not add a data provider filter if user is admin or publisher', function() {
      spyOn(Principal, 'hasAnyAuthority').and.returnValue(true);

      SearchDao.search(undefined, page, dataAcquisitionProjectId, filter, elasticSearchType, pageSize);

      var generatedFilter = _.find(capturedQuery.body.query.bool.filter, predicate);

      expect(generatedFilter).toBeUndefined();
    });
  });
});
