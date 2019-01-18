*** Settings ***
Documentation     Publisher Create a new Project and Assign role and check the project as dataprovider
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
   Assign a dataprovider  dataprovider  1
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
   Login as publisher
   Delete project by name  ${PROJECT_NAME}${BROWSER}



*** Keywords ***
Ensure Study Create Button is Restricted
    Click Element Through Tooltips  xpath=//md-card[@type="studies"]//button[contains(.,"Neu")]
    Click Element Through Tooltips  xpath=//md-toast//span[contains(.,"Die Aktion ist nicht möglich")]
    Element Should Contain  xpath=//md-toast//span[contains(.,"Die Aktion ist nicht möglich")]  ${TOAST_MSSG}
    Click Element Through Tooltips  xpath=//button//following::md-icon[contains(.,"close")]

Ensure Question Upload Button is Restricted
    Click Element Through Tooltips  xpath=//md-card[@type="questions"]//button[contains(.,"Hochladen")]
    Click Element Through Tooltips  xpath=//md-toast//span[contains(.,"Die Aktion ist nicht möglich")]
    Element Should Contain  xpath=//md-toast//span[contains(.,"Die Aktion ist nicht möglich")]  ${TOAST_MSSG}
    Click Element Through Tooltips  xpath=//button//following::md-icon[contains(.,"close")]

Ensure Instrument Create Button is Restricted
    Click Element Through Tooltips  xpath=//md-card[@type="instruments"]//button[contains(.,"Neu")]
    Click Element Through Tooltips  xpath=//md-toast//span[contains(.,"Die Aktion ist nicht möglich")]
    Element Should Contain  xpath=//md-toast//span[contains(.,"Die Aktion ist nicht möglich")]  ${TOAST_MSSG}
    Click Element Through Tooltips  xpath=//button//following::md-icon[contains(.,"close")]

Ensure Dataset Create Button is Restricted
    Click Element Through Tooltips  xpath=//md-card[@type="dataSets"]//button[contains(.,"Neu")]
    Click Element Through Tooltips  xpath=//md-toast//span[contains(.,"Die Aktion ist nicht möglich")]
    Element Should Contain  xpath=//md-toast//span[contains(.,"Die Aktion ist nicht möglich")]  ${TOAST_MSSG}
    Click Element Through Tooltips  xpath=//button//following::md-icon[contains(.,"close")]

Ensure Expected Metadata Fields are Disabled
    Page Should Contain Element  xpath=//md-card[@ng-if="ctrl.project.configuration.requirements"]//following::md-checkbox[@name="survey" and @disabled="disabled"]
    Page Should Contain Element  xpath=//md-card[@ng-if="ctrl.project.configuration.requirements"]//following::md-checkbox[@name="instruments" and @disabled="disabled"]
    Page Should Contain Element  xpath=//md-card[@ng-if="ctrl.project.configuration.requirements"]//following::md-checkbox[@name="questions" and @disabled="disabled"]
    Page Should Contain Element  xpath=//md-card[@ng-if="ctrl.project.configuration.requirements"]//following::md-checkbox[@name="dataSet" and @disabled="disabled"]
    Page Should Contain Element  xpath=//md-card[@ng-if="ctrl.project.configuration.requirements"]//following::md-checkbox[@name="variables" and @disabled="disabled"]


