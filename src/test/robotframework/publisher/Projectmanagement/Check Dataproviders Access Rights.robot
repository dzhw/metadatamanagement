*** Settings ***
Documentation     Publisher Create a new Project and Assign role and check the project as dataprovider
Force Tags        noslowpoke
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/project_management_resource.robot


*** Variables ***
${PROJECT_NAME}  hossainrobot
${TOAST_MSSG}  Die Aktion ist nicht möglich

*** Test Cases ***
Check Publisher Can Change Accordingly
   Create Project  ${PROJECT_NAME}${BROWSER}
   Assign a dataprovider  dataprovider
   Select Survey Checkbox
   Select Instruments Checkbox
   Select Questions Checkbox
   Select Datasets Checkbox
   Select Variable Checkbox
   Save Changes
   Switch To Status Tab
   Ensure Survey Creation is Possible
   Go Back
   Sleep  1s   #to avoid failling edge test
   Ensure Instrument Create Button is Restricted
   Ensure Dataset Create Button is Restricted
   Publisher Logout

Check Dataprovider Cannot Change Anything
   Login as dataprovider
   Select project by name  ${PROJECT_NAME}${BROWSER}
   Click on Cockpit Button
   Ensure Data Providers Ready Checkbox is Disabled
   Ensure Publisher Ready Checkbox is Disabled
   Ensure Project Release Button is Disabled
   Ensure Project Assign Role Button is Disabled
   Ensure Study Create Button is Restricted
   Ensure Question Upload Button is Restricted
   Switch To Settings Tab
   Ensure Expected Metadata Fields are Disabled
   Data Provider Logout

Check Project is Assigned to Dataprovider But Can Not Change Anything When Publisher is Ready
   Login as publisher
   Select project by name  ${PROJECT_NAME}${BROWSER}
   Click on Cockpit Button
   Click Publisher Ready Checkbox for Surveys
   Click Dataprovider Ready Checkbox for Surveys
   Click Publisher Ready Checkbox for Instruments
   Click Dataprovider Ready Checkbox for Instruments
   Click Publisher Ready Checkbox for Questions
   Click Dataprovider Ready Checkbox for Questions
   Click Publisher Ready Checkbox for Datasets
   Click Dataprovider Ready Checkbox for Datasets
   Click Publisher Ready Checkbox for Variables
   Click Dataprovider Ready Checkbox for Variables
   Click Publisher Ready Checkbox for Studies
   Click Dataprovider Ready Checkbox for Studies
   Change Project Roles
   Write Message and Assign
   Sleep  1s  #to ensure that the next locator is visible
   Assert Project is Assigned to Dataprovider
   Save Changes
   Publisher Logout
   Login as dataprovider
   Select project by name  ${PROJECT_NAME}${BROWSER}
   Click on Cockpit Button
   Ensure Study Create Button is Restricted
   Ensure Survey Create Button is Restricted
   Ensure Instrument Create Button is Restricted
   Ensure Question Upload Button is Restricted
   Ensure Dataset Create Button is Restricted
   Ensure Variable Upload Button is Restricted
   Data Provider Logout
   Login as publisher
   Delete project by name  ${PROJECT_NAME}${BROWSER}


*** Keywords ***
Assert Project is Assigned to Dataprovider
    Element Should Contain  xpath=//project-status-badge//span[contains(.,"Zugewiesen an Datengeber")]  Zugewiesen an Datengeber

Ensure Study Create Button is Restricted
    Click Element Through Tooltips  xpath=//md-card[@type="studies"]//button[contains(.,"Neu")]
    Close The Toast Message  ${TOAST_MSSG}

Ensure Survey Create Button is Restricted
    Click Element Through Tooltips  xpath=//md-card[@type="surveys"]//button[contains(.,"Neu")]
    Close The Toast Message  ${TOAST_MSSG}

Ensure Variable Upload Button is Restricted
    Click Element Through Tooltips  xpath=//md-card[@type="variables"]//button[contains(.,"Hochladen")]
    Run Keyword if  '${BROWSER}' == 'chrome'  Close The Toast Message  ${TOAST_MSSG}
    Run Keyword if  '${BROWSER}' == 'firefox'  Close The Toast Message  ${TOAST_MSSG}
    Run Keyword if  '${BROWSER}' == 'edge'  Close The Toast Message  ${TOAST_MSSG}
    Run Keyword if  '${BROWSER}' == 'ie'  Close The Toast Message for Upload Button in IE   #in IE upload is not possible because of that toast mesaage is different

Ensure Question Upload Button is Restricted
    Click Element Through Tooltips  xpath=//md-card[@type="questions"]//button[contains(.,"Hochladen")]
    Run Keyword if  '${BROWSER}' == 'chrome'  Close The Toast Message  ${TOAST_MSSG}
    Run Keyword if  '${BROWSER}' == 'firefox'  Close The Toast Message  ${TOAST_MSSG}
    Run Keyword if  '${BROWSER}' == 'edge'  Close The Toast Message  ${TOAST_MSSG}
    Run Keyword if  '${BROWSER}' == 'ie'  Close The Toast Message for Upload Button in IE

Ensure Instrument Create Button is Restricted
    Click Element Through Tooltips  xpath=//md-card[@type="instruments"]//button[contains(.,"Neu")]
    Close The Toast Message  ${TOAST_MSSG}

Ensure Dataset Create Button is Restricted
    Click Element Through Tooltips  xpath=//md-card[@type="dataSets"]//button[contains(.,"Neu")]
    Close The Toast Message  ${TOAST_MSSG}

Ensure Expected Metadata Fields are Disabled
    Page Should Contain Element  xpath=//md-card[@ng-if="ctrl.project.configuration.requirements"]//following::md-checkbox[@name="survey" and @disabled="disabled"]
    Page Should Contain Element  xpath=//md-card[@ng-if="ctrl.project.configuration.requirements"]//following::md-checkbox[@name="instruments" and @disabled="disabled"]
    Page Should Contain Element  xpath=//md-card[@ng-if="ctrl.project.configuration.requirements"]//following::md-checkbox[@name="questions" and @disabled="disabled"]
    Page Should Contain Element  xpath=//md-card[@ng-if="ctrl.project.configuration.requirements"]//following::md-checkbox[@name="dataSet" and @disabled="disabled"]
    Page Should Contain Element  xpath=//md-card[@ng-if="ctrl.project.configuration.requirements"]//following::md-checkbox[@name="variables" and @disabled="disabled"]

Close The Toast Message for Upload Button in IE
    Click Element Through Tooltips  xpath=//md-toast//span[contains(.,"Diese Aktion wird ")]
    Element Should Contain  xpath=//md-toast//span[contains(.,"Diese Aktion wird ")]  Diese Aktion wird vom verwendeten Browser nicht unterstützt.
    Click Element Through Tooltips  xpath=//button//following::md-icon[contains(.,"close")]