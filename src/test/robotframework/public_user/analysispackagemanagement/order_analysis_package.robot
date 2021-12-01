*** Settings ***
Documentation     Tests the user experience of ordering the analysis package Test Analysis Package (testanalysepaket)
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/order_resource.robot

*** Test Cases ***
Put analysis packages in shopping cart
    Click Element   //a[@ui-sref="search({type: 'analysis_packages'})"]
    Click on first search result
    Wait for Angular    2s
    Put all analysis package versions in shopping cart
    Go to Shopping Cart
    Check for shopping cart items    Dieses Datenpaket    2
    Check for shopping cart items    Dieses Analysepaket    2
    Empty The Shopping Cart
    Get back to german home page

Test delete functionality
    Click Element   //a[@ui-sref="search({type: 'analysis_packages'})"]
    Click on first search result
    Wait for Angular    2s
    Put all analysis package versions in shopping cart
    Go to Shopping Cart
    Check for shopping cart items    Dieses Datenpaket    2
    Check for shopping cart items    Dieses Analysepaket    2
    Delete an Item    1
    Empty The Shopping Cart
    Get back to german home page

Order analysis packages
    Click Element   //a[@ui-sref="search({type: 'analysis_packages'})"]
    Click on first search result
    Wait for Angular    2s
    Put all analysis package versions in shopping cart
    Go to Shopping Cart
    Check for shopping cart items    Dieses Datenpaket    2
    Check for shopping cart items    Dieses Analysepaket    2
    Go to Shopping Cart
    Order
    Sleep   6s
    go back
    Go to Shopping Cart
    Empty The Shopping Cart
    Get back to german home page

*** Keywords ***
Check for shopping cart items
    [Arguments]    ${text}    ${limit}
    ${count}    get element count    //span[contains(., '${text}')]
    Should Be True  ${count} == ${limit}

