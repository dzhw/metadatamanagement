/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ApprovedUsageListSearchFilterController', [
    '$scope',
    function($scope) {
      
      $scope.getApprovedUsageList = function() {
        return [
          "SCIENTIFIC_USE",
          "TEACHING_PURPOSES",
          "NON_COMMERCIAL_USE",
          "COMMERCIAL_USE"
        ];
      }

      $scope.currentApprovedUsageList = {
        "SCIENTIFIC_USE": false,
        "TEACHING_PURPOSES": false,
        "NON_COMMERCIAL_USE": false,
        "COMMERCIAL_USE": false,
      }


      $scope.useAndLogic = false;

      // This is used in case a user provides an unsupported usage via url-param
      $scope.errorMsg = "";

      var init = function() {
        // if the search page is called with an url-param (e.g. "?approved-usage-list=SCIENTIFIC_USE"),
        // $scope.currentSearchParams.filter is not empty
        if ($scope.currentSearchParams.filter &&
            $scope.currentSearchParams.filter["approved-usage-list"]) {
          var errors = "";
          var usages = $scope.currentSearchParams.filter["approved-usage-list"].split("||")          
          for (const usage of usages) {
            if ($scope.currentApprovedUsageList.hasOwnProperty(usage)){
              $scope.currentApprovedUsageList[usage] = true;
            } else if (usage == "useAndLogic" || usage == "useOrLogic"){
              $scope.useAndLogic = usage == "useAndLogic" ? true : false;
            } else {
              errors += usage + " ";
            }
          }
          $scope.errorMsg = errors;
        }
      };

      $scope.onApprovedUsageListChanged = function() {
        $scope.errorMsg = "";
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }
        if (noApprovedUsageListSelected()) {
          delete $scope.currentSearchParams.filter["approved-usage-list"];
        } else {
          var usages = "";
          for (const usage in $scope.currentApprovedUsageList) {
            if ($scope.currentApprovedUsageList[usage]) {
              usages += usage + "||"
            }
          }
          // provide info about and/or-logic as last item
          usages += $scope.useAndLogic == true ? "useAndLogic" : "useOrLogic";
          $scope.currentSearchParams.filter["approved-usage-list"] = usages
        }        
        $scope.approvedUsageListChangedCallback();
      }

      $scope.getTranslationPathFromApprovedUsageId = function(id) {
        switch(id) {
          case 'SCIENTIFIC_USE':
            return "data-package-management.common.approvedUsage.scientificUse"
          case 'TEACHING_PURPOSES':
            return "data-package-management.common.approvedUsage.teachingPurposes"
          case 'NON_COMMERCIAL_USE':
            return "data-package-management.common.approvedUsage.nonCommercialUse"
          case 'COMMERCIAL_USE':
            return "data-package-management.common.approvedUsage.commercialUse"
        }
      }

      noApprovedUsageListSelected = function() {
        for (const usage in $scope.currentApprovedUsageList) {
          if ($scope.currentApprovedUsageList[usage]) {
            return false;
          }
        }
        return true;
      }

      $scope.$watch('currentSearchParams.filter.approved-usage-list', function() {
        init();
      });
    }
  ]);
