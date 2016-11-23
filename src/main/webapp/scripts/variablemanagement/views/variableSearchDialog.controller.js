/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableSearchDialogController',
  function($mdDialog, $scope, blockUI, paramObject, VariableSearchService) {
    var ctrl = this;
    ctrl.paramObject = paramObject;
    var blockArea = blockUI.instances.get('blockRelatedVariableContainer');
    ctrl.infiniteItems = {
      numLoaded_: 0,
      toLoad_: 0,
      pageToLoad: 0,
      from: 0,
      size: 5,
      items: [],
      getItemAtIndex: function(index) {
                if (index > this.numLoaded_) {
                  this.fetchMoreItems_(index);
                  return null;
                }
                return this.items[index];
              },
      getLength: function() {
        if (this.numLoaded_ >= ctrl.count) {
          return this.items.length;
        }
        return this.items.length + this.size;
      },
      fetchMoreItems_: function(index) {
                if (this.toLoad_ < index) {
                  this.toLoad_ += this.size;
                  if (_.isArray(ctrl.paramObject.methodParams)) {
                    var searchTerms = _.chunk(ctrl.paramObject
                      .methodParams, this.size);
                    blockArea.start();
                    VariableSearchService[ctrl.paramObject.methodName]
                    (searchTerms[this.pageToLoad])
                    .then(angular.bind(this, function(variables) {
                          _.pullAllBy(variables.docs, [{'found': false}],
                          'found');
                          ctrl.count = variables.docs.length;
                          this.items = _.concat(this.items, variables.docs);
                          this.numLoaded_ = this.items.length;
                          this.pageToLoad += 1;
                        })).finally(function() {
                          blockArea.stop();
                        });
                  } else {
                    blockArea.start();
                    VariableSearchService[ctrl.paramObject.methodName](
                      ctrl.paramObject.methodParams, this.from, this.size,
                      ctrl.paramObject.variableId)
                      .then(angular.bind(this, function(variables) {
                        ctrl.count = variables.hits.total;
                        this.items = _.concat(this.items, variables.hits.hits);
                        this.numLoaded_ = this.items.length;
                        this.from += this.size;
                      })).finally(function() {
                        blockArea.stop();
                      });
                  }
                }
              }
    };
    ctrl.closeDialog = $mdDialog.hide;
    $scope.$on('$stateChangeStart', function() {
      ctrl.closeDialog();
    });
  });
