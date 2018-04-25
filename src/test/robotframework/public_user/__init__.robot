*** Settings ***
Documentation     Suite Setup and Teardown for Test Cases executed as Public User.
Suite Setup       Open Browser with Home Page
Suite Teardown    Close All Browsers
Library           ExtendedSelenium2Library

*** Variables ***
${WEBSITE}           https://metadatamanagement-dev.cfapps.io/#!/de

*** Keywords ***
Open Browser with Home Page
    Open Browser  ${WEBSITE}  ${BROWSER}
    Maximize Browser Window
