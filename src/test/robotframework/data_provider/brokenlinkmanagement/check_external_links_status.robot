*** Settings ***
Documentation     Check 200 OK status of various static links of dataprovides
Library           HttpLibrary.HTTP
Library           ExtendedSelenium2Library

*** Variables ***
${dzhwandbmbflink}
${docanddataaccesslink}

*** Test Cases ***
Check Link Status for DZHW Documentation and Data Access
    Get Link Status with xpath Locator   //a[@aria-label="Klicken, um die Benutzerdokumentation zu öffnen"]
    Get Link Status with xpath Locator   //a[@aria-label="Klicken, um Informationen zum Datenzugang zu erhalten"]

Check Link Status for DZHW and BMBF
    Get Link Status with xpath Locator   //a[@id="dzhw-link"]
    #Get Link Status with xpath Locator   //a[@id="bmbf-link"]

*** Keywords ***
Get Link Status with xpath Locator
    [Arguments]    ${xpathlocator}
    ${docanddataaccesslink}     Get Element Attribute   xpath=${xpathlocator}@href
    Log To Console   ${docanddataaccesslink}
    GET    ${docanddataaccesslink}
    Response Status Code Should Equal   200 OK
