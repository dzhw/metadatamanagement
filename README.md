[![Build Status](https://travis-ci.org/dzhw/metadatamanagement.svg?branch=master)](https://travis-ci.org/dzhw/metadatamanagement) [![Documentation Status](https://readthedocs.org/projects/metadatamanagement/badge/?version=latest)](https://metadatamanagement.readthedocs.io/de/latest/?badge=latest) [![Sauce Test Status](https://saucelabs.com/buildstatus/rreitmann)](https://saucelabs.com/u/rreitmann)
[![Known Vulnerabilities](https://snyk.io/test/github/dzhw/metadatamanagement/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/dzhw/metadatamanagement?targetFile=pom.xml
)[![Coverage Status](https://coveralls.io/repos/dzhw/metadatamanagement/badge.svg?branch=master&service=github)](https://coveralls.io/github/dzhw/metadatamanagement?branch=master)
[![DOI](https://zenodo.org/badge/39431147.svg)](https://zenodo.org/badge/latestdoi/39431147)

[![Sauce Build Matrix](https://saucelabs.com/browser-matrix/rreitmann.svg)](https://saucelabs.com/u/rreitmann)
# Metadatamanagement (MDM)

The MDM holds the metadata of the studies which are available as data products in our Research Data Center [FDZ](https://fdz.dzhw.eu). It enables researchers to browse our data products before signing a contract for using the data.

# Developing the MDM system

Please checkout the development branch before starting to code and create a new branch starting with your username followed by the backlog items issue number you will be working on:

    git checkout development
    git checkout -b rreitmann/issue1234

Before you can build this project, you must install and configure the following dependencies on your machine:

1. Java: You need to install java 8 sdk on your system. On Ubuntu you should use [SDKMAN!][]
2. Maven: You need to install maven 3 on your system. On Ubuntu you should use [SDKMAN!][]
3. [Node.js][]: Node.js (latest) and npm (coming with node.js) are required as well. On Ubuntu you should install node using [NVM][]

We use [Grunt][] as our client build system. Install the grunt command-line tool globally with:

    npm install -g grunt-cli

You need to install [Bower][] globally as well:

    npm install -g bower

On Windows, `patch.exe` has to exist in the PATH. It is distributed as part of git bash, or can be downloaded manually from [GnuWin32][].

## Running on your local machine

Before starting the app on your local machine you need to start the following Document Stores:
1. Mongodb: Mongodb must be running on the default port, on ubuntu you should install it from here https://docs.mongodb.org/manual/tutorial/install-mongodb-on-ubuntu/
2. Elasticsearch (6.3.2): Elasticsearch must be running on its default port. You can download it from here https://www.elastic.co/downloads/elasticsearch

Make sure that you have read-write-access on the ***data*** directory (in your project directory) for Elasticsearch.

Alternatively you can run

    docker-compose up
    # for later use once the containers are created
    docker-compose start

to start all services the metadatamanagement depends on. Mongodb and Elasticsearch will be listening on its default ports.

In order to have all java dependencies for the server and  all bower dependencies for the client and in order to build everything, simply run (and lean back for a while):

    mvn clean package

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    mvn
    grunt

If you want to build a docker image for the metadatamanagement server app you can run

    mvn install

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

Before deploying the `{dev|test|prod}` system you need to [install the cloudfoundry cli](https://docs.cloudfoundry.org/cf-cli/install-go-cli.html#-linux-installation).

You can build and deploy the jar to the desired environment by running

    ./deploy/build-and-deploy.sh {dev|test|prod}

We test our project continuously with the Robot Framework. Test Developers can get further info [here](https://github.com/dzhw/metadatamanagement/wiki/Robot-Framework).

## Pivotal Cloudfoundry
This project is currently built and deployed to Pivotal Cloudfoundry by [Travis CI][TravisCI]. You can test the latest version on https://metadatamanagement-dev.cfapps.io/

## Big Thanks

Cross-browser Testing Platform and Open Source :heart: Provided by [Sauce Labs][saucelabs]

Continuous Integration Platform provided by [Travis CI][TravisCI]

[saucelabs]: https://saucelabs.com
[JHipster]: https://jhipster.github.io/
[Node.js]: https://nodejs.org/
[Bower]: http://bower.io/
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
