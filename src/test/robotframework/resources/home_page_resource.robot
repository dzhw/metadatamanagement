*** Settings ***
Documentation     Resource used by all search and view test cases
Library           ExtendedSelenium2Library
Variables         ../common_variables.yaml

*** Keywords ***
Get back to home page
           Click Element Through Tooltips  xpath=//*[@id="SideNavBar"]/md-toolbar/a
Change language to english
           Wait Until Keyword Succeeds  5s  1s  Click Button  id=changeLanguageToEn
Get back to german home page
           Click Element Through Tooltips  xpath=//*[@id = 'changeLanguageToDe']
           Get back to home page
