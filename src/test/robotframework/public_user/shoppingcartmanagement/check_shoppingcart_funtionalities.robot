*** Settings ***
Documentation     As a public user checking the shopping cart funtionalisties
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot


*** Test Cases ***
Check Shopping Cart as a Public User
   Click on search result by id    stu-gra2005$
   Select Item and Put in The Cart
   Check Shopping Cart
   Confirm Order
   Close The Toast Message
   Check The Links
   Check Delete Button is Available
   Empty The Shopping Cart
   Get back to german home page   # to sync with next test flow


*** Keywords ***
Select Item and Put in The Cart
   @{MD_ACCESSWAYNAMES}   Create List    download-suf  remote-desktop-suf   onsite-suf   download-cuf
   :FOR  ${MD_ACC}  IN  @{MD_ACCESSWAYNAMES}
   \  Click On Add Shopping Cart Icon
   \  Assert Study Input is Disabled
   \  Select Access Way for the Datasets from The List   ${MD_ACC}
   \  Select Version the Datasets from The List   1.0.1
   \  Check The Close Button is Available
   \  Put in Shopping Cart

Assert Item Numbers
   @{MD_ITEMSITERATION}   Create List   1   2   3   4
   :FOR  ${MD_IT}  IN  @{MD_ITEMSITERATION}
   \  Page Should Contain Element   xpath=//span[@ng-if='productsCount'][contains(., '${MD_IT}')]

Click On Add Shopping Cart Icon
   Click Element Through Tooltips   xpath=//md-icon[contains(., 'add_shopping_cart')]

Assert Study Input is Disabled
   Element Should Be Disabled    xpath=//input[@name='studyTitle']

Select Access Way for the Datasets from The List
   [Arguments]   ${accesswayname}
   Click Element Through Tooltips    xpath=//md-select[@name='accessWay']
   Click Element Through Tooltips    xpath=//md-select-menu//md-option[contains(., '${accesswayname}')]

Select Version the Datasets from The List
   [Arguments]   ${versionname}
   Click Element Through Tooltips    xpath=//md-select[@name='version']
   Click Element Through Tooltips    xpath=//md-select-menu//md-option[contains(., '${versionname}')]

Put in Shopping Cart
   Click Element Through Tooltips    xpath=//span[contains(., 'In den Einkaufswagen')]

Check Shopping Cart
   Click Element Through Tooltips    xpath=//a//md-icon[contains(., 'shopping_cart')]

Check Delete Button is Available
   Page Should Contain Element    xpath=//button[md-icon[text()='delete_forever']]

Check The Close Button is Available
   Page Should Contain Element    xpath=//span[contains(., 'Schließen')]

Confirm Order
   Click Element Through Tooltips   xpath=//span[contains(., 'Beantragen')]

Close The Toast Message
   Click Element Through Tooltips  xpath=//md-toast//span[contains(., 'Sie haben noch nicht alle benötigten')]
   Element Should Contain  xpath=//md-toast//span[contains(.,'Sie haben noch nicht alle benötigten')]  Sie haben noch nicht alle benötigten
   Click Element Through Tooltips  xpath=//button//following::md-icon[contains(.,"close")]

Check The Links
    @{MD_DATALINKS}   Create List    Studienreihe   Variablen   Datens
    :FOR   ${MD_LK}   IN  @{MD_DATALINKS}
    \   Click Element Through Tooltips   xpath=//a[contains(., '${MD_LK}')]
    \   Go Back

Empty The Shopping Cart
   Click Element Through Tooltips   xpath=//span[contains(., 'Einkaufswagen leeren')]






