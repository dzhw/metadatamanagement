*** Settings ***
Documentation     Tests the user experience of ordering the analysis package Test Analysis Package (testanalysepaket)
Metadata          Info on data    This test suite uses the analysis package 'testanalysispackage'
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/order_resource.robot

*** Test Cases ***
Put analysis packages in shopping cart
    wait until page contains element    //a[@ui-sref="search({type: 'analysis_packages'})"]
    Click Element   //a[@ui-sref="search({type: 'analysis_packages'})"]
    Search for    Test Analysepaket
    Click on first search result
    Wait for Angular    2s
    Put all analysis package versions in shopping cart
    Go to Shopping Cart
    # TODO: remove data dependency (number of items in cart depends on data)
    #Check for shopping cart items    Dieses Datenpaket    2
    Check for shopping cart items    Dieses Analysepaket    2
    Empty The Shopping Cart
    [Teardown]    Get back to german home page

Test delete functionality
    wait until page contains element    //a[@ui-sref="search({type: 'analysis_packages'})"]
    Click Element   //a[@ui-sref="search({type: 'analysis_packages'})"]
    Click on first search result
    Wait for Angular    2s
    Put all analysis package versions in shopping cart
    Go to Shopping Cart
    # TODO: remove data dependency (number of items in cart depends on data)
    #Check for shopping cart items    Dieses Datenpaket    2
    Check for shopping cart items    Dieses Analysepaket    2
    Delete an Item    1
    Empty The Shopping Cart
    [Teardown]    Get back to german home page

Order analysis packages
    Pass Execution If    '${BROWSER}' == 'ie'         Package Creation not possible in IE
    Pass Execution If    '${BROWSER}' == 'firefox'    Package Creation not possible in Firefox
    Pass Execution If    '${BROWSER}' == 'safari'     Package Creation not possible in Safari
    wait until page contains element    //a[@ui-sref="search({type: 'analysis_packages'})"]
    Click Element   //a[@ui-sref="search({type: 'analysis_packages'})"]
    Click on first search result
    Wait for Angular    2s
    Put all analysis package versions in shopping cart
    Go to Shopping Cart
    Order
    Sleep   6s
    go back
    Go to Shopping Cart
    Empty The Shopping Cart
    [Teardown]    Get back to german home page

*** Keywords ***
Check for shopping cart items
    [Arguments]    ${text}    ${limit}
    ${count}    get element count    //span[contains(., '${text}')]
    Should Be True  ${count} == ${limit}

