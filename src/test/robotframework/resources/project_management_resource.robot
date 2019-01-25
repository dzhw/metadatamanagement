*** Settings ***
Documentation     Resources for Project Cockpit Management
Library           ExtendedSelenium2Library
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

Click on OK Button
   Click Element Through Tooltips  xpath=//md-dialog-actions//button[contains(.,'OK')]

Discard Changes Yes
   Click Element Through Tooltips  xpath=//md-dialog-actions//button[contains(.,'Ja')]

Discard Changes No
   Click Element Through Tooltips  xpath=//md-dialog-actions//button[contains(.,'Nein')]

Assign a dataprovider
   [Arguments]   ${dataprovidername}  ${dataprovidernameindex}
   Input Text  xpath=//md-card[@group='dataProviders']//following::input[@type='search']  ${dataprovidername}
   Click Element Through Tooltips  xpath=//span[@md-highlight-text='ctrl.searchText[ctrl.group]'][contains(.,'${dataprovidername}')][${dataprovidernameindex}]

Assign a publisher
   [Arguments]   ${publishername}  ${publishernameindex}
   Input Text  xpath=//md-card[@group='publishers']//following::input[@type='search']  ${publishername}
   Click Element Through Tooltips  xpath=//span[@md-highlight-text='ctrl.searchText[ctrl.group]'][contains(.,'${publishername}')][${publishernameindex}]

Assign to publisher
   Click Element Through Tooltips   xpath=//project-status-badge[@assignee-group="DATA_PROVIDER"]//div//button[@type="button"]

Assign to dataprovider
   Click Element Through Tooltips   xpath=//project-status-badge[@assignee-group="PUBLISHER"]//div//button[@type="button"]

Switch To Settings Tab
   Click Element Through Tooltips  xpath=//md-pagination-wrapper[@role="tablist"]//md-tab-item//md-icon[contains(.,"settings")]

Switch To Status Tab
   Click Element Through Tooltips  xpath=//md-pagination-wrapper[@role="tablist"]//md-tab-item//md-icon[contains(.,"info")]

Select Study Checkbox
   Click Element Through Tooltips  xpath=//md-checkbox[@name="studies"]

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

Ensure Data Providers Ready Checkbox is Disabled
   Page Should Contain Element  xpath=//*[@ng-model="ctrl.project.configuration[ctrl.type+'State'].dataProviderReady" and @disabled="disabled"][1]

Ensure Publisher Ready Checkbox is Disabled
   Page Should Contain Element  xpath=//*[@ng-model="ctrl.project.configuration[ctrl.type+'State'].publisherReady" and @disabled="disabled"][1]

Ensure Project Release Button is Disabled
   Page Should Contain Element  xpath=//md-card//release-status-badge[@released="ctrl.project.release"]//following::button[@disabled="disabled"]

Ensure Project Assign Role Button is Disabled
   Page Should Contain Element  xpath=//md-card//project-status-badge[@assignee-group="PUBLISHER"]//following::button[@disabled="disabled"]

Ensure Study Creation is Possible
   Click Element Through Tooltips  xpath=//md-card[@type="studies"]//button[contains(.,"Neu")]

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

Click Publisher Ready Checkbox for Studies
   Click Element Through Tooltips  xpath=//md-card[@type="studies"]//md-checkbox[contains(.,"Publisher Fertig")]

Click Dataprovider Ready Checkbox for Studies
   Click Element Through Tooltips  xpath=//md-card[@type="studies"]//md-checkbox[contains(.,"Datengeber Fertig")]

Click Publisher Ready Checkbox for Surveys
   Click Element Through Tooltips  xpath=//md-card[@type="surveys"]//md-checkbox[contains(.,"Publisher Fertig")]

Click Dataprovider Ready Checkbox for Surveys
   Click Element Through Tooltips  xpath=//md-card[@type="surveys"]//md-checkbox[contains(.,"Datengeber Fertig")]

Click Publisher Ready Checkbox for Instruments
   Click Element Through Tooltips  xpath=//md-card[@type="instruments"]//md-checkbox[contains(.,"Publisher Fertig")]

Click Dataprovider Ready Checkbox for Instruments
   Click Element Through Tooltips  xpath=//md-card[@type="instruments"]//md-checkbox[contains(.,"Datengeber Fertig")]

Click Publisher Ready Checkbox for Questions
   Click Element Through Tooltips  xpath=//md-card[@type="questions"]//md-checkbox[contains(.,"Publisher Fertig")]

Click Dataprovider Ready Checkbox for Questions
   Click Element Through Tooltips  xpath=//md-card[@type="questions"]//md-checkbox[contains(.,"Datengeber Fertig")]

Click Publisher Ready Checkbox for Datasets
   Click Element Through Tooltips  xpath=//md-card[@type="dataSets"]//md-checkbox[contains(.,"Publisher Fertig")]

Click Dataprovider Ready Checkbox for Datasets
   Click Element Through Tooltips  xpath=//md-card[@type="dataSets"]//md-checkbox[contains(.,"Datengeber Fertig")]

Click Publisher Ready Checkbox for Variables
   Click Element Through Tooltips  xpath=//md-card[@type="variables"]//md-checkbox[contains(.,"Publisher Fertig")]

Click Dataprovider Ready Checkbox for Variables
   Click Element Through Tooltips  xpath=//md-card[@type="variables"]//md-checkbox[contains(.,"Datengeber Fertig")]

Close The Toast Message
    [Arguments]  ${TOAST_MSSG}
    Click Element Through Tooltips  xpath=//md-toast//span[contains(.,"Die Aktion ist nicht möglich")]
    Element Should Contain  xpath=//md-toast//span[contains(.,"Die Aktion ist nicht möglich")]  ${TOAST_MSSG}
    Click Element Through Tooltips  xpath=//button//following::md-icon[contains(.,"close")]

