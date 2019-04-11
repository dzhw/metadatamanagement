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
  Sleep  2s  # to avoid failing in firefox
  Assert Project Release Action Has Error Message
  Close The Toast Message for Project Release Validation
  Click on OK Button
  Click Publisher Ready Checkbox for Studies
  Change Project Release Status
  Write Version Name
  Click on OK Button
  Verify The Released Project is Available under The Study Tab
  Verify The Unreleased Project is Still Available under The Study Tab with Shadow Copy
  Edit Project Title and Check it does not appear under study when unreleased
  Check Edited Project appears under study when released
  Restore The Previous Project Version and Publish Again
  Verify The Re-Released Previous Project is Available under The Study Tab
  Unrelased The Project again to Sync with Intial Step

*** Keywords ***
Assert Project Release Action Has Error Message
  Wait Until Element Is Visible   xpath=//md-dialog-content//h2
  Element Should Contain   xpath=//md-dialog-content//h2   kann nicht freigegeben werden

Write Version Name
  Input Text  xpath=//input[@name="version"]   0.0.2

Close The Toast Message for Project Release Validation
  Click Element Through Tooltips  xpath=//md-toast//span[contains(.,"Die Post-Validierung wurde mit")]
  Element Should Contain  xpath=//md-toast//span[contains(.,"Die Post-Validierung wurde mit")]  Die Post-Validierung wurde mit
  Click Element Through Tooltips  xpath=//button//following::md-icon[contains(.,"close")]

Click Study Edit Button
  Click Element Through Tooltips  xpath=//button[md-icon[text()="mode_edit"]]

Click Study Save Button
  Click Element Through Tooltips  xpath=//button[@type="submit"]//md-icon[contains(. , "save")]

Click Restore Button
  Click Element Through Tooltips  xpath=//div[@ng-if="ctrl.study.id"]//button[@type="button"]//md-icon[contains(.,"undo")]

Revise to second latest version
  Click Element Through Tooltips  xpath=//md-dialog//table//tbody//tr[2]//td
  Sleep  1s

Get back to home page and deselect project
  Get back to german home page
  Click Element Through Tooltips    xpath=//md-sidenav//project-navbar-module//button[@aria-label="Clear Input"]

Verify The Released Project is Available under The Study Tab
  Sleep  30s  #We need explicit sleep to ensure the project is available under the study tab
  Publisher Logout   #explicit logout
  Click on study tab
  Wait Until Page Contains Element  xpath=//md-card-header-text//span[contains(.,"stu-robotprojectrelease4${BROWSER}$")]  5s

Verify The Unreleased Project is Still Available under The Study Tab with Shadow Copy
  Login as publisher
  Select project by name  robotprojectrelease4${BROWSER}
  Click on Cockpit Button
  Change Project Release Status
  Click on OK Button
  Sleep  2s  #to ensure enough time for the next checkbox to be ready
  Click Publisher Ready Checkbox for Studies   #deselect the check box here
  Sleep  5s   #We need explicit sleep to ensure the project is not available under the study tab
  Publisher Logout
  Click on study tab
  Wait Until Page Contains Element  xpath=//md-card-header-text//span[contains(.,"stu-robotprojectrelease4${BROWSER}$")]  5s

Edit Project Title and Check it does not appear under study when unreleased
  Login as publisher  #we need explicit login to be synced with suite teardown
  Select project by name  robotprojectrelease4${BROWSER}
  Click Study Edit Button
  Input Text    name=titleDe    Test Project Release Study ${BROWSER} DE Edit_786
  Input Text    name=titleEn    Test Project Release Study ${BROWSER} EN Edit_786
  Click Study Save Button
  Get back to german home page
  Sleep  10s
  Publisher Logout
  Click on study tab
  Reload Page
  Wait Until Page Contains Element  xpath=//md-card-header-text//span[contains(. ,"Test Project Release Study ${BROWSER} DE")]  10s

Check Edited Project appears under study when released
  Login as publisher
  Select project by name  robotprojectrelease4${BROWSER}
  Click on Cockpit Button
  Click Publisher Ready Checkbox for Studies
  Change Project Release Status
  Write Version Name
  Click on OK Button
  Sleep  90s  #We need explicit sleep to ensure the project is available under the study tab
  Publisher Logout   #explicit logout
  Click on study tab
  Reload Page
  Wait Until Page Contains Element  xpath=//md-card-header-text//span[contains(. ,"Test Project Release Study ${BROWSER} DE Edit_786")]  10s

Restore The Previous Project Version and Publish Again
  Login as publisher
  Select project by name  robotprojectrelease4${BROWSER}
  Click on Cockpit Button
  Change Project Release Status
  Click on OK Button
  Sleep  2s
  Click Publisher Ready Checkbox for Studies
  Get back to german home page
  Click Study Edit Button
  Click Restore Button
  Revise to second latest version   #select the previous version
  Click Study Save Button
  Click on Cockpit Button
  Click Publisher Ready Checkbox for Studies
  Change Project Release Status  #release the previous version again
  Click on OK Button
  Sleep  90s

Verify The Re-Released Previous Project is Available under The Study Tab
  Publisher Logout
  Click on study tab
  Reload Page
  Wait Until Page Contains Element  xpath=//md-card-header-text//span[contains(. ,"Test Project Release Study ${BROWSER} DE")]  10s

Unrelased The Project again to Sync with Intial Step
  Login as publisher
  Select project by name  robotprojectrelease4${BROWSER}
  Click on Cockpit Button
  Change Project Release Status  #unrelease the project to initial state
  Click on OK Button
  Sleep  2s
  Click Publisher Ready Checkbox for Studies
  Sleep  5s
  Get back to home page and deselect project
