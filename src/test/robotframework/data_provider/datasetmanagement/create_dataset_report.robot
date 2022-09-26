*** Settings ***
Documentation     Upload a variable report template to generate variable report. Folder upload does not work in this case.
Metadata        Info on data    This test suite uses the project with the name "gra2005" which needs to be available in the testing environment
Library   OperatingSystem
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/search_resource.robot

*** Test Cases ***
Create Dataset Report
    Select project by name    gra2005
    Click on data set tab
    Search for    dat-gra2005-ds2$
    Click on first search result
    Click Report Button
    Write Dataset Report Version
    Page Should Not Contain Element  xpath=//md-toast[contains(@class,'md-accent')]
    Get back to home page and deselect project

*** Keywords ***
Get back to home page and deselect project
    Get back to german home page
    Click Element Through Tooltips    xpath=//md-sidenav//project-navbar-module//button[@aria-label='Clear Input']

Write Dataset Report Version
    Input Text   name=version   0.0.1
    Click on OK Button

Click Report Button
    Click Element Through Tooltips  xpath=//button//md-icon[contains(.,'pdf')]

Click on OK Button
   Click Element Through Tooltips  xpath=//md-dialog-actions//button//span[contains(.,'OK')]
