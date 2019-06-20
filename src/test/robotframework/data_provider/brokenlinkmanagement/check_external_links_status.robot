*** Settings ***
Documentation     Check 200 OK status of various static links of dataprovides
Library           HttpLibrary.HTTP
Library           ExtendedSelenium2Library

*** Variables ***
${dzhwandbmbflink}
${docanddataaccesslink}

*** Test Cases ***
Check Link Status for DZHW and BMBF
    Get Attribute and Status of the Element with id   dzhw-link
    Get Attribute and Status of the Element with id   bmbf-link

Check Link Status for DZHW Documentation and Data Access
    Get Attribute and Status of the Element with tooltip   md-tooltip-2
    Get Attribute and Status of the Element with tooltip   md-tooltip-4

*** Keywords ***
Get Attribute and Status of the Element with id
    [Arguments]    ${id}
    ${dzhwandbmbflink}     Get Element Attribute   xpath=//a[@id="${id}"]@href
    Log To Console   ${dzhwandbmbflink}
    GET    ${dzhwandbmbflink}
    Response Status Code Should Equal    200 OK

Get Attribute and Status of the Element with tooltip
    [Arguments]    ${tooltip}
    ${docanddataaccesslink}     Get Element Attribute   xpath=//a[@md-labeled-by-tooltip="${tooltip}"]@href
    Log To Console   ${docanddataaccesslink}
    GET    ${docanddataaccesslink}
    Response Status Code Should Equal   200 OK
