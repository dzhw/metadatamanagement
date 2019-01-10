*** Settings ***
Documentation     Publisher Create a new Project and Assign role and check the project as dataprovider
Default Tags      smoketest
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot


*** Variables ***
${PROJECT_NAME}  hossainrobot
${TOAST_MSSG}  Die Aktion ist nicht möglich

*** Test Cases ***
Check Dataprovider Cannot Change Anything
   Create Project  ${PROJECT_NAME}${BROWSER}
   Assign a dataprovider
   Save Changes
   Publisher Logout
   Login as dataprovider
   Select project by name  ${PROJECT_NAME}${BROWSER}
   Click on Cockpit Button
   Ensure Data Providers Ready Checkbox is Disabled
   Ensure Publisher Ready Checkbox is Disabled
   Ensure Project Release Button is Disabled
   Ensure Project Assign Role Button is Disabled
   Ensure New Study Create Button is Restricted
   Ensure Question Upload Button is Restricted
   Switch To Settings Tab
   Ensure Expected Metadata Fields are Disabled
   Data Provider Logout
   Login as publisher
   Delect project by name  ${PROJECT_NAME}${BROWSER}

*** Keywords ***
Assign a dataprovider
    Input Text  xpath=//project-cockpit-userlist[@group='dataProviders']//following::input[@type='search']  dataprovider
    Click Element Through Tooltips  xpath=//span[@md-highlight-text='searchText[group]'][contains(.,'dataprovider')][1]

Ensure Data Providers Ready Checkbox is Disabled
    Page Should Contain Element  xpath=//*[@ng-model="project.configuration[group+'State'].dataProviderReady" and @disabled="disabled"][1]

Ensure Publisher Ready Checkbox is Disabled
    Page Should Contain Element  xpath=//*[@ng-model="project.configuration[group+'State'].publisherReady" and @disabled="disabled"][1]

Ensure Project Release Button is Disabled
    Page Should Contain Element  xpath=//md-card//release-status-badge[@released="project.release"]//following::button[@disabled="disabled"]

Ensure Project Assign Role Button is Disabled
    Page Should Contain Element  xpath=//md-card//project-status-badge[@assignee-group="PUBLISHER"]//following::button[@disabled="disabled"]

Ensure New Study Create Button is Restricted
    Click Element Through Tooltips  xpath=//md-card[@group="studies"]//button[contains(.,"Neu")]
    Click Element Through Tooltips  xpath=//md-toast//span[contains(.,"Die Aktion ist nicht möglich")]
    Element Should Contain  xpath=//md-toast//span[contains(.,"Die Aktion ist nicht möglich")]  ${TOAST_MSSG}
    Click Element Through Tooltips  xpath=//button//following::md-icon[contains(.,"close")]

Ensure Question Upload Button is Restricted
    Click Element Through Tooltips  xpath=//md-card[@group="questions"]//button[contains(.,"Hochladen")]
    Click Element Through Tooltips  xpath=//md-toast//span[contains(.,"Die Aktion ist nicht möglich")]
    Element Should Contain  xpath=//md-toast//following::span[contains(.,"Die Aktion ist nicht möglich")]  ${TOAST_MSSG}
    Click Element Through Tooltips  xpath=//button//following::md-icon[contains(.,"close")]

Switch To Settings Tab
    Click Element Through Tooltips  xpath=//md-pagination-wrapper[@role="tablist"]//md-tab-item//md-icon[contains(.,"settings")]

Ensure Expected Metadata Fields are Disabled
    Page Should Contain Element  xpath=//md-card[@ng-if="project.configuration.requirements"]//following::md-checkbox[@name="survey" and @disabled="disabled"]
    Page Should Contain Element  xpath=//md-card[@ng-if="project.configuration.requirements"]//following::md-checkbox[@name="instruments" and @disabled="disabled"]
    Page Should Contain Element  xpath=//md-card[@ng-if="project.configuration.requirements"]//following::md-checkbox[@name="questions" and @disabled="disabled"]
    Page Should Contain Element  xpath=//md-card[@ng-if="project.configuration.requirements"]//following::md-checkbox[@name="dataSet" and @disabled="disabled"]
    Page Should Contain Element  xpath=//md-card[@ng-if="project.configuration.requirements"]//following::md-checkbox[@name="variables" and @disabled="disabled"]

