[![Build Status](https://travis-ci.org/dzhw/metadatamanagement.svg?branch=master)](https://travis-ci.org/dzhw/metadatamanagement) [![Coverage Status](https://coveralls.io/repos/dzhw/metadatamanagement/badge.svg?branch=master&service=github)](https://coveralls.io/github/dzhw/metadatamanagement?branch=master) [![Dependency Status](https://www.versioneye.com/user/projects/55af5e7a3865620017000077/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55af5e7a3865620017000077)
[![Sauce Test Status](https://saucelabs.com/buildstatus/rrreitmann)](https://saucelabs.com/u/rreitmann)
[![Sauce Build Matrix](https://saucelabs.com/browser-matrix/rreitmann.svg)](https://saucelabs.com/u/rreitmann)

# Developing metadatamanagement

This application was generated using JHipster, you can find documentation and help at https://jhipster.gitub.io.

Before you can build this project, you must install and configure the following dependencies on your machine:

1. Java: You need to install java 8 sdk on your system.
2. Maven: You need to install maven 3 on your system.
3. [Node.js][]: Node.js and npm (coming with node.js) are required as well. On Ubuntu you should install node using [NVM][] 
4. Mongodb: Mongodb must be running on the default port, on ubuntu you should install it from here https://docs.mongodb.org/manual/tutorial/install-mongodb-on-ubuntu/

After installing Node, you should be able to run the following command to install development tools (like
[Bower][] and [BrowserSync][]). You will only need to run this command when dependencies change in package.json.

    npm install

We use [Grunt][] as our build system. Install the grunt command-line tool globally with:

    npm install -g grunt-cli

You should install [Bower][] and [Yoeman][] globally as well:

    npm install -g bower
    npm install -g yo
    npm install -g generator-jhipster

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    mvn
    grunt

Bower is used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in `bower.json`. You can also run `bower update` and `bower install` to manage dependencies.
Add the `-h` flag on any command to see how you can use it. For example, `bower update -h`.

# Building for production

To optimize the metadatamanagement client for production, run:

    mvn -Pprod clean package

This will concatenate and minify CSS and JavaScript files. It will also modify `index.html` so it references
these new files.

To ensure everything worked, run:

    java -jar target/*.war --spring.profiles.active=prod

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

# Testing

Unit tests are run by [Karma][] and written with [Jasmine][]. They're located in `src/test/javascript` and can be run with:

    grunt test

## Pivotal Cloudfoundry
This project is currently built and deployed to Pivotal Cloudfoundry by Travis CI. You can test the latest version on https://metadatamanagement.cfapps.io/

[JHipster]: https://jhipster.github.io/
[Node.js]: https://nodejs.org/
[Bower]: http://bower.io/
[Grunt]: http://gruntjs.com/
[BrowserSync]: http://www.browsersync.io/
[Karma]: http://karma-runner.github.io/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html
[Protractor]: https://angular.github.io/protractor/
[Yoeman]: http://yeoman.io/
[NVM]: https://github.com/creationix/nvm
