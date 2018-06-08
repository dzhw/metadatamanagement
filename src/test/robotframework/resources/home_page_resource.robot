*** Settings ***
Documentation     Resource used by all search and view test cases
Library     ExtendedSelenium2Library
Resource    click_element_resource.robot
Variables   ../common_variables.yaml

*** Keywords ***
Change language to english
  Wait Until Keyword Succeeds  5s  0.5s  Click Button  id=changeLanguageToEn

Get back to german home page
  ${url} =  Get Location
  Run Keyword If   '/en/' in '${url}'    Click Element Through Tooltips  xpath=//*[@id = 'changeLanguageToDe']
  Click Element Through Tooltips  xpath=//*[@id='SideNavBar']/md-toolbar/a
