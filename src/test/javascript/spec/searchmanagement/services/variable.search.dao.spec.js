'use strict';

describe('VariableSearchDao with ElasticSearchClient service api', function () {

  var VariableSearchDao, ElasticSearchClient, data, query, queryWithQueryterm;
   beforeEach(function() {
     data = {
       hits: {
         hits:[
           {
             _index: 'metadata_de',
             _type: 'variables',
             _id: '564dd567ce1a0ed573dbda51',
             _score: 1,
             _source: {
               id: '564dd567ce1a0ed573dbda51',
               dataType: 'string',
               scaleLevel: 'nominal',
               label: 'Test_Mock_de_1',
               name: 'Test_de_1'
             }
           }
         ]
       }
     }
      query =  {
        index: 'metadata_de',
        type: 'variables',
        body: {
          query: {
            match_all: {  }
          },
          from: NaN,
          size: 10
        }
      };
      queryWithQueryterm =  {
        index: 'metadata_de',
        type: 'variables',
        body: {
          query: {
            bool: {
              should: [
                {
                  match: {
                    _all: {
                      query: 'queryterm',
                      type: 'boolean',
                      operator: 'AND',
                      zero_terms_query: 'NONE'
                    }
                  }
                }, {
                  match: {
                    allStringsAsNgrams: {
                      query: 'queryterm',
                      type: 'boolean',
                      operator: 'AND',
                      minimum_should_match: '100%',
                      zero_terms_query: 'NONE'
                    }
                  }
                }
              ]
            }
          },
          from: -10,
          size: 10
        }
      };
        module(function($provide) {
        $provide.value('ElasticSearchClient', {
          search: function(query) {
            return {
               then: function(callback){
                 return callback(data);
               }
            };
          }
        });
        return null;
      });
    });
    beforeEach(function() {
      inject(function(_VariableSearchDao_, _ElasticSearchClient_) {
        VariableSearchDao = _VariableSearchDao_;
        ElasticSearchClient = _ElasticSearchClient_;
      });
    });

     it('should call ElasticSearchClient', function() {
      spyOn(ElasticSearchClient, 'search').and.callThrough();
      VariableSearchDao.search(0);
      expect(ElasticSearchClient.search).toHaveBeenCalled();
    });

   it('should call ElasticSearchClient without queryterm', function() {
     spyOn(ElasticSearchClient, 'search').and.callThrough();
     VariableSearchDao.search(0);
     expect(ElasticSearchClient.search).toHaveBeenCalledWith(query);
   });

   it('should call ElasticSearchClient with queryterm', function() {
    spyOn(ElasticSearchClient, 'search').and.callThrough();
    VariableSearchDao.search('queryterm', 0);
    expect(ElasticSearchClient.search).toHaveBeenCalledWith(queryWithQueryterm);
  });

    it('should get Data from ElasticSearchClient', function() {
      spyOn(ElasticSearchClient, 'search').and.callThrough();
      var testVariables = function(variables) {
        expect(variables).toBe(data);
    };
      ElasticSearchClient.search().then(testVariables);
    });
});
