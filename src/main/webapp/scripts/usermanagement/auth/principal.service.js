/* globals _ */
'use strict';

angular.module('metadatamanagementApp').factory(
  'Principal',
  function Principal(Auth) {
    var displayWelcomeDialog = function(identity) {
      return _.indexOf(identity.authorities, 'ROLE_DATA_PROVIDER') !== -1 &&
        !identity.welcomeDialogDeactivated;
    };

    return {
      isAuthenticated: function() {
        return Auth.isAuthenticated();
      },
      hasAuthority: function(authority) {
        var accessToken = Auth.getAccessToken();
        if (!Auth.isAuthenticated() || !accessToken ||
          !accessToken.resource_access ||
          !accessToken.resource_access['mdm-frontend'] ||
          !accessToken.resource_access['mdm-frontend'].roles) {
          return false;
        }

        return (accessToken.resource_access['mdm-frontend'].roles.indexOf(
          authority) !== -1);
      },
      hasAnyAuthority: function(authorities) {
        var accessToken = Auth.getAccessToken();
        if (!Auth.isAuthenticated() || !accessToken ||
          !accessToken.resource_access ||
          !accessToken.resource_access['mdm-frontend'] ||
          !accessToken.resource_access['mdm-frontend'].roles) {
          return false;
        }

        for (var i = 0; i < authorities.length; i++) {
          if (accessToken.resource_access['mdm-frontend'].roles.indexOf(
            authorities[i]) !== -1) {
            return true;
          }
        }

        return false;
      },
      identity: function() {
        return Auth.getIdToken();
      },
      loginName: function() {
        return Auth.getIdToken() && Auth.getIdToken().preferred_username;
      }
    };
  });
