*** Settings ***
Documentation     Generate a data package overview.
Metadata        Info on data    This test suite uses the project with the name "gra2005" which needs to be available in the testing environment
Library           OperatingSystem
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/search_resource.robot

*** Test Cases ***
Create Data Package Overview
    Select project by name    gra2005
    Click on data package tab
    Click on first search result
    Click Overview Button
    Select English
    Write Overview Version
    Page Should Not Contain Element  xpath=//md-toast[contains(@class,'md-accent')]
    Get back to home page and deselect project

*** Keywords ***
Get back to home page and deselect project
    Get back to german home page
    Click Element Through Tooltips    xpath=//md-sidenav//project-navbar-module//button[@aria-label='Clear Input']

Write Overview Version
    Input Text   name=version   1.0.0
    Click on OK Button

Click Overview Button
    Click Element Through Tooltips  xpath=//button//md-icon[contains(.,'pdf')]

Click on OK Button
   Click Element Through Tooltips  xpath=//md-dialog-actions//button//span[contains(.,'OK')]

Select English
   Click Element Through Tooltips  xpath=//md-checkbox[@name="english"]
