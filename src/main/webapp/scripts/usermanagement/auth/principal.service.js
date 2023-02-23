/* globals _ */
'use strict';

angular.module('metadatamanagementApp').factory(
  'Principal',
  function Principal($q, AccountResource, AuthServerProvider, $rootScope,
                     WelcomeDialogService, $sessionStorage) {
    var _identity;
    var _authenticated = false;
    
    // Attribut, ob der View gewechselt werden kann
    var _canSwitchViews = false;
    // Attribut ob provider view aktiv oder nicht
    var _providerViewActive;
    var _showProjectCockpitInSidenav = false;
    var _showAdminMenu = false;
    var _showProjectOverview = false;
    var _displayWelcomeDialog = false;

    var displayWelcomeDialog = function(identity) {
      return _.indexOf(identity.authorities, 'ROLE_DATA_PROVIDER') !== -1 &&
        !identity.welcomeDialogDeactivated;
    };

    var allowViewSwitch = function(identity) {
      if (identity !== null){
        return _.indexOf(identity.authorities, 'ROLE_DATA_PROVIDER') !== -1;
      }
      return false;
    };

    var showProjectCockpitInSidenav = function(identity) {
      if (identity !== null 
        && (_.indexOf(identity.authorities, 'ROLE_DATA_PROVIDER') !== -1 
        || _.indexOf(identity.authorities, 'ROLE_PUBLISHER') !== -1)){
        return true;
      }
      return false;
    }

    var showAdminMenu = function(identity) {
      if (identity !== null){
        return _.indexOf(identity.authorities, 'ROLE_ADMIN') !== -1;
      }
      return false;
    }

    var showProjectOverview = function(identity) {
      if (identity !== null && (_.indexOf(identity.authorities, 'ROLE_DATA_PROVIDER') !== -1 
      || _.indexOf(identity.authorities, 'ROLE_PUBLISHER') !== -1)){
        return true;
      }
      return false;
    }

    return {
      isIdentityResolved: function() {
        return angular.isDefined(_identity);
      },
      isAuthenticated: function() {
        return _authenticated;
      },
      hasAuthority: function(authority) {
        if (!_authenticated || !_identity || !_identity.authorities) {
          return false;
        }

        return (_identity.authorities.indexOf(authority) !== -1);
      },
      hasAnyAuthority: function(authorities) {
        if (!_authenticated || !_identity || !_identity.authorities) {
          return false;
        }

        for (var i = 0; i < authorities.length; i++) {
          if (_identity.authorities.indexOf(authorities[i]) !== -1) {
            return true;
          }
        }

        return false;
      },
      authenticate: function(identity) {
        _identity = identity;
        _authenticated = identity !== null;
        if (allowViewSwitch(_identity)){
          _canSwitchViews = true;
        } else {
          _canSwitchViews = false;
        } 
        if (_identity == null){
          localStorage.removeItem("currentView");
        }
      },
      identity: function(force) {
        var deferred = $q.defer();

        if (force === true) {
          _identity = undefined;
        }

        // check and see if we have retrieved the identity data from the
        // server.
        // if we have, reuse it by immediately resolving
        if (angular.isDefined(_identity)) {
          deferred.resolve(_identity);
          return deferred.promise;
        }

        // retrieve the identity data from the server, update the
        // identity object, and then resolve.
        if (AuthServerProvider.hasToken()) {
          $rootScope.$broadcast('start-ignoring-401');
          AccountResource.get().$promise.then(function(account) {
            $rootScope.$broadcast('stop-ignoring-401');
            _identity = account.data;
            _authenticated = true;
            if (allowViewSwitch(_identity)){
              _canSwitchViews = true;
            } else {
              _canSwitchViews = false;
            }
            // activate provider view for all authenticated users
            // wenn nur Dataprovider und nix höheres: providerView = false
            // wenn was höheres oder gar keiner: true
            if (localStorage.getItem('currentView') === null){
              if (_.indexOf(_identity.authorities, 'ROLE_DATA_PROVIDER') !== -1
                && _.indexOf(_identity.authorities, 'ROLE_PUBLISHER') === -1
                && _.indexOf(_identity.authorities, 'ROLE_ADMIN') === -1){
                _providerViewActive = false;
                localStorage.setItem('currentView', 'orderView');
              } else {
                _providerViewActive = true;
                localStorage.setItem('currentView', 'providerView');
              }
              $rootScope.$broadcast('view-changed', localStorage.getItem('currentView'));
            } else if (localStorage.getItem('currentView') === "orderView"){
              _providerViewActive = false;
            } else if (localStorage.getItem('currentView') === "providerView"){
              _providerViewActive = true;
            }
            $rootScope.$broadcast('view-changed', localStorage.getItem('currentView'));
              
            if (displayWelcomeDialog(_identity)) {
              WelcomeDialogService.display(_identity.login)
                .then(function(hideWelcomeDialog) {
                  if (hideWelcomeDialog) {
                    _identity.welcomeDialogDeactivated = true;
                    AccountResource.save(_identity);
                  }
                });
            }
            if (showProjectCockpitInSidenav(_identity)){
              _showProjectCockpitInSidenav = true;
            } else {
              _showProjectCockpitInSidenav = false;
            }
            _showAdminMenu = showAdminMenu(_identity);
            _showProjectOverview = showProjectOverview(_identity);
            _displayWelcomeDialog = displayWelcomeDialog(_identity);
            
            return deferred.resolve(_identity);
          }).catch(function() {
            $rootScope.$broadcast('stop-ignoring-401');
            AuthServerProvider.deleteToken();
            _identity = null;
            _authenticated = false;
            _canSwitchViews = false;
            _providerViewActive = false;
            _showAdminMenu = false;
            _showProjectCockpitInSidenav = false;
            _showProjectOverview = false;
            deferred.resolve(_identity);
          });
        } else {
          _identity = null;
          _authenticated = false;
          _canSwitchViews = false;
          _providerViewActive = false;
          _showAdminMenu = false;
          _showProjectCockpitInSidenav = false;
          _showProjectOverview = false;
          deferred.resolve(_identity);
        }
        return deferred.promise;
      },
      loginName: function() {
        return _identity && _identity.login;
      },
      canSwitchViews: function() {
        return _canSwitchViews;
      },
      activateProviderView: function() {
        _providerViewActive = true;
        localStorage.setItem('currentView', 'providerView');
        $rootScope.$broadcast('view-changed', localStorage.getItem('currentView'));
      },
      deactivateProviderView: function() {
        _providerViewActive = false;
        localStorage.setItem('currentView', 'orderView');
        $rootScope.$broadcast('view-changed', localStorage.getItem('currentView'));
      },
      isProviderActive: function() {
        return _providerViewActive;
      },
      showProjectCockpitInSidenav() {
        return _showProjectCockpitInSidenav;
      },
      displayWelcomeDialog() {
        return _displayWelcomeDialog;
      },
      showProjectOverviewInSidenav() {
        return _showProjectOverview;
      },
      showAdminMenuInSidenav() {
        return _showAdminMenu;
      },
      isPublisher() {
        return _.indexOf(_identity.authorities, 'ROLE_PUBLISHER') !== -1;
      },
      isAdmin() {
        return _.indexOf(_identity.authorities, 'ROLE_ADMIN') !== -1;
      },
      isDataprovider() {
        return _.indexOf(_identity.authorities, 'ROLE_DATA_PROVIDER') !== -1;
      },
      showAllData() {
        if (_identity != null && (_.indexOf(_identity.authorities, 'ROLE_ADMIN') !== -1
        || _.indexOf(_identity.authorities, 'ROLE_PUBLISHER') !== -1)){
          return true;
        }
        return false;
      }
    };
  });
