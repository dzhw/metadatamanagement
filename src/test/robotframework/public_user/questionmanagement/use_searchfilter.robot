*** Settings ***
Documentation     Tests the user experience of using a survey filter for finding a connected question
Force Tags        smoketest
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Test Cases ***
Use of Searchfilters to find a question
    Click on questions tab
    Activate Filter by name    Erhebung
    Choose Filter Option by id    sur-gra2005-sy1$
    Click on search result by id    que-gra2005-ins1-1.1$
    Page Should Contain    Verbundene Objekte
    [Teardown]    Get back to german home page
