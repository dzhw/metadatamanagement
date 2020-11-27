*** Settings ***
Documentation     Check Project Overview Funtionalities
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Test Cases ***
Check Project Overview Funtionalities
   Click on Project Overview Button
   Assert Table Head Has All Columns Names
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
   @{MD_THEADERS}    Create List   Projekt  Aktuelle Version   Zugewiesene Gruppe  Datenpaket  Erhebungen  Instrumente   Datensätze   Fragen  Variablen
   FOR   ${MD_TH}   IN  @{MD_THEADERS}
      Page Should Contain Element   xpath=//table//thead//tr//th[contains(., "${MD_TH}")]
   END

Assert Project robotprojectrelease4chrome-firefox-edge's Version and Assigned Group
   @{MD_PROJECTS}    Create List   robotprojectrelease4chrome  robotprojectrelease4firefox   robotprojectrelease4edge   robotprojectrelease4safari
   FOR   ${MD_PJ}   IN  @{MD_PROJECTS}
      Page Should Contain Element  xpath=//table//tbody//tr//td[contains(., "nicht freigegeben")]
      Page Should Contain Element  xpath=//table//tbody//tr//td//span[contains(., "Publisher")]
   END

Assert Project robotprojectrelease4ie's Version and Assigned Group
   Page Should Contain Element  xpath=//table//tbody//tr//td[contains(., "nicht freigegeben")]
   Page Should Contain Element  xpath=//table//tbody//tr//td//span[contains(., "Publisher")]

Click on Next Button
   Click Element Through Tooltips   xpath=//ul//li[@class="pagination-next ng-scope"]//a[contains(., "Weiter")]
   Sleep  1s

Click on Previous Button
   Click Element Through Tooltips   xpath=//ul//li[@class="pagination-prev ng-scope"]//a[contains(., "Zurück")]
   Sleep  1s
