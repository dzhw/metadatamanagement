*** Settings ***
Documentation     Tests the user experience of searching & finding a question through the use of filters
Resource    ../resources/search_resource.robot
Resource    ../resources/home_page_resource.robot

*** Test Cases ***
Use of Searchfilters to find XY
  Click on questions tab
  Click Filter Selection
  Choose Third Filter
  Choose Fourth Filteroption
  Click on search result by id  que-gra2005-ins2-1.1$
  Page Should Contain  Verbundene Objekte
  [Teardown]  Get back to home page

*** Keywords ***
Click Filter Selection
  Wait Until Keyword Succeeds    5s    1s    Click Element Through Tooltips  xpath=//search-filter-panel//md-select
Choose Third Filter
  Wait Until Keyword Succeeds    5s    1s    Click Element Through Tooltips  xpath=//md-select-menu//md-option[3]
Choose Fourth Filteroption
  Wait Until Keyword Succeeds    5s    1s    Click Element Through Tooltips  xpath=//md-virtual-repeat-container//li[4]
