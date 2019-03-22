*** Settings ***
Documentation     Tests the related publications of study DZHW Graduate Panel 2005 and study series  "DZHW Graduate Survey Series"
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Test Cases ***
Check related publications of the study gra2005
    Click on study tab
    Search for   DZHW Absolventenpanel 2005
    Click on search result by id    stu-gra2005$
    Assert study gra2005 has five publications
    Click on publications related to study gra2005
    Click on search result by id    pub-Kratz.2016$
    Assert publication pub-Kratz.2016$ belongs to study gra2005
    Get back to german home page

Check related publications of the DZHW Graduate Survey Series
    Click on study tab
    Search for   DZHW Absolventenpanel 2005
    Click on search result by id    stu-gra2005$
    Assert study series DZHW Absolventenstudien has five publications
    Click on publications related to study series DZHW Absolventenstudien
    Click on search result by id    pub-Jaksztat.2014$
    Assert publication pub-Jaksztat.2014$ belongs to study series DZHW Absolventenstudien
    Get back to german home page


*** Keywords ***
Assert study gra2005 has five publications
    Element Should Contain  xpath=//div[@class="fdz-truncate-string ng-scope flex"]//a[contains(., "Publikationen zu dieser Studie: (5)")]  Publikationen zu dieser Studie: (5)

Click on publications related to study gra2005
    Click Element Through Tooltips   xpath=//div[@class="fdz-truncate-string ng-scope flex"]//a[contains(., "Publikationen zu dieser Studie: (5)")]

Assert publication pub-Kratz.2016$ belongs to study gra2005
    Element Should Contain  xpath=//div[@class="fdz-truncate-string ng-scope flex"]//a//span[contains(., "DZHW-Absolventenpanel 2005")]  DZHW-Absolventenpanel 2005

Assert study series DZHW Absolventenstudien has five publications
    Element Should Contain  xpath=//div[@class="ng-scope"]//a[contains(., "Publikationen zur Studienreihe")]   Publikationen zur Studienreihe "DZHW-Absolventenstudien": (5)

Click on publications related to study series DZHW Absolventenstudien
    Click Element Through Tooltips  xpath=//div[@class="ng-scope"]//a[contains(., "Publikationen zur Studienreihe")]

Assert publication pub-Jaksztat.2014$ belongs to study series DZHW Absolventenstudien
    Element Should Contain  xpath=//div[@class="ng-binding ng-scope"]//a[contains(., "DZHW-Absolventenstudien")]  DZHW-Absolventenstudien



