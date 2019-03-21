*** Settings ***
Documentation     Check Project Overview Funtionalities
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot


*** Test Cases ***
Check Project Overview Funtionalities
   Click on Project Overview Button
   Assert Table Head Has All Columns Names
   Assert Project gra2005's Version and Assigned Group
   Click on Next Button
   Assert Project robotprojectrelease4chrome-firefox-edge's Version and Assigned Group
   Click on Next Button
   Assert Project robotprojectrelease4ie's Version and Assigned Group
   Click on Previous Button
   Get back to german home page

*** Keywords ***
Click on Project Overview Button
   Click Element Through Tooltips   xpath=//a//span[contains(.,"Projektübersicht")]

Assert Table Head Has All Columns Names
   @{MD_THEADERS}    Create List   Projekt  Aktuelle Version   Zugewiesene Gruppe  Studie  Erhebungen  Instrumente   Datensätze   Fragen  Variablen
   :FOR   ${MD_TH}   IN  @{MD_THEADERS}
   \  Page Should Contain Element   xpath=//table[@class="table-striped table-hover ng-scope fdz-table-wide"]//thead//tr//th[contains(., "${MD_TH}")]

Assert Project gra2005's Version and Assigned Group
    Page Should Contain Element  xpath=//table[@class="table-striped table-hover ng-scope fdz-table-wide"]//tbody//tr//td[contains(., "1.0.1")]
    Page Should Contain Element  xpath=//table[@class="table-striped table-hover ng-scope fdz-table-wide"]//tbody//tr//td//span[contains(., "Publisher")]

Assert Project robotprojectrelease4chrome-firefox-edge's Version and Assigned Group
    @{MD_PROJECTS}    Create List   robotprojectrelease4chrome  robotprojectrelease4firefox   robotprojectrelease4edge
    :FOR   ${MD_PJ}   IN  @{MD_PROJECTS}
    \  Page Should Contain Element  xpath=//table[@class="table-striped table-hover ng-scope fdz-table-wide"]//tbody//tr//td[contains(., "nicht freigegeben")]
    \  Page Should Contain Element  xpath=//table[@class="table-striped table-hover ng-scope fdz-table-wide"]//tbody//tr//td//span[contains(., "Publisher")]

Assert Project robotprojectrelease4ie's Version and Assigned Group
    Page Should Contain Element  xpath=//table[@class="table-striped table-hover ng-scope fdz-table-wide"]//tbody//tr//td[contains(., "nicht freigegeben")]
    Page Should Contain Element  xpath=//table[@class="table-striped table-hover ng-scope fdz-table-wide"]//tbody//tr//td//span[contains(., "Publisher")]

Click on Next Button
    Click Element Through Tooltips   xpath=//ul//li[@class="pagination-next ng-scope"]//a[contains(., "Weiter")]

Click on Previous Button
    Click Element Through Tooltips   xpath=//ul//li[@class="pagination-prev ng-scope"]//a[contains(., "Zurück")]

