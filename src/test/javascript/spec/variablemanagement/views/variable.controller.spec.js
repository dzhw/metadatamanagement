'use strict';

xdescribe('VariableController search api', function () {

var $controller,controller, VariableSearchDao, createController, Variable,scope, data, $httpBackend, $location;
 beforeEach(function() {
   data = {
     hits: {
       total: 1,
       max_score: 1,
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
   };
    module(function($provide) {
      $provide.value('VariableSearchDao', {
        search: function(queryterm, pageNumber) {
          return {
             then: function(callback){
               return callback(data);
             }
          };
        }
      });
      return null;
    });
    module(function($provide) {
      $provide.value('Variable', {
        get: function() {
          scope.variable=data.hits.hits[0]._source;
        },
        delete:function(){

        }
      });
      return null;
    });
  });

  beforeEach(function() {
    inject(function($controller, $rootScope, _VariableSearchDao_,_$httpBackend_, _$location_,_Variable_) {
      scope = $rootScope.$new();
      $httpBackend = _$httpBackend_;
      $location = _$location_;
      Variable = _Variable_;
      VariableSearchDao = _VariableSearchDao_;
      createController = function() {
        return $controller('VariablesController', {
          $scope: scope,
          $location: $location
        });
      };
    });
    var globalJson = new RegExp('i18n\/.*\/global.json')
    var mainJson = new RegExp('i18n\/.*\/main.json');
    $httpBackend.whenGET(globalJson).respond({});
    $httpBackend.whenGET(mainJson).respond({});
    $httpBackend.expectGET(/api\/account\?cacheBuster=\d+/).respond(200, '');
  });
  describe('VariableController with VariableSearchDao service api', function() {
    beforeEach(function(){
      spyOn(VariableSearchDao, 'search').and.callThrough();
      $location.search('query','test query');
      createController();
    });
    it('should call VariableSearchDao', function() {
      expect(VariableSearchDao.search).toHaveBeenCalled();
    });
    it('should call VariableSearchDao with query and pageNumber', function() {
     expect(VariableSearchDao.search).toHaveBeenCalledWith('test query', 1);
   });
   it('should return one result', function() {
     var testVariables = function(variables) {
       expect(variables.hits.hits.length).toEqual(1);
   };
     VariableSearchDao.search().then(testVariables);
   });
   it('should get data from VariableSearchDao', function() {
     var testVariables = function(variables) {
       expect(variables.hits.hits[0]._source.id).toBe('564dd567ce1a0ed573dbda51');
       expect(variables.hits.hits[0]._source.dataType).toBe('string')
       expect(variables.hits.hits[0]._source.scaleLevel).toBe('nominal');
       expect(variables.hits.hits[0]._source.label).toBe('Test_Mock_de_1');
       expect(variables.hits.hits[0]._source.name).toBe('Test_de_1');
   };
     VariableSearchDao.search().then(testVariables);
   });

   describe('Page Object',function(){
     beforeEach(function(){
      VariableSearchDao.search();
     });
   it('should return default size', function() {
     expect(scope.page.size).toEqual(10);
   });
   it('should return content size', function() {
     expect(scope.page.contentSize).toEqual(1);
   });
   it('should return current page number', function() {
     expect(scope.page.currentPageNumber).toEqual(1);
   });
   it('should return number of total hits', function() {
     expect(scope.page.totalHits).toEqual(1);
   });
   it('should return total number of pages', function() {
     expect(scope.page.getTotalNumberOfPages()).toEqual(1);
   });
   it('should return minimum number of hits', function() {
     expect(scope.page.getMinHitNumber()).toEqual(1);
   });
   it('should return maximum number of hits', function() {
     expect(scope.page.getMaxHitNumber()).toEqual(1);
   });
   it('should return next page', function() {
     expect(scope.page.hasNextPage()).toBe(false);
   });
   it('should return previous page', function() {
     expect(scope.page.hasPreviousPage()).toBe(false);
   });
 });
 describe('Functions of VariableController',function(){
  /* beforeEach('',function(){
      modal = jasmine.createSpyObj('modal', ['show', 'hide']);
   });*/
   it('should execute a search when variable is created', function() {
      scope.$on('variable.created');
      expect(VariableSearchDao.search).toHaveBeenCalled();
   });
   it('should execute a search when variable is updated', function() {
      scope.$on('variable.updated');
      expect(VariableSearchDao.search).toHaveBeenCalled();
   });
   it('should execute a search when refresh view', function() {
      scope.refresh;
      expect(VariableSearchDao.search).toHaveBeenCalled();
   });
   it('should execute a search when clicked on next page', function() {
      scope.nextPage;
      expect(scope.page.currentPageNumber).toEqual(1);
      expect(VariableSearchDao.search).toHaveBeenCalled();
   });
   it('should execute a search when clicked on previous page', function() {
      scope.previousPage;
      expect(scope.page.currentPageNumber).toEqual(1);
      expect(VariableSearchDao.search).toHaveBeenCalled();
   });
   describe('Remove a variable', function(){
     beforeEach(function(){
       spyOn(Variable, 'get').and.callThrough();
       spyOn(Variable, 'delete').and.callThrough();
       scope.delete('564dd567ce1a0ed573dbda51');
     });
     it('should return index of variable when clicked on delete', function() {
       expect(Variable.get).toHaveBeenCalled();
       expect(scope.item._id).toBe('564dd567ce1a0ed573dbda51');
       expect(scope.itemtoBeRemoved).toEqual(0);
     });
     it('should remove the variable when clicked on confirme delete', function() {
        scope.confirmDelete('564dd567ce1a0ed573dbda51');
        expect(scope.page.contentSize).toEqual(0);
        expect(scope.page.totalHits).toEqual(0);
        expect(scope.searchResult.length).toEqual(0);
     });
   });
 });
});
});
