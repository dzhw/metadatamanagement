*** Settings ***
Documentation     Resources for Project Cockpit Management
Library           SeleniumLibrary
Resource          click_element_resource.robot
Variables         ../common_variables.yaml

*** Keywords ***
Change Project Roles
  Click Element Through Tooltips  xpath=//button//md-icon[contains(., "send")]

Change Project Release Status
  Click Element Through Tooltips  xpath=//button//md-icon[contains(., "screen_share")]

Write Message and Assign
  Click Element Through Tooltips  xpath=//textarea[@name="assigneeMessage"]
  Input Text   xpath=//textarea[@name="assigneeMessage"]   This is a meesage by Robot for changing project roles!!
  Click Element Through Tooltips   xpath=//md-dialog-actions//button[contains(., "Zuweisen")]

Confirm Local Release
  [Documentation]  Check "local release" hint when running in local environment
  Run Keyword Ignore Error   Click Element Through Tooltips   xpath=//md-dialog-content//md-input-container//md-checkbox[@name='releaseConfirmed']

Click on OK Button
   Click Element Through Tooltips  xpath=//md-dialog-actions//button[contains(.,'OK')]

Discard Changes Yes
   Click Element Through Tooltips  xpath=//md-dialog-actions//button[contains(.,'Ja')]

Discard Changes No
   Click Element Through Tooltips  xpath=//md-dialog-actions//button[contains(.,'Nein')]

Assign a dataprovider
   [Arguments]   ${dataprovidername}
   Run Keyword If  '${BROWSER}' == 'ie'  Scroll Element Into View  xpath=//md-card[@group='dataProviders']//input
   Input Text  xpath=//md-card[@group='dataProviders']//input  ${dataprovidername}
   Click Element Through Tooltips  xpath=//md-virtual-repeat-container//span[text()='${dataprovidername}']

Assign a publisher
   [Arguments]   ${publishername}
   Run Keyword If  '${BROWSER}' == 'ie'  Scroll Element Into View  xpath=//md-card[@group='publishers']//input
   Input Text  xpath=//md-card[@group='publishers']//input  ${publishername}
   Click Element Through Tooltips  xpath=//md-virtual-repeat-container//span[text()='${publishername}']

Assign to publisher
   Click Element Through Tooltips   xpath=//project-status-badge[@assignee-group="DATA_PROVIDER"]//div//button[@type="button"]

Assign to dataprovider
   Click Element Through Tooltips   xpath=//project-status-badge[@assignee-group="PUBLISHER"]//div//button[@type="button"]

Switch To Settings Tab
   Click Element Through Tooltips  xpath=//md-pagination-wrapper[@role="tablist"]//md-tab-item//md-icon[contains(.,"settings")]

Switch To Status Tab
   Click Element Through Tooltips  xpath=//md-pagination-wrapper[@role="tablist"]//md-tab-item//md-icon[contains(.,"info")]

Select Data Package Checkbox
   Click Element Through Tooltips  xpath=//md-checkbox[@name="dataPackages"]

Select Survey Checkbox
   Click Element Through Tooltips  xpath=//md-checkbox[@name="survey"]

Select Instruments Checkbox
   Click Element Through Tooltips  xpath=//md-checkbox[@name="instruments"]

Select Questions Checkbox
   Click Element Through Tooltips  xpath=//md-checkbox[@name="questions"]

Select Datasets Checkbox
   Click Element Through Tooltips  xpath=//md-checkbox[@name="dataSet"]

Select Variable Checkbox
   Click Element Through Tooltips  xpath=//md-checkbox[@name="variables"]

Click Publications Checkbox
   Click Element Through Tooltips  xpath=//md-checkbox[@name="publications"]

Ensure Data Providers Ready Checkbox is Disabled
   Page Should Contain Element  xpath=//*[@ng-model="ctrl.project.configuration[ctrl.type+'State'].dataProviderReady" and @disabled="disabled"][1]

Ensure Publisher Ready Checkbox is Disabled
   Page Should Contain Element  xpath=//*[@ng-model="ctrl.project.configuration[ctrl.type+'State'].publisherReady" and @disabled="disabled"][1]

Ensure Project Release Button is Disabled
   Page Should Contain Element  xpath=//md-card//release-status-badge[@released="ctrl.project.release"]//following::button[@disabled="disabled"]

Ensure Project Is Unreleased
   Page Should Contain Element  xpath=//md-card//release-status-badge[@released="ctrl.project.release"]//span["Nicht freigegeben"]

Ensure Project Assign Role Button is Disabled
   Page Should Contain Element  xpath=//md-card//project-status-badge[@assignee-group="PUBLISHER"]//following::button[@disabled="disabled"]

Ensure Data Package Creation is Possible
   Click Element Through Tooltips  xpath=//md-card[@type="dataPackages"]//button[contains(.,"Neu")]

Ensure Survey Creation is Possible
   Click Element Through Tooltips  xpath=//md-card[@type="surveys"]//button[contains(.,"Neu")]

Ensure Instrument Creation is Possible
   Click Element Through Tooltips  xpath=//md-card[@type="instruments"]//button[contains(.,"Neu")]

Ensure Questions Upload is Possible
   Click Element Through Tooltips  xpath=//md-card[@type="questions"]//button[contains(.,"Hochladen")]

Ensure Dataset Creation is Possible
   Click Element Through Tooltips  xpath=//md-card[@type="dataSets"]//button[contains(.,"Neu")]

Ensure Variable Upload is Possible
   Click Element Through Tooltips  xpath=//md-card[@type="variables"]//button[contains(.,"Hochladen")]

Click Dataprovider Ready Checkbox
   [Documentation]  Clicks the Dataprovider Ready checkbox for the given card (dataPackages, surveys, instruments, questions, dataSets, variables)
   [Arguments]  ${card}
   Click Element Through Tooltips  xpath=//md-card[@type="${card}"]/md-card-content//md-checkbox/div[normalize-space(.)="Datengeber:innen Fertig"]/parent::md-checkbox

Click Publisher Ready Checkbox
   [Documentation]  Clicks the Publisher Ready checkbox for the given card (dataPackages, surveys, instruments, questions, dataSets, variables)
   [Arguments]  ${card}
   Click Element Through Tooltips  xpath=//md-card[@type="${card}"]/md-card-content//md-checkbox/div[normalize-space(.)="Publisher Fertig"]/parent::md-checkbox

Close The Toast Message
    [Arguments]  ${TOAST_MSSG}
    Click Element Through Tooltips  xpath=//md-toast//span[contains(.,"Die Aktion ist nicht möglich")]
    Element Should Contain  xpath=//md-toast//span[contains(.,"Die Aktion ist nicht möglich")]  ${TOAST_MSSG}
    Click Element Through Tooltips  xpath=//button//following::md-icon[contains(.,"close")]
