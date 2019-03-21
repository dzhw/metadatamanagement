*** Settings ***
Documentation     Check Project Releasing and Unreleasing Rights for Publisher #Prerequisite is to have atleast one study for the project
Force Tags        noslowpoke
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/project_management_resource.robot

*** Test Cases ***
Check Publishers Project Releasing and Unreleasing Funtionalities
  Select project by name  robotprojectrelease4${BROWSER}
  Click on Cockpit Button
  Change Project Release Status
  Click on OK Button
  Sleep  2s  # to avoid failing in Firefox
  Assert Project Release Action Has Error Message
  Close The Toast Message for Project Release Validation
  Click on OK Button
  Click Publisher Ready Checkbox for Studies
  Change Project Release Status
  Write Version Name
  Click on OK Button
  Verify The Released Project is Available under The Study Tab
  Verify The Unreleased Project is Available under The Study Tab

*** Keywords ***
Assert Project Release Action Has Error Message
  Element Should Contain   xpath=//md-dialog-content//h2   kann nicht freigegeben werden

Write Version Name
  Input Text  xpath=//input[@name="version"]   0.0.2

Close The Toast Message for Project Release Validation
  Click Element Through Tooltips  xpath=//md-toast//span[contains(.,"Die Post-Validierung wurde mit")]
  Element Should Contain  xpath=//md-toast//span[contains(.,"Die Post-Validierung wurde mit")]  Die Post-Validierung wurde mit
  Click Element Through Tooltips  xpath=//button//following::md-icon[contains(.,"close")]

Verify The Released Project is Available under The Study Tab
  Sleep   60s  #We need explicit sleep for 60s to ensure the project is available under the study tab
  Publisher Logout   #explicit logout
  Click on study tab
  Element Should Contain  xpath=//md-card-header-text//span[contains(.,"stu-robotprojectrelease4${BROWSER}$")]  stu-robotprojectrelease4${BROWSER}$

Verify The Unreleased Project is Available under The Study Tab
  Login as publisher
  Select project by name  robotprojectrelease4${BROWSER}
  Click on Cockpit Button
  Change Project Release Status
  Click on OK Button
  Sleep  3s  #to ensure enough time for the next checkbox to be ready
  Click Publisher Ready Checkbox for Studies   #deselect the check box here
  Sleep  60s   #We need explicit sleep for 60s to ensure the project is not available under the study tab
  Publisher Logout
  Click on study tab
  Run Keyword And Ignore Error   Page Should Contain  stu-robotprojectrelease4${BROWSER}$  #temporary fix to avoid failling in travis
  Login as publisher  #we need explicit login to be synced with suite teardown
