*** Settings ***
Documentation     Banner appears for Dataproviders and assert the welcome text and dont show again checkbox
Library           HttpLibrary.HTTP
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/check_url_resource.robot

*** Test Cases ***
Check for Dataproviders Welcome Banner Appears
   Data Provider Logout   # explicit logout to sync with suite setup
   Login as dataprovidertest
   Assert Welcome Text After Login
   Check URL Status with xpath Locator   //p//a[contains(., "Dokumentation")]   #check MDM documentation url status
   Assert Checkbox Dont Show Again is Available  # prerequisite to maintain the test, never mark the checkbox for dataprovidertest
   Close The Banner
   Click on Information for Data Providers Link
   Close The Banner
   Reload Page   # cross check the banner after reload also
   Sleep  1s
   Assert Welcome Text After Login
   Close The Banner
   Dataprovidertest Provider Logout
   Login as dataprovider

*** Keywords ***
Assert Welcome Text After Login
   Page Should Contain Element   xpath=//h2[contains(., 'Herzlich Willkommen')]

Assert Checkbox Dont Show Again is Available
   Page Should Contain Element   xpath=//md-checkbox//div[contains(. , ' Nicht mehr anzeigen')]

Login as dataprovidertest
   ${visible}=  Run Keyword And Return Status    Element Should Be Visible   xpath=//*[@id = 'login']
   Run Keyword If    not ${visible}    Click Element Through Tooltips    xpath=//*[@id = 'account-menu-toggle']
   Click Element Through Tooltips    xpath=//*[@id = 'login']
   Input Text    id=username    dataprovidertest
   Input Password    id=password    dataprovidertest
   Click Element Through Tooltips    xpath=//button[@type='submit']

Close The Banner
   Click Element Through Tooltips  xpath=//button[contains(.,' Schließen')]

Click on Information for Data Providers Link
    Click Element Through Tooltips  xpath=//span[contains(.,'Hinweise für Datengeber')]

Dataprovidertest Provider Logout
    ${url} =    Get Location
    Run Keyword If    '/de/' in '${url}'    Click Element Through Tooltips    xpath=//button[contains(.,'abmelden')]
    Run Keyword If    '/en/' in '${url}'    Click Element Through Tooltips    xpath=//button[contains(.,'logout')]
