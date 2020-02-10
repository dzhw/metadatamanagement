*** Settings ***
Documentation     Tests the user experience of searching & finding the Graduate Panel 2005 and opening a data set page
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Test Cases ***
Looking for Absolventenpanel 2005s individual data bachelor in german
    Navigate to search
    Search for    Absolventenpanel 2005
    Click on first search result
    Click on data set tab
    Search for in details   Personendatensatz traditionell
    Click on first search result
    Page Should Contain    Kann nach Abschluss eines Datennutzungsvertrags heruntergeladen werden
    [Teardown]    Get back to german home page
