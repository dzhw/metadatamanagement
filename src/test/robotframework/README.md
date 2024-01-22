# E2E Test with the Robot Framework

# Execution of Tests

## Run tests locally

In the file `common_variables.yaml` the parameter `website` must be adjusted.

For local execution use `website: http://localhost:8080/de/start`

For execution on the development instance use `website: https://dev.metadata.fdz.dzhw.eu/de/start`

## Run complete test suites

There are three test suites that map the different roles of MDM. As an example, 
below are the necessary commands that launch the tests in Chrome for the respective role.

- Publisher
  ``` shell
  robot -P ./src/test/robotframework/libs -d target/test/robotframework/logs/chrome -v BROWSER:chrome --include publisherNOTfirefoxonly ./src/test/robotframework
  ```
- Dataprovider
  ``` shell
  robot -P ./src/test/robotframework/libs -d target/test/robotframework/logs/chrome -v BROWSER:chrome --include dataproviderNOTfirefoxonly ./src/test/robotframework
  ```
- Public User
  ``` shell
  robot -P ./src/test/robotframework/libs -d target/test/robotframework/logs/chrome -v BROWSER:chrome --include publicuserNOTfirefoxonly ./src/test/robotframework
  ```

## Run single test

Parameter `-t` allows to specify a test case name. The parameter can be used multiple times.

``` shell
robot -P ./src/test/robotframework/libs -d target/test/robotframework/logs/chrome -v BROWSER:chrome --include publisherNOTfirefoxonly -t CreateAnalysisPackage ./src/test/robotframework
```

The parameter `-s` can be used to specify an entire test suite. All tests contained will be executed.

``` shell
robot -P ./src/test/robotframework/libs -d target/test/robotframework/logs/chrome -v BROWSER:chrome --include publisherNOTfirefoxonly -s conceptmanagement ./src/test/robotframework
```

Names of test cases and test suite have to be written as one word and are case-INsensitive.

It should also be noted that when running test cases individually, the setup/teardown process of the associated Test Suite
is not executed.

## Add break points

Add the following line to the test code to add a break point.

```robot
Evaluate    pdb.Pdb(stdout=sys.__stdout__).set_trace()    modules=sys, pdb
```

Type `continue` in the terminal to continue the execution.

## Reports

The logs and reports of the executed tests can be found in the project directory under `/target/test/robotframework/logs`.
The parameter `-d` can be used to specify a different directory for storing the reports.

## Overview of the test projects

### Temporary projects (created during test)

Temporary test projects are created for different test suites. 
Occasionally these were not deleted due to failure of tests.
Temporary projects must not exist before executing the test, or the test will fail.
Temporary projects contain the expression `temprobot`.

- `temprobotcheckaccess[BROWSER]`
- `temprobotassignroles[BROWSER]`
- `temprobotdeleteroles[BROWSER]`
- `atemprobotcheckicons[BROWSER]` (needs an `a` at the beginning because of sorting)
- `tempdatapackage[BROWSER]`
- `tempanalysis[BROWSER]`

The common teardown process (filed in `./__init__.robot`) deletes all of these projects if
they still exist at the end of the execution.

### Required projects and datasets

There are some projects and datasets that need to be available in the testing environment:

- `robotprojectrelease4[BROWSER]` 
- `robotproject4[BROWSER]` 
- `robotproject`
- `cmp2014` 
- `gra2005` 
- `conceptproject[BROWSER]`
- `fileuploadproject` (The project needs to contain a survey which is validly filled with every necessary input. Additionaly it has to have exactly one attachment.)
- `testanalysispackage`
- Concept `Roll Back Concept [BROWSER] De` with one previous version with the title "Roll Back Concept [BROWSER] De_Rollback"
- Concept `Referenced Concept [BROWSER] De`, which needs to be referenced in at least one instrument.

The projects must not only be created, but also contain the necessary data (see [MongoDB dump](#mongodb-dump))

The common setup process checks if the listed projects (not the concepts) do exist.
If they don't the execution is canceled.

### Info on used data in tests

Tests using test projects and data are labeled with the additional metadata keyword `info on data`. 
There you can find the name of the used test project and whether it is temporary or not.
This information is also visible in the test logs.

### MongoDB dump

A database dump with all required data can be downloaded 
under `https://metadatamanagement-public.s3.eu-central-1.amazonaws.com/20220926_metadatamanagement_e2e.zip` to enable
the local execution of tests.
