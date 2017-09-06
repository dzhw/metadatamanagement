[![Build Status](https://travis-ci.org/dzhw/metadatamanagement.svg?branch=master)](https://travis-ci.org/dzhw/metadatamanagement) [![Coverage Status](https://coveralls.io/repos/dzhw/metadatamanagement/badge.svg?branch=development&service=github)](https://coveralls.io/github/dzhw/metadatamanagement?branch=development) [![Dependency Status](https://www.versioneye.com/user/projects/55af5e7a3865620017000077/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55af5e7a3865620017000077) [![Sauce Test Status](https://saucelabs.com/buildstatus/rreitmann)](https://saucelabs.com/u/rreitmann)
[![Sauce Build Matrix](https://saucelabs.com/browser-matrix/rreitmann.svg)](https://saucelabs.com/u/rreitmann)

# Developing metadatamanagement

Before you can build this project, you must install and configure the following dependencies on your machine:

1. Java: You need to install java 8 sdk on your system.
2. Maven: You need to install maven 3 on your system.
3. [Node.js][]: Node.js and npm (coming with node.js) are required as well. On Ubuntu you should install node using [NVM][]
4. Mongodb: Mongodb must be running on the default port, on ubuntu you should install it from here https://docs.mongodb.org/manual/tutorial/install-mongodb-on-ubuntu/
5. Elasticsearch: Elasticsearch must be running on its default port. You can download it from here https://www.elastic.co/downloads/elasticsearch

We use [Grunt][] as our build system. Install the grunt command-line tool globally with:

    npm install -g grunt-cli

You should install [Bower][] globally as well:

    npm install -g bower

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    mvn
    grunt

# Building for the dev environment

To optimize the metadatamanagement client for the dev environment, run:

    mvn -Pdev clean package

This will concatenate and minify CSS and JavaScript files. It will also modify `index.html` so it references
these new files.

You can build and deploy the jar to the dev environment by running

    mvn -Pdev clean package cf:push-only
    
## Pivotal Cloudfoundry
This project is currently built and deployed to Pivotal Cloudfoundry by Travis CI. You can test the latest version on https://metadatamanagement-dev.cfapps.io/

[JHipster]: https://jhipster.github.io/
[Node.js]: https://nodejs.org/
[Bower]: http://bower.io/
[Grunt]: http://gruntjs.com/
[BrowserSync]: http://www.browsersync.io/
[Karma]: http://karma-runner.github.io/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html
[Protractor]: https://angular.github.io/protractor/
[NVM]: https://github.com/creationix/nvm
