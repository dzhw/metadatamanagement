[![Build Status](https://travis-ci.org/dzhw/metadatamanagement.svg?branch=master)](https://travis-ci.org/dzhw/metadatamanagement) [![Coverage Status](https://coveralls.io/repos/dzhw/metadatamanagement/badge.svg?branch=master&service=github)](https://coveralls.io/github/dzhw/metadatamanagement?branch=master) [![Dependency Status](https://www.versioneye.com/user/projects/55af5e7a3865620017000077/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55af5e7a3865620017000077)

# MetaDataManagement
This platform gathers all metadata of the surveys published by our FDZ.

## Getting started
Clone this repo
```
git clone https://github.com/dzhw/metadatamanagement.git
```
and install JDK 1.8 and Apache Maven 3 than start the server.
```
mvn spring-boot:run
```
and point your browser to [http://localhost:8080/](http://localhost:8080/)

## Pivotal Cloudfoundry
This project is currently built and deployed to Pivotal Cloudfoundry by Travis CI. You can test the latest version on [https://metadatamanagement.cfapps.io/](https://metadatamanagement.cfapps.io/)
