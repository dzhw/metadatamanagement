'use strict';

angular.module('metadatamanagementApp').config([
    '$translateProvider',

    function ($translateProvider) {
        var translations = {
            //jscs:disable
            'embargo-warning': {
                'data-package': {
                    'title-order-view': 'Data package "{{projectId}}" is subject to an embargo set by the data providers',
                    'title-order-view-expired': 'The data package "{{projectId}}" was subject to an embargo until {{date}}. The data package is currently being prepared for publication.',
                    'title-provider-view': 'Data package "{{projectId}}" is subject to an embargo set by the data providers',
                    'title-provider-view-expired': 'The data package "{{projectId}}" was subject to an embargo until {{date}}. The data package is currently being prepared for publication.',
                    'content-order-view': 'This data package is currently unavailable for order due to an embargo in place until {{date}}. The data package can only be published after this date. <b>Please note that the embargo date does not necessarily coincide with the expected release date.</b> The metadata presented here is preliminary and may be subject to changes until the final release. For further information regarding the release date, please contact <a href="mailto:userservice@dzhw.eu">userservice@dzhw.eu</a>. Once the data package is released, you will be able to add it to your shopping cart here.',
                    'content-order-view-expired': 'This data package is currently not yet available for order as it is still being prepared. Contact <a href="mailto:userservice@dzhw.eu">userservice@dzhw.eu</a> if you wish to receive information regarding the release date of the data package. As soon as the data package is released, you can put them in the shopping cart at this point.',
                    'content-provider-view': 'This data package is subject to an embargo until {{ date }}. Publication can only take place after this date. You can still edit the data package. Attention! Every saved change is <u>immediately visible to the public</u>.',
                    'content-provider-view-expired': 'This data package is currently preliminarily released. You can edit the data package until the final release. Attention! Every saved change is <u>immediately visible to the public</u>.'
                },
                'analysis-package': {
                    'title-order-view': 'Analysis package "{{projectId}}" is subject to an embargo set by the data providers',
                    'title-order-view-expired': 'The anaylsis package "{{projectId}}" was subject to an embargo until {{date}}. The analysis package is currently being prepared for publication.',
                    'title-provider-view': 'Analysis package "{{projectId}}" is subject to an embargo set by the data providers',
                    'title-provider-view-expired': 'The analysis package "{{projectId}}" was subject to an embargo until {{date}}. The analysis package is currently being prepared for publication.',
                    'content-order-view': 'This analysis package is currently unavailable for order due to an embargo in place until {{date}}. The analysis package can only be published after this date. <b>Please note that the embargo date does not necessarily coincide with the expected release date.</b> The metadata presented here is preliminary and may be subject to changes until the final release. For further information regarding the release date, please contact <a href="mailto:userservice@dzhw.eu">userservice@dzhw.eu</a>. Once the analysis package is released, you will be able to add it to your shopping cart here.',
                    'content-order-view-expired': 'This analysis package is currently not yet available for order as it is still being prepared. Contact <a href="mailto:userservice@dzhw.eu">userservice@dzhw.eu</a> if you wish to receive information regarding the release date of the analysis package. As soon as the analysis package is released, you can put them in the shopping cart at this point.',
                    'content-provider-view': 'This analysis package is subject to an embargo until {{ date }}. Publication can only take place after this date. You can still edit the analysis package. Attention! Every saved change is <u>immediately visible to the public</u>.',
                    'content-provider-view-expired': 'This analysis package is currently preliminarily released. You can edit the analysis package until the final release. Attention! Every saved change is <u>immediately visible to the public</u>.'
                }
              }
        }
        $translateProvider.translations('en', translations);
    }])