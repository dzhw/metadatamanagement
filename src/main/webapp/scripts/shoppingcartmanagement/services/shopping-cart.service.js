'use strict';

angular.module('metadatamanagementApp').service('ShoppingCartService',
    function($rootScope, localStorageService, StudyReferencedResource) {
      var basket = [];
      var getShoppingCart = function() {
        if (localStorageService.get('shoppingCart') === null) {
          localStorageService.set('shoppingCart', JSON.stringify([]));
        }
        basket = JSON.parse(localStorageService.get('shoppingCart'));
        return basket;
      };
      var searchInShoppingCart = function(item) {
        var search = {};
        for (var i = 0, len = basket.length; i < len; i++) {
          search[basket[i].id] = basket[i];
        }
        try {
          return search[item].id;
        } catch (e) {
          return 'notFound';
        }
      };
      var addToShoppingCart = function(studyId) {
        var markedStudies = getShoppingCart();
        if (searchInShoppingCart(studyId) === 'notFound') {
          StudyReferencedResource.getCustomStudy({id: studyId})
          .$promise.then(function(study) {
            markedStudies.push({
              id: studyId,
              text: study.title[$rootScope.currentLanguage],
              authors: study.authors,
              date:  new Date()
            });
            localStorageService
            .set('shoppingCart', JSON.stringify(markedStudies));
            $rootScope.$broadcast('itemsCount', markedStudies.length);
          });
        }
      };

      var removeFromShoppingCart = function(items) {
        var oldItems = items;
        basket.items = [];
        angular.forEach(oldItems, function(item) {
          if (!item.done) {
            basket.items.push(item);
          }
        });
        localStorageService.set('shoppingCart', JSON.stringify(basket.items));
        $rootScope.$broadcast('itemsCount', basket.items.length);
        return basket.items;
      };
      return {
        getShoppingCart: getShoppingCart,
        addToShoppingCart: addToShoppingCart,
        removeFromShoppingCart: removeFromShoppingCart
      };
    });
