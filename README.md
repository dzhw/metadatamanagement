[![Build Status](https://travis-ci.org/dzhw/metadatamanagement.svg?branch=development)](https://travis-ci.org/dzhw/metadatamanagement)  [![Sauce Test Status](https://saucelabs.com/buildstatus/rreitmann)](https://saucelabs.com/u/rreitmann)
[![Known Vulnerabilities](https://snyk.io/test/github/dzhw/metadatamanagement/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/dzhw/metadatamanagement?targetFile=pom.xml
)[![Coverage Status](https://coveralls.io/repos/dzhw/metadatamanagement/badge.svg?branch=development&service=github)](https://coveralls.io/github/dzhw/metadatamanagement?branch=development) [![Built with Grunt](https://cdn.gruntjs.com/builtwith.svg)](https://gruntjs.com/)

[![Sauce Build Matrix](https://saucelabs.com/browser-matrix/rreitmann.svg)](https://saucelabs.com/u/rreitmann)

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

## Running on your local machine

Before starting the app on your local machine you need to start the following Document Stores:
1. Mongodb: Mongodb must be running on the default port, on ubuntu you should install it from here https://docs.mongodb.org/manual/tutorial/install-mongodb-on-ubuntu/
2. Elasticsearch (5.2.2): Elasticsearch must be running on its default port. You can download it from here https://www.elastic.co/downloads/elasticsearch

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    mvn
    grunt

## Building for the dev environment

To optimize the metadatamanagement client for the dev environment, run:

    mvn -Pdev clean install

This will concatenate and minify CSS and JavaScript files using grunt. It will also modify `index.html` so it references
these new files.

Before deploying the dev system you need to [install the cloudfoundry cli](https://docs.cloudfoundry.org/cf-cli/install-go-cli.html#-linux-installation).

You can build and deploy the jar to the dev environment by running

    ./deploy/build-and-deploy.sh dev

## Pivotal Cloudfoundry
This project is currently built and deployed to Pivotal Cloudfoundry by [Travis CI][TravisCI]. You can test the latest version on https://metadatamanagement-dev.cfapps.io/

## Big Thanks

Cross-browser Testing Platform and Open Source :heart: Provided by [Sauce Labs][saucelabs]

Continous Integration Platform provided by [Travis CI][TravisCI]

[saucelabs]: https://saucelabs.com
[JHipster]: https://jhipster.github.io/
[Node.js]: https://nodejs.org/
[Bower]: http://bower.io/
[Grunt]: http://gruntjs.com/
[BrowserSync]: http://www.browsersync.io/
[Karma]: http://karma-runner.github.io/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html
[NVM]: https://github.com/creationix/nvm
[SDKMAN!]:http://sdkman.io/install.html
[TravisCI]:https://travis-ci.org/

[![forthebadge](http://forthebadge.com/images/badges/built-by-developers.svg)](http://forthebadge.com)  [![forthebadge](https://forthebadge.com/images/badges/built-with-science.svg)](https://forthebadge.com)
 [![forthebadge](https://forthebadge.com/images/badges/60-percent-of-the-time-works-every-time.svg)](https://forthebadge.com) [![forthebadge](http://forthebadge.com/images/badges/uses-badges.svg)](http://forthebadge.com) [![forthebadge](https://forthebadge.com/images/badges/makes-people-smile.svg)](https://forthebadge.com)
