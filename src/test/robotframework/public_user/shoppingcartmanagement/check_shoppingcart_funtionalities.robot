*** Settings ***
Documentation     As a public user checking the shopping cart funtionalities
Metadata          Info on data    This test suite uses the "gra2005" project
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/order_resource.robot

*** Test Cases ***
Check Shopping Cart as a Public User
   Click Element Through Tooltips  xpath=//a[contains(@ui-sref, 'Absolventen')]
   Click Element Through Tooltips  xpath=//a[contains(., 'DZHW-Absolventenpanel 2005')]
   Put all access ways in shopping cart
   Go to Shopping Cart
   Delete an Item   1 # we have 4 items in the cart and we delete one item
   Check Item Count  3
   Check The Links
   Empty The Shopping Cart
   Get back to german home page   # to sync with next test flow


*** Keywords ***
Put all access ways in shopping cart
   @{MD_ACCESSWAYNAMES}   Create List    SUF: Download  SUF: Remote-Desktop   SUF: On-Site   CUF: Download
   FOR  ${INDEX}  IN RANGE  0   4
      Select Access Way for the Data Package   ${MD_ACCESSWAYNAMES}[${INDEX}]
      Put in Shopping Cart
   END

Select Access Way for the Data Package
   [Arguments]   ${accesswayname}
   Click Element Through Tooltips    xpath=//md-select[@name='accessWay']
   Click Element Through Tooltips    xpath=//md-select-menu//md-option[contains(., '${accesswayname}')]

Select Version for the Data Package
   [Arguments]   ${versionname}
   Click Element Through Tooltips    xpath=//md-select[@name='version']
   Click Element Through Tooltips    xpath=//md-select-menu//md-option[contains(., '${versionname}')]

Put in Shopping Cart
   Click Element Through Tooltips    xpath=//span[contains(., 'In den Warenkorb legen')]

Go to Shopping Cart
   Click Element Through Tooltips    xpath=//a//md-icon[contains(., 'shopping_cart')]
   ${count} =  Get Element Count   //md-card[contains(@class, 'fdz-search-result')]
   Should Be True    ${count} == 4

Check The Links
    @{MD_DATALINKS}   Create List    Variablen   Datensätze
    FOR   ${MD_LK}   IN  @{MD_DATALINKS}
        Click Element Through Tooltips   xpath=//a[contains(., '${MD_LK}')]
        Go Back
    END

Empty The Shopping Cart
   Click Element Through Tooltips   xpath=//span[contains(., 'Warenkorb leeren')]
