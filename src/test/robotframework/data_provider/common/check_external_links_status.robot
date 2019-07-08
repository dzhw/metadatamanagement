*** Settings ***
Documentation     Check 200 OK status of various static links of dataprovides
Resource          ../../resources/check_url_resource.robot
Resource          ../../resources/home_page_resource.robot


*** Test Cases ***
Check Link Status for MDM Documentation
    Check URL Status with xpath Locator   //a[@aria-label="Klicken, um die Benutzerdokumentation zu öffnen"]

Check Link Status for Data Access
    Check URL Status with xpath Locator   //a[@aria-label="Klicken, um Informationen zum Datenzugang zu erhalten"]

Check Link Status for DZHW
    Check URL Status with xpath Locator   //a[@id="dzhw-link"]
    Get back to german home page  # to sync with the next test case

#Check Link Status for BMBF
    #Get Link Status with xpath Locator   //a[@id="bmbf-link"]  #bmbf link does not work in travis for an uncertain reason

