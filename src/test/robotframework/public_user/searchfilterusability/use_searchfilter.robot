*** Settings ***
Documentation     Tests the user experience of searching & finding the Graduate Panel 2005 and opening a variable page
Resource    ../resources/search_resource.robot
Resource    ../resources/home_page_resource.robot

*** Test Cases ***

Use of Searchfilters to find XY
  Click on questions tab
  Click Element Through Tooltips  xpath=//search-filter-panel//md-select
  Click Element Through Tooltips  xpath=//md-select-menu//md-option[3]
  Click Element Through Tooltips  xpath=//md-virtual-repeat-container//li[4]
  Click on search result by id  que-gra2005-ins2-1.1$
  Page Should Contain  Verbundene Objekte
  [Teardown]  Get back to home page
