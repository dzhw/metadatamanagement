# local install infos

## docker

* before first `docker-compose up`:
    * `mkdir -p data/mongodb/db data/mongodb/logs data/elasticsearch/data` in order to avoid wrong permissions
    * if protonmail bridge is running: change smtp port of the bridge (e.g. `1026`)

## idp

* optional:
    * `docker-compose exec identity_provider apt update`
    * `docker-compose exec identity_provider apt install less vim`
* finish idp installation:
    * use sqlite (lazy me)
    * create roles: http://localhost:8082/admin/people/roles
        * `ROLE_ADMIN`
        * `ROLE_USER`
        * `ROLE_PUBLISHER`
        * `ROLE_ANONYMOUS`
        * `ROLE_DATA_PROVIDER`
        * `ROLE_RELEASE_MANAGER`
        * `ROLE_TASK_USER`
    * create users: http://localhost:8082/admin/people
        * `resource_server` (for backend and frontend)
            * `Administrator`
        * `taskuser` (for repost task)
            * `ROLE_TASK_USER`
        * `publisher` (for testing as a publisher)
            * `ROLE_USER`
            * `ROLE_PUBLISHER`
        * `dataprovider` (for testing as a data provider)
            * `ROLE_USER`
            * `ROLE_DATA_PROVIDER`
        * `<me>` (for testing as yourself)
            * `Administrator`
            * `ROLE_ADMIN`
            * `ROLE_USER`
            * `ROLE_PUBLISHER`
            * `ROLE_DATA_PROVIDER`
            * `ROLE_RELEASE_MANAGER`
    * create clients: http://localhost:8082/admin/config/services/consumer
        * `mdm_frontend`
            * user: `resource_server`
            * confidential: `true`
            * 3rd party: `true`
            * redirect: `http://localhost:8080`
        * `report_task`
            * user: `taskuser`
            * confidential: `true`
            * 3rd party: `false`
            * scopes: `ROLE_TASK_USER`
    * ensure claim `welcome_dialog_deactivated` is available for openid (workaround)
        * http://localhost:8082/admin/structure/claims
        * disable the claim `welcome_dialog_deactivated`
        * enable the claim `welcome_dialog_deactivated`
        * reason: claim is not listed for openid in simple oauth config until it is enabled
    * create a private claim called `preferred_username` (will be added as an access token claim)
        * http://localhost:8082/admin/structure/claims
        * Label: `preferred_username`
        * Type: `private`
        * Field name: `Name`
* fix jwt issuer:
    * `docker-compose exec identity_provider bash`
    * `vim /opt/drupal/web/modules/contrib/simple_oauth_claims/simple_oauth_claims.module`
        * add `, '/'` to rtrim() calls on lines 35 and 63
        * alternatively use `sed` (if `vim` hasn't been installed in this container):
            * `sed -i "s#\['iss'\] = rtrim((new Url('<front>'))->setAbsolute()->toString())#['iss'] = rtrim((new Url('<front>'))->setAbsolute()->toString(), '/')#g" /opt/drupal/web/modules/contrib/simple_oauth_claims/simple_oauth_claims.module`
    * clear drupal cache: http://localhost:8082/admin/config/development/performance
* fix jwt bearer validation:
    * `docker-compose exec identity_provider bash`
    * `vim /opt/drupal/vendor/league/oauth2-server/src/AuthorizationValidators/BearerTokenValidator.php`
        * change `'scopes'` to `'scope'` for getClaim() call on line 104
        * alternatively use `sed` (if `vim` hasn't been installed in this container):
            * `sed -i "s#\$token->getClaim('scopes')#\$token->getClaim('scope')#g" /opt/drupal/vendor/league/oauth2-server/src/AuthorizationValidators/BearerTokenValidator.php`
    * clear drupal cache: http://localhost:8082/admin/config/development/performance

### cause of the jwt issuer problem

* spring security fetches the openid configuration of the idp via `http://localhost:8082/.well-known/openid-configuration`
* the value of `issuer` in that document must match the configured issuer uri (configured either directly using the spring property `spring.security.oauth2.resourceserver.jwt.issuer-uri` or indirectly from the environment variable `RESOURCE_SERVER_ISSUER_URI`)
* the value of `issuer` in the openid configuration is set using `rtrim((new Url('<front>'))->setAbsolute()->toString(), '/')` in `web/modules/contrib/openid_connect_discovery/src/Controller/OpenidConnectDiscoveryController.php`
    * trailing slashes are trimmed
* the jwt issuer claim is set using `rtrim((new Url('<front>'))->setAbsolute()->toString())` in `web/modules/contrib/simple_oauth_claims/simple_oauth_claims.module`
    * trailing whitespaces are trimmed
* the system route `<front>` points to `/`, which is why the token issuer differs from the config issuer

## mongodb

* on ubuntu based systems `mongorestore` is provided by the package `mongodb-clients`

## build and run mdm with sso

* `git checkout sso`
* environment variables:
    * `JAVA_HOME=/home/<me>/.sdkman/candidates/java/15.0.2.hs-adpt`
    * `RESOURCE_SERVER_ISSUER_URI="http://localhost:8082"`
    * `USER_API_ENDPOINT="http://localhost:8082"`
    * `USER_API_USERNAME=resource_server` (actual username on idp)
    * `USER_API_PASSWORD=123` (password chosen for user `resource_server`)
    * `CLIENT_ID_LOCAL=64208774-4b2d-4703-9ea5-1de6c975ae44` (uuid of consumer/client `mdm_frontend`)
    * `CLIENT_SECRET_LOCAL=456` (secret chosen for consumer/client `mdm_frontend`)
    * `ISSUER_LOCAL="http://localhost:8082"`
* build frontend:
    * `CLIENT_ID_LOCAL=64208774-4b2d-4703-9ea5-1de6c975ae44 CLIENT_SECRET_LOCAL=456 ISSUER_LOCAL="http://localhost:8082" grunt buildlocal`
* build backend:
    * `JAVA_HOME=/home/jana/.sdkman/candidates/java/15.0.2.hs-adpt mvn verify`
* run backend:
    * `JAVA_HOME=/home/jana/.sdkman/candidates/java/15.0.2.hs-adpt RESOURCE_SERVER_ISSUER_URI="http://localhost:8082/" USER_API_ENDPOINT="http://localhost:8082" USER_API_USERNAME=resource_server USER_API_PASSWORD=123 mvn`

