/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetSearchDialogController',
    function($mdDialog, $scope, blockUI, paramObject, DataSetSearchService) {
      var ctrl = this;
      ctrl.paramObject = paramObject;
      var blockArea = blockUI.instances.get('blockRelatedDataSetContainer');
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
                      ctrl.count = ctrl.paramObject.methodParams.length;
                      blockArea.start();
                      DataSetSearchService[ctrl.paramObject.methodName]
                      (searchTerms[this.pageToLoad])
                      .then(angular.bind(this, function(dataSets) {
                            _.pullAllBy(dataSets.docs, [{'found': false}],
                            'found');
                            this.items = _.concat(this.items, dataSets.docs);
                            this.numLoaded_ = this.items.length;
                            this.pageToLoad += 1;
                          })).finally(function() {
                            blockArea.stop();
                          });
                    } else {
                      blockArea.start();
                      DataSetSearchService[ctrl.paramObject.methodName](
                        ctrl.paramObject.methodParams, this.from, this.size,
                        ctrl.paramObject.dataSetId)
                        .then(angular.bind(this, function(dataSets) {
                          ctrl.count = dataSets.hits.total;
                          this.items = _.concat(this.items, dataSets.hits.hits);
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
