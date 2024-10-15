'use strict';

angular.module('metadatamanagementApp').config([
    '$translateProvider',

    function ($translateProvider) {
        var translations = {
            //jscs:disable
            'embargo-warning': {
                'data-package': {
                    'title-order-view': 'Data package "{{projectId}}" is subject to an embargo set by the data providers',
                    'title-provider-view': 'Data package "{{projectId}}" is subject to an embargo set by the data providers',
                    'content-order-view': 'This data package is currently not yet available for order as it is subject to an embargo until {{ date }}. Publication can only take place after this date. Please note that the embargo date does not necessarily correspond to the expected release date, please contact <a href="mailto:userservice@dzhw.eu">userservice@dzhw.eu</a> if you wish to receive information regarding the release date of the data package.',
                    'content-order-view-expired': 'This data package is currently not yet available for order as it is still being prepared. Contact <a href="mailto:userservice@dzhw.eu">userservice@dzhw.eu</a> if you wish to receive information regarding the release date of the data package.',
                    'content-provider-view': 'This data package is subject to an embargo until {{ date }}. Publication can only take place after this date. You can still edit the data package. Attention! Every saved change is <u>immediately visible to the public</u>.',
                    'content-provider-view-expired': 'This data package is currently preliminarily released. You can edit the data package until the final release. Attention! Every saved change is <u>immediately visible to the public</u>.'
                },
                'analysis-package': {
                    'title-order-view': 'Analysis package "{{projectId}}" is subject to an embargo set by the data providers',
                    'title-provider-view': 'Analysis package "{{projectId}}" is subject to an embargo set by the data providers',
                    'content-order-view': 'This analysis package is currently not yet available for order as it is subject to an embargo until {{ date }}. Publication can only take place after this date. Please note that the embargo date does not necessarily correspond to the expected release date, please contact <a href="mailto:userservice@dzhw.eu">userservice@dzhw.eu</a> if you wish to receive information regarding the release date of the analysis package.',
                    'content-order-view-expired': 'This analysis package is currently not yet available for order as it is still being prepared. Contact <a href="mailto:userservice@dzhw.eu">userservice@dzhw.eu</a> if you wish to receive information regarding the release date of the analysis package.',
                    'content-provider-view': 'This analysis package is subject to an embargo until {{ date }}. Publication can only take place after this date. You can still edit the analysis package. Attention! Every saved change is <u>immediately visible to the public</u>.',
                    'content-provider-view-expired': 'This analysis package is currently preliminarily released. You can edit the analysis package until the final release. Attention! Every saved change is <u>immediately visible to the public</u>.'
                }
              }
        }
        $translateProvider.translations('en', translations);
    }])