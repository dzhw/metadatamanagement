*** Settings ***
Documentation  Test editing survey page and versioning
Resource  ../resources/login_resource.robot
Resource  ../resources/home_page_resource.robot
Resource  ../resources/search_resource.robot
Force Tags  new

*** Test Cases ***
Editing survey and check versioning
  Login as dataprovider
  Select project by name  fileuploadproject
  Click on surveys tab
  Click Survey Edit Button
  Input Text  name=titleDe  Test1337
  Click Element Through Tooltips xpath=//button[@type='submit']
  Click Element Through Tooltips xpath=//button[text()='undo']
  Page Should Contain  a few seconds ago
  Click Element Through Tooltips xpath=//button[text()='Abbrechen']
  Input Text  name=titleDe  Test
  Click Element Through Tooltips xpath=//button[@type='submit']
  [Teardown]  Get back to home page and logout


*** Keywords ***
Get back to home page and logout
  Get back to home page
  ${present}=  Run Keyword And Return Status    Page Should Contain  Sie haben ungespeicherte Änderungen.
  Run Keyword If    ${present} == 'True'   Click Element Through Tooltips  xpath=//button[contains(.,'Ja')]
  Click Element Through Tooltips xpath=//button[@id='logout']