*** Settings ***
Documentation     Test for assigning and unassigning publications to a publisher project and verify it under data package tab
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/project_management_resource.robot

*** Test Cases ***
Assigning and unassigning publications to a publisher project and assert it
    Mark and edit the publication tab of the project
    Assign a publication to the project
    Get back to german home page
    Click on data package tab
    Click on first search result
    Assert the publication belongs to selected data package
    Unassign the publication from the project
    Get back to german home page
    Click on data package tab
    Click on first search result
    Assert the publication has been unassigend from the selected data package
    Get back to german home page
    click on cockpit button
    Switch To Settings Tab
    Click Publications Checkbox
    Get back to home page and deselect project

*** Keywords ***
Mark and edit the publication tab of the project
    Select project by name  robotprojectrelease4${BROWSER}
    click on cockpit button
    Switch To Settings Tab
    Click Publications Checkbox
    Switch To Status Tab
    Click on Publication Edit Button

Click on Publication Edit Button
   Click Element Through Tooltips    xpath=//md-card[@type="publications"]//button[@ng-disabled="ctrl.isProjectReleased()"]//span[text()="Bearbeiten"]

Assign a publication to the project
    Clear Element Text   xpath=//md-card//input
    Input Text  xpath=//md-card//input   The labour market's requirement profiles for higher education graduates
    Click Element Through Tooltips   xpath=//div//ul//li//span//span[contains(., "The labour market's requirement profiles for higher education graduates")]

Unassign the publication from the project
    Get back to german home page
    Select project by name  robotprojectrelease4${BROWSER}
    click on cockpit button
    Click on Publication Edit Button
    Delete the publication from the data package

Assert the publication belongs to selected data package
    Click on publications tab
    Wait Until Page Contains Element  xpath=//related-publication-search-result[contains(.,"The labour market's requirement profiles for higher education graduates")]

Assert the publication has been unassigend from the selected data package
    Click on publications tab
    Wait Until Page Contains Element  xpath=//md-card[contains(.,"Keine Suchergebnisse gefunden")]

Get back to home page and deselect project
    Get back to german home page
    Click Element Through Tooltips    xpath=//md-sidenav//project-navbar-module//button[@aria-label='Clear Input']

Delete the publication from the data package
    Click Element Through Tooltips    xpath=//button[@ng-click="ctrl.removePublication(publication)"]//md-icon[contains(.,"delete_forever")]
