[![Build Status](https://travis-ci.org/dzhw/metadatamanagement.svg?branch=master)](https://travis-ci.org/dzhw/metadatamanagement) [![Documentation Status](https://readthedocs.org/projects/metadatamanagement/badge/?version=latest)](https://metadatamanagement.readthedocs.io/de/latest/?badge=latest)
[![Sauce Test Status](https://app.saucelabs.com/buildstatus/rreitmann)](https://app.saucelabs.com/u/rreitmann)
[![Known Backend Vulnerabilities](https://snyk.io/test/github/dzhw/metadatamanagement/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/dzhw/metadatamanagement?targetFile=pom.xml
)[![Known Frontend Vulnerabilities](https://snyk.io/test/github/dzhw/metadatamanagement/badge.svg?targetFile=package.json)](https://snyk.io/test/github/dzhw/metadatamanagement?targetFile=package.json
)[![codecov](https://codecov.io/gh/dzhw/metadatamanagement/branch/development/graph/badge.svg)](https://codecov.io/gh/dzhw/metadatamanagement)[![Mergify Status](https://gh.mergify.io/badges/dzhw/metadatamanagement.png?style=cut)](https://mergify.io)
[![DOI](https://zenodo.org/badge/39431147.svg)](https://zenodo.org/badge/latestdoi/39431147)

[![Sauce Test Status](https://app.saucelabs.com/browser-matrix/rreitmann.svg)](https://app.saucelabs.com/u/rreitmann)
# Metadatamanagement (MDM)

The MDM holds the metadata of the data packages which are available in our Research Data Center [FDZ](https://fdz.dzhw.eu). It enables researchers to browse our data packages before signing a contract for using the data.

# Developing the MDM system

Please checkout the development branch before starting to code and create a new branch starting with your username followed by the backlog items issue number you will be working on:

    git checkout development
    git checkout -b rreitmann/issue1234

Before you can build this project, you must install and configure the following dependencies on your machine:

1. Java: You need to install java 14 sdk on your system. On Ubuntu you should use [SDKMAN!][]
2. Maven: You need to install maven 3.6.1 or above on your system. On Ubuntu you should use [SDKMAN!][]
3. [Node.js][]: Node.js (latest) and npm (coming with node.js) are required as well. On Ubuntu you should install node using [NVM][]

We use [Grunt][] as our client build system. Install the grunt command-line tool globally with:

    npm install -g grunt-cli

On Windows, `patch.exe` has to exist in the PATH. It is distributed as part of git bash, or can be downloaded manually from [GnuWin32][].

## Running on your local machine

Before starting the app on your local machine you need to start the following Document Stores:
1. Mongodb: Mongodb must be running on the default port, on ubuntu you should install it from here https://docs.mongodb.org/manual/tutorial/install-mongodb-on-ubuntu/
2. Elasticsearch (7.9.0): Elasticsearch must be running on its default port. You can download it from here https://www.elastic.co/downloads/elasticsearch

Make sure that you have read-write-access on the ***data*** directory (in your project directory) for Elasticsearch.

***Alternatively*** you can run

    docker-compose up
    # for later use once the containers are created
    docker-compose start

to start all services the metadatamanagement depends on. Mongodb and Elasticsearch will be listening on its default ports.

In order to have all java dependencies for the server and  all nodejs dependencies for the client and in order to build everything, simply run (and lean back for a while):

    mvn clean install

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    mvn
    grunt

If you want to build a docker image for the metadatamanagement server app you can run

    mvn deploy

This image can be run with all its dependent containers by

    docker-compose -f docker-compose.yml -f docker-compose-app.yml up -d --build

## Building for the dev environment

Our CI pipleline will do some automatic checks and tests and it will optimize the metadatamanagement client for the dev environment. So before pushing to Github in order to be sure you won't fail the build you should run:

    mvn -Pdev clean install

This will concatenate and minify CSS and JavaScript files using grunt. It will also modify the `index.html` so it references
these new files.

To make the build runnable with an enabled dev profile, you'll need to install the following dependencies:

* Python

Once Python is installed, run:

    pip install git+https://github.com/dzhw/javasphinx.git --user

Note that the `--user` flag installs the dependency somewhere in your user directory (e.g. /home/{user}/local/bin on Linux). Make sure that the installed binaries/scripts are on your path.

We test our project continuously with the Robot Framework. Test Developers can get further info [here](https://github.com/dzhw/metadatamanagement/wiki/Robot-Framework).

## AWS Fargate
This project is currently built and deployed to AWS Fargate by [Travis CI][TravisCI]. You can test the latest version on https://dev.metadata.fdz.dzhw.eu/

## Big Thanks

Cross-browser Testing Platform and Open Source :heart: Provided by [Sauce Labs][saucelabs]

Continuous Integration Platform provided by [Travis CI][TravisCI]

[saucelabs]: https://saucelabs.com
[JHipster]: https://jhipster.github.io/
[Node.js]: https://nodejs.org/
[Grunt]: http://gruntjs.com/
[BrowserSync]: http://www.browsersync.io/
[Karma]: http://karma-runner.github.io/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html
[NVM]: https://github.com/creationix/nvm
[SDKMAN!]: http://sdkman.io/install.html
[TravisCI]: https://travis-ci.org/
[GnuWin32]: http://gnuwin32.sourceforge.net/packages/patch.htm

[![forthebadge](http://forthebadge.com/images/badges/built-by-developers.svg)](http://forthebadge.com)  [![forthebadge](https://forthebadge.com/images/badges/built-with-science.svg)](https://forthebadge.com)
 [![forthebadge](https://forthebadge.com/images/badges/60-percent-of-the-time-works-every-time.svg)](https://forthebadge.com) [![forthebadge](http://forthebadge.com/images/badges/uses-badges.svg)](http://forthebadge.com) [![forthebadge](https://forthebadge.com/images/badges/makes-people-smile.svg)](https://forthebadge.com)
