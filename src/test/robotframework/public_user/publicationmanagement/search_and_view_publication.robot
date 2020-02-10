*** Settings ***
Documentation     Tests the user experience of searching & finding the Graduate Panel 2005 and opening a publication page
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Test Cases ***
Looking for Bildungsherkunft und Promotionen
    Navigate to search
    Search for    Absolventenpanel 2005
    Click on first search result
    Click on publications tab
    Search for in details   Bildungsherkunft und Promotionen
    Click on first search result
    Page Should Contain    http://www.zfs-online.org/index.php/zfs/article/view/3175/2712
    [Teardown]    Get back to german home page

Looking for Graduate Panel 2005s individual data bachelor in english
    [Setup]    Change language to english
    Navigate to search
    Search for    Graduate Panel 2005
    Click on first search result
    Click on publications tab
    Search for in details   Bildungsherkunft und Promotionen
    Click on first search result
    Page Should Contain    http://www.zfs-online.org/index.php/zfs/article/view/3175/2712
    [Teardown]    Get back to german home page
