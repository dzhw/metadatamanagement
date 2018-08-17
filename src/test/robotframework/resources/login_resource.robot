*** Settings ***
Documentation     Resource used by all search and view test cases
Library     ExtendedSelenium2Library
Resource    click_element_resource.robot
Variables   ../common_variables.yaml

*** Keywords ***

Login as dataprovider
  Click Element Through Tooltips  xpath=//*[@id = 'account-menu-toggle']
  Click Element Through Tooltips  xpath=//*[@id = 'login']
  Input Text  id=username  dataprovider
  Input Password  id=password  dataprovider
  Click Element Through Tooltips  xpath=//button[@type='submit']

Login as publisher after logout
  Click Element Through Tooltips  xpath=//*[@id = 'login']
  Input Text  id=username  publisher
  Input Password  id=password  publisher
  Click Element Through Tooltips  xpath=//button[@type='submit']

Delete Robotsproject
  Pass Execution If    '${BROWSER}' == 'ie'  Study Creation not possible in IE
  Get back to german home page
  ${present}=  Run Keyword And Return Status    Page Should Contain  Sie haben ungespeicherte Änderungen.
  Run Keyword If    ${present} == 'True'   Click Element Through Tooltips  xpath=//button[contains(.,'Ja')]
  Click Element Through Tooltips  xpath=//button[contains(.,'abmelden')]
  Login as publisher after logout
  Input Text			xpath=//input[@placeholder = 'Projekt auswählen']		robotsproject
  Click Element Through Tooltips		xpath=//md-sidenav/descendant::button[md-icon[text()='']]
  Click Element Through Tooltips		xpath=//button[text()='OK']
  Click Element Through Tooltips  xpath=//button[contains(.,'abmelden')]

Save Changes
  Click Element Through Tooltips  xpath=//ui-view/descendant::button[md-icon[text()='save']]
