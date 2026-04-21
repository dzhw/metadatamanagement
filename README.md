[![Build Status](https://github.com/dzhw/metadatamanagement/workflows/Build%20and%20Deploy/badge.svg)](https://github.com/dzhw/metadatamanagement/actions) [![Documentation Status](https://readthedocs.org/projects/metadatamanagement/badge/?version=latest)](https://metadatamanagement.readthedocs.io/de/latest/)
[![Known Backend Vulnerabilities](https://snyk.io/test/github/dzhw/metadatamanagement/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/dzhw/metadatamanagement?targetFile=pom.xml
)[![Known Frontend Vulnerabilities](https://snyk.io/test/github/dzhw/metadatamanagement/badge.svg?targetFile=package.json)](https://snyk.io/test/github/dzhw/metadatamanagement?targetFile=package.json
)[![codecov](https://codecov.io/gh/dzhw/metadatamanagement/branch/development/graph/badge.svg)](https://codecov.io/gh/dzhw/metadatamanagement)[![Mergify Status](https://gh.mergify.io/badges/dzhw/metadatamanagement.png?style=cut)](https://mergify.io)
[![DOI](https://zenodo.org/badge/39431147.svg)](https://zenodo.org/badge/latestdoi/39431147)

[![Sauce Test Status](https://app.saucelabs.com/browser-matrix/andydaniel.svg?cachebuster=1234)](https://app.saucelabs.com/u/andydaniel)
# Metadatamanagement (MDM)

The MDM holds the metadata of the data packages which are available in our Research Data Center [FDZ](https://fdz.dzhw.eu). It enables researchers to browse our data packages before signing a contract for using the data. 

# Developing the MDM system

Please checkout the development branch before starting to code and create a new branch starting with your username followed by the backlog items issue number you will be working on:

    git checkout development
    git checkout -b rreitmann/issue1234

Before you can build this project, install and configure the following dependencies:

1. Java 17
2. Maven 3.6.1 or newer
3. [Node.js][] 18 with npm, preferably through [NVM][]
4. Docker Desktop or a compatible Docker Engine with Docker Compose

On macOS with Homebrew:

```bash
brew install openjdk@17 maven nvm
brew install --cask docker
mkdir -p ~/.nvm
```

Add NVM and Java to your shell profile, for example `~/.zshrc`:

```bash
export NVM_DIR="$HOME/.nvm"
source "$(brew --prefix nvm)/nvm.sh"
export JAVA_HOME="/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"
export PATH="$JAVA_HOME/bin:$PATH"
```

Reload the shell and install Node:

```bash
source ~/.zshrc
nvm install 18
nvm use 18
```

On Windows, `patch.exe` has to exist in the PATH. It is distributed as part of git bash, or can be downloaded manually from [GnuWin32][].

## Running on your local machine

### 1. Prepare local service directories

Make sure that you have read-write access on the **data** directory in your project directory for Elasticsearch and MongoDB. Specifically **Mac** users need to run the following command to create all data directories before bringing up the containers for the first time:

```bash
mkdir -p data/elasticsearch/data data/mongodb/db data/mongodb/logs
```

Otherwise your Docker host may attempt to change permissions on the directories and fail.

### 2. Start Docker services

Start Docker Desktop first. Then start the local infrastructure:

```bash
docker compose up -d
```

MongoDB and Elasticsearch listen on their default ports. MailDev shows locally sent email on `http://localhost:8081`, and the identity provider is available on `http://localhost:8082`.

In case elasticsearch does not start successfully, you might need to increase its memory limit
`mem_limit: 512m`, e.g. to `1024` (this change requires removing and re-building the container).

Verify the services:

```bash
curl http://localhost:9200/
docker exec mongodb mongosh --quiet --eval 'db.runCommand({ ping: 1 }).ok'
```

The MongoDB connection used by the local backend is:

```text
host: localhost
port: 27017
database: metadatamanagement
```

### 3. Configure Maven GitHub package access

Maven needs access to GitHub Packages for the `pl.allegro.tech:embedded-elasticsearch` test dependency. Configure `~/.m2/settings.xml`:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <server>
      <id>github</id>
      <username>YOUR_GITHUB_USERNAME</username>
      <password>YOUR_GITHUB_TOKEN</password>
    </server>
  </servers>
</settings>
```

The token needs package read access.

### 4. Install the local Maven plugin

Install the local MDM Maven plugin. This plugin generates frontend translations and constants during the main Maven build.

```shell
mvn clean install -f maven-plugin/pom.xml
```

### 5. Install frontend dependencies

```bash
cd mdm-frontend
npm ci
cd ..
```

### 6. Generate backend and frontend build artifacts

Run Maven once before starting the frontend so the generated Angular constants file exists:

```bash
mvn compile -DskipTests -Dpmd.skip=true
```

This generates `mdm-frontend/src/app/legacy/app.constants.js`.

### 7. Start the Spring backend

In order for all external services to work on your local machine, set the following environment variables when starting the Spring Boot application:

* `DARA_ENDPOINT` (regular endpoint for registering projects)
* `DARA_USERNAME`
* `DARA_PASSWORD`
* `DARA_PID_ENDPOINT` (endpoint for registering variables)
* `DARA_PID_USERNAME`
* `DARA_PID_PASSWORD`
* `DATACITE_ENDPOINT`
* `DATACITE_USERNAME`
* `DATACITE_PASSWORD`

For normal local development, dummy values are enough to start the application:

```sh
DARA_ENDPOINT="http://localhost/dara/projects" \
DARA_USERNAME="dummy" \
DARA_PASSWORD="dummy" \
DARA_PID_ENDPOINT="http://localhost/dara/variables" \
DARA_PID_USERNAME="dummy" \
DARA_PID_PASSWORD="dummy" \
DATACITE_ENDPOINT="http://localhost/datacite" \
DATACITE_USERNAME="dummy" \
DATACITE_PASSWORD="dummy" \
mvn spring-boot:run
```

Verify the backend:

```bash
curl http://localhost:8080/management/info
```

Expected result: HTTP `200`.

`/management/health` may return `503 DOWN` locally when DARA/DataCite credentials are dummy values.

### 8. Start the Angular frontend

In a second terminal:

```bash
cd mdm-frontend
npm start
```

Open:

```text
http://localhost:4200/
```

Verify the frontend:

```bash
curl http://localhost:4200/
```

Expected result: HTTP `200`.

### 9. Optional: restore a MongoDB dump

You can get a MongoDB dump and restore it locally:

```bash
wget https://metadatamanagement-public.s3.eu-central-1.amazonaws.com/20220926_metadatamanagement_e2e.zip
unzip 20220926_metadatamanagement_e2e.zip
mv dump/metadatamanagement data/mongodb/db/
docker exec -it mongodb bash
cd /data/db
mongorestore ./metadatamanagement --db=metadatamanagement
exit
rm -r dump
```

### 10. Optional: run local frontend against production API

Create or use `mdm-frontend/proxy.prod.conf.json` with production targets for `/api`, `/oauth`, `/management`, and `/websocket`. Then start the frontend on a separate port:

```bash
cd mdm-frontend
npm run ng -- serve --port 4201 --proxy-config proxy.prod.conf.json
```

Open:

```text
http://localhost:4201/
```

This runs local frontend code against the production backend/search API.

If you run the backend on your machine for the first time, or you have restored a
mongodb dump, then you need to setup/reindex the elasticsearch indices. Therefore, login as admin to the application,
go to `Administration` on the left, navigate to `External Services` and then
click the red button `Reindex` for the Elasticsearch service. Reindexing can take up to 1 hour.

If you want to build a docker image for the metadatamanagement server app you can run

    mvn deploy

This image can be run with all its dependent containers by

    docker-compose -f docker-compose.yml -f docker-compose-app.yml up -d --build

## Building for the dev environment

Our CI pipleline will do some automatic checks and tests and it will optimize the metadatamanagement client for the dev environment. So before pushing to Github in order to be sure you won't fail the build you should run:

    mvn -Pdev clean verify

This will concatenate and minify CSS and JavaScript files using grunt. It will also modify the `index.html` so it references
these new files.

We test our project continuously with the Robot Framework. Test Developers can get further info [here](https://github.com/dzhw/metadatamanagement/wiki/Robot-Framework).


## Tweet 

#### Authentication
When an analysis package or data package is released with version >=1.0.0, the user can optionally post a message about 
the release on X (formerly Twitter).

To set this up, you need to have an X Developer Account (Free Access Level) and your projects' api 
credentials `consumer key` and `consumer secret`. Be aware that the current [Free Access Level](https://developer.twitter.com/en/docs/twitter-api)
is limited to 50 tweets/24h; 1,500 tweets/month; 1 environment; 1 project.

Make your credentials `consumer key` and `consumer secret` accessible by the `application.yml` of the current stage through
`sensitive_variables.tf` just like other highly sensitive data. 

```shell
[application.yml]
...
tweet:
  consumerkey: ${vcap.services.tweet.credentials.consumerkey}
  consumersecret: ${vcap.services.tweet.credentials.consumersecret}
  oauthtoken: ${vcap.services.tweet.credentials.oauthtoken}
  oauthtokensecret: ${vcap.services.tweet.credentials.oauthtokensecret}
  ...
```

Create your `oauthtoken` and `oauthtokensecret` by following the three steps of the Postman
Twitter examples: [Twitter OAuth 1.0a flow test](https://www.postman.com/twitter/workspace/twitter-s-public-workspace/request/9956214-5bd6ebb1-9d79-4456-a9a6-22ead4a41625).
1. step `oauth/request_token`: 

Execute the request with your consumer key and consumer secret from your Developer Account. 
An `OAUTH_TOKEN_FROM_STEP1` and `OAUTH_TOKEN_SECRET_FROM_STEP1` will be returned.

2. step `oauth/authorize`: 

Visit `https://api.twitter.com/oauth/authorize?oauth_token={OAUTH_TOKEN_FROM_STEP1}&oauth_token_secret={OAUTH_TOKEN_SECRET_FROM_STEP1}&oauth_callback_confirmed=true` with `OAUTH_TOKEN_FROM_STEP1` and `OAUTH_TOKEN_SECRET_FROM_STEP1` from the first step,
and authenticate your app.

After being redirected to X, open the network, and copy the values for `oauth_token` as `OAUTH_TOKEN_FROM_STEP2`
and `oauth_token` as `OAUTH_VERIFIER_FROM_STEP2` from this GET request
```shell
GET 'http://twitter.com/?oauth_token={OAUTH_TOKEN_FROM_STEP2}&oauth_verifier={OAUTH_VERIFIER_FROM_STEP2}`'
```

3. step `oauth/access_token`:

Insert the `OAUTH_TOKEN_FROM_STEP2` and `OAUTH_VERIFIER_FROM_STEP2` from step 2 into the third 
request (If you are using Postman like
the linked Twitter example, select `No Auth` instead of `OAuth 1.0`).
```shell
POST 'https://api.twitter.com/?oauth_token={OAUTH_TOKEN_FROM_STEP2}&oauth_verifier={OAUTH_VERIFIER_FROM_STEP2}' 
```

Add the returned values for `oauth_token` and `oauth_token_secret` from step 3 to the `sensitive_variables.tf`.

For further details also see [Authentication OAuth FAQ](https://developer.twitter.com/en/docs/authentication/faq).

# Big Thanks 

Cross-browser Testing Platform and Open Source :heart: Provided by [Sauce Labs][saucelabs]

Continuous Integration Platform provided by [Github Actions][GithubActions]

[saucelabs]: https://saucelabs.com
[Node.js]: https://nodejs.org/
[Grunt]: http://gruntjs.com/
[NVM]: https://github.com/creationix/nvm
[SDKMAN!]: http://sdkman.io/install.html
[GithubActions]: https://github.com/dzhw/metadatamanagement/actions
[GnuWin32]: http://gnuwin32.sourceforge.net/packages/patch.htm

[![forthebadge](http://forthebadge.com/images/badges/built-by-developers.svg)](http://forthebadge.com)  [![forthebadge](https://forthebadge.com/images/badges/built-with-science.svg)](https://forthebadge.com)
 [![forthebadge](https://forthebadge.com/images/badges/60-percent-of-the-time-works-every-time.svg)](https://forthebadge.com) [![forthebadge](http://forthebadge.com/images/badges/uses-badges.svg)](http://forthebadge.com) [![forthebadge](https://forthebadge.com/images/badges/makes-people-smile.svg)](https://forthebadge.com)
