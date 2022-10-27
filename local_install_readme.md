# local install infos

## docker

* before first `docker-compose up`:
    * `mkdir -p data/mongodb/db data/mongodb/logs data/elasticsearch/data` in order to avoid wrong permissions
    * if protonmail bridge is running: change smtp port of the bridge (e.g. `1026`)
    * restrict ram usage of elasticsearch (optional, but recommended)
        * add `ES_JAVA_OPTS` to environment of elasticsearch container in `docker-compose.yml`
        * e.g. (4 gb): `- "ES_JAVA_OPTS=-Xms4096m -Xmx4096m"`

## idp

* optional:
    * `docker-compose exec identity_provider apt update`
    * `docker-compose exec identity_provider apt install less vim`
* also optional: update container
    * `docker pull sanduhrs/identity-provider:latest`
    * `docker-compose rm identity_provider`
    * `docker-compose up identity_provider`
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
            * client id: any you like
            * user: `resource_server`
            * confidential: `true`
            * 3rd party: `true`
            * redirect: `http://localhost:8080`
        * `report_task`
            * client id: any you like
            * user: `taskuser`
            * confidential: `true`
            * 3rd party: `false`
            * scopes: `ROLE_TASK_USER`
        * `mdm_frontend_pkce`
            * client id: any you like
            * user: `resource_server`
            * **DON'T SET ANY SECRET** (otherwise refresh token will not work)
            * confidential: `false`
            * pkce: `true`
            * 3rd party: `true`
            * redirect: `http://localhost:8080`
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
    * create an undetermined claim called `iss` (will be added as an access token claim)
        * http://localhost:8082/admin/structure/claims
        * Label: `iss`
            * alternatively use `Issuer` as label and edit the machine name to be `iss`
        * Type: `undetermined`
        * Field name: any
* claims might be missing in tokens after the drupal cache has been cleared
    * just disable and enable them to fix this
    * http://localhost:8082/admin/structure/claims

## mongodb

* on ubuntu based systems `mongorestore` is provided by the package `mongodb-clients`

## build and run mdm with sso

* `git checkout sso`
* environment variables:
    * `JAVA_HOME=/home/<me>/.sdkman/candidates/java/15.0.2.hs-adpt`
    * `RESOURCE_SERVER_ISSUER_URI="http://localhost:8082/"`
    * `USER_API_ENDPOINT="http://localhost:8082"`
    * `USER_API_USERNAME=resource_server` (actual username on idp)
    * `USER_API_PASSWORD=123` (password chosen for user `resource_server`)
    * `CLIENT_ID_LOCAL=64208774-4b2d-4703-9ea5-1de6c975ae44` (uuid of consumer/client `mdm_frontend`)
    * `CLIENT_SECRET_LOCAL=456` (secret chosen for consumer/client `mdm_frontend`)
    * `ISSUER_LOCAL="http://localhost:8082/"`
* build frontend:
    * `CLIENT_ID_LOCAL=64208774-4b2d-4703-9ea5-1de6c975ae44 CLIENT_SECRET_LOCAL=456 ISSUER_LOCAL="http://localhost:8082/" grunt buildlocal`
* build backend:
    * `JAVA_HOME=/home/jana/.sdkman/candidates/java/15.0.2.hs-adpt mvn verify`
* run backend:
    * `JAVA_HOME=/home/jana/.sdkman/candidates/java/15.0.2.hs-adpt RESOURCE_SERVER_ISSUER_URI="http://localhost:8082/" USER_API_ENDPOINT="http://localhost:8082" USER_API_USERNAME=resource_server USER_API_PASSWORD=123 mvn`
* use PKCE:
    * `CLIENT_ID_LOCAL=64208774-4b2d-4703-9ea5-1de6c975ae44` (uuid of consumer/client `mdm_frontend_pkce`)
    * `CLIENT_SECRET_LOCAL=` (consumer/client `mdm_frontend_pkce` must not have a secret set)


