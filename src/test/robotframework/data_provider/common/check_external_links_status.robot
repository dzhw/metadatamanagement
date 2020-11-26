*** Settings ***
Documentation     Check 200 OK status of various static links of dataprovides
Resource          ../../resources/check_url_resource.robot
Resource          ../../resources/home_page_resource.robot


*** Test Cases ***
Check Link Status for MDM Documentation
    Check URL Status with xpath Locator   //a[contains(.,'Dokumentation')]

Check Link Status for Data Access
    Check URL Status with xpath Locator   //a[contains(.,'Datenzugang')]

Check Link Status for DZHW
    Check URL Status with xpath Locator   //a[contains(.,'Deutsches Zentrum für Hochschul- und Wissenschaftsforschung GmbH')]
    Get back to german home page  # to sync with the next test case

#Check Link Status for BMBF
    #Get Link Status with xpath Locator   //a[@id="bmbf-link"]  #bmbf link does not work on saucelabs for an uncertain reason
