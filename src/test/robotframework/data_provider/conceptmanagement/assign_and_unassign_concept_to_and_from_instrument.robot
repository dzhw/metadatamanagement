*** Settings ***
Documentation   Dataprovider assign a concept to an instrument and assert it under the instrument.
Metadata        Info on data    This test suite uses the project with the name "conceptproject${BROWSER}" which needs to be available in the testing environment
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Test Cases ***
Assign and Unassign concept to and from an instrument
    Get Back to german home page
    Select project by name  conceptproject${BROWSER}
    Click on Cockpit Button
    Click on Instrument Edit Button
    Assign Concept to an Instrument
    Click on Save Button
    Wait For Angular  10s
    Get back to german home page
    # wait for elasticsearch index beeing updated
    Sleep  60s
    Assert the concept has been assigned to the instrument
    Assert the concept is assigned to a data package
    Assert the concept is assigned to a survey
    Unassign the concept from the instrument
    Get back to home page and deselect project

*** Keywords ***
Click on Instrument Edit Button
   Click Element Through Tooltips  xpath=//md-card[@type="instruments"]//button[@ng-click="ctrl.edit(ctrl.searchState)"]//span[text()="Bearbeiten"]
   Click Element Through Tooltips  xpath=//md-card-actions//button[normalize-space()="Bearbeiten"]

Assign Concept to an Instrument
   Click Element  xpath=//md-chips[@name="concepts"]//input[@placeholder="Konzepte suchen..."]
   Input Text  xpath=//md-chips[@name="concepts"]//input[@placeholder="Konzepte suchen..."]  Test Konzept ${BROWSER}
   Run Keyword If    '${BROWSER}' == 'firefox'  Click Element Through Tooltips  xpath=//md-virtual-repeat-container[@ng-hide="$mdAutocompleteCtrl.hidden"]//ul//li//span//span[contains(., "con-i8h7b1a0e4e4i8f5b1$")]
   Run Keyword If    '${BROWSER}' == 'chrome'  Click Element Through Tooltips  xpath=//md-virtual-repeat-container[@ng-hide="$mdAutocompleteCtrl.hidden"]//ul//li//span//span[contains(., "con-d3i8h7i8g6g6i8f5b1$")]
   Run Keyword If    '${BROWSER}' == 'safari'  Click Element Through Tooltips  xpath=//md-virtual-repeat-container[@ng-hide="$mdAutocompleteCtrl.hidden"]//ul//li//span//span[contains(., "con-e2j9h7i8g6g6i9g6c2$")]
   Run Keyword If    '${BROWSER}' == 'edge'  Click Element Through Tooltips  xpath=//md-virtual-repeat-container[@ng-hide="$mdAutocompleteCtrl.hidden"]//ul//li//span//span[contains(., "con-e4g6c2d3e4j9j9f5b1$")]
   Run Keyword If    '${BROWSER}' == 'ie'  Click Element Through Tooltips  xpath=//md-virtual-repeat-container[@ng-hide="$mdAutocompleteCtrl.hidden"]//ul//li//span//span[contains(., "con-j9b1i8i8a0i8i8f5b1$")]

Click on Save Button
   Click Element Through Tooltips  xpath=//button[@ng-click="ctrl.saveInstrument()"]//md-icon[contains(., "save")]

Assert the concept has been assigned to the instrument
   Click on instruments tab
   Click on first search result
   Click on concept tab
   Click on first search result
   Run Keyword If    '${BROWSER}' == 'firefox'  Page Should Contain   Test Konzept Firefox Publisher
   Run Keyword If    '${BROWSER}' == 'chrome'  Page Should Contain   Test Konzept Chrome Publisher
   Run Keyword If    '${BROWSER}' == 'safari'  Page Should Contain   Test Konzept Safari Publisher
   Run Keyword If    '${BROWSER}' == 'edge'  Page Should Contain   Test Konzept Edge Publisher
   Run Keyword If    '${BROWSER}' == 'ie'  Page Should Contain   Test Konzept IE Publisher

Assert the concept is assigned to a data package
   Page Should Contain  Study Title in German

Assert the concept is assigned to a survey
   Click on surveys tab
   Page Should Contain  Survey Title in German

Unassign the concept from the instrument
   Click on instruments tab
   Click Element Through Tooltips  xpath=//md-card-actions//button[normalize-space()="Bearbeiten"]
   Click Element Through Tooltips  xpath=//md-chips[@name="concepts"]//button[@ng-if="$mdChipsCtrl.isRemovable()"]//md-icon[@role="img"]
   Click on Save Button

Get back to home page and deselect project
   Get back to german home page
   Click Element Through Tooltips    xpath=//md-sidenav//project-navbar-module//button[@aria-label='Clear Input']
