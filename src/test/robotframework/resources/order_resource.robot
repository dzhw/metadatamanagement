*** Settings ***
Documentation     Resource used by order package test cases for the german language
Library           SeleniumLibrary
Library           AngularJSLibrary  root_selector=[data-ng-app]
Resource          click_element_resource.robot

*** Keywords ***
Put all analysis package versions in shopping cart
    @{MD_VERSIONS}   Create List    1.0.1  1.0.0
    FOR  ${INDEX}  IN RANGE  0   2
       Select Version   ${MD_VERSIONS}[${INDEX}]
       Put in Shopping Cart
    END

Select Version
     [Arguments]   ${versionname}
     Click Element Through Tooltips    //md-select[@name='version']
     Click Element Through Tooltips    //md-select-menu//md-option[contains(., '${versionname}')]

Put in Shopping Cart
   Click Element Through Tooltips    //span[contains(., 'In den Warenkorb legen')]

Order
    wait until page contains element    //span[contains(., 'Kostenlos bestellen')]
    Click Element Through Tooltips      //span[contains(., 'Kostenlos bestellen')]

Go to Shopping Cart
    wait until page contains element    //a//md-icon[contains(., 'shopping_cart')]
    Click Element Through Tooltips      //a//md-icon[contains(., 'shopping_cart')]

Delete an Item
    [Arguments]   ${limit}
    Click Element Through Tooltips   //button[normalize-space()='Löschen']

Check Item Count
    [Arguments]  ${expected}
    ${count} =  Get Element Count   //md-card[contains(@class, 'fdz-search-result')]
    Should Be True    ${count} == ${expected}

Empty The Shopping Cart
   Click Element Through Tooltips   //span[contains(., 'Warenkorb leeren')]
