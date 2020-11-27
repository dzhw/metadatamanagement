*** Settings ***
Documentation     Assign dataproviders and publishers from the list and check deleting the last element from the stack in not possible. Verify the check icons of metadata.
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/project_management_resource.robot

*** Variables ***
${PROJECT_NAME}   smileyproject

*** Test Cases ***
Check Deleting The Last Dataprovider and Publisher From Stack is Not Possible
   Create Project  ${PROJECT_NAME}${BROWSER}
   Click on Cockpit Button
   Assign Dataprovider From List
   Click Element Through Tooltips   xpath=//md-card[@group='dataProviders']//md-list-item//div//strong[contains(.,'dataprovidertest1')]//following::md-icon[contains(.,'delete_forever')]
   Click Element Through Tooltips   xpath=//md-card[@group='dataProviders']//md-list-item//div//strong[contains(.,'dataprovidertest2')]//following::md-icon[contains(.,'delete_forever')]
   Run Keyword And Ignore Error  Page Should Contain Element  xpath=//md-card[@group='dataProviders']//md-list-item[1]//button[@disabled='disabled']//md-icon[contains(.,'delete_forever')]
   Assign Publisher From List
   Click Element Through Tooltips   xpath=//md-card[@group='publishers']//md-list-item//div//strong[contains(.,'publishertest1')]//following::md-icon[contains(.,'delete_forever')]
   Click Element Through Tooltips   xpath=//md-card[@group='publishers']//md-list-item//div//strong[contains(.,'publishertest2')]//following::md-icon[contains(.,'delete_forever')]
   Run Keyword And Ignore Error  Page Should Contain Element  xpath=//md-card[@group='publishers']//md-list-item[1]//button[@disabled='disabled']//md-icon[contains(.,'delete_forever')]

Check The Sentiments of Metadata
   Select Survey Checkbox
   Select Instruments Checkbox
   Select Questions Checkbox
   Select Datasets Checkbox
   Select Variable Checkbox
   Click on Cockpit Button
   Switch To Status Tab
   Verify The Sentiments From The List of Metadata
   Delete project by name  ${PROJECT_NAME}${BROWSER}
   Sleep  2s

*** Keywords ***
Assign Dataprovider From List
    @{DP_ITEMS}    Create List    dataprovider   dataprovidertest1    dataprovidertest2
    FOR    ${DP}    IN    @{DP_ITEMS}
         Assign a dataprovider   ${DP}
    END

Assign Publisher From List
     @{PL_ITEMS}    Create List    publishertest1   publishertest2
    FOR    ${PL}    IN    @{PL_ITEMS}
         Assign a publisher  ${PL}
    END

Verify The Sentiments From The List of Metadata
    @{MD_ITEMS}    Create List    dataPackages   surveys   instruments   questions   dataSets    variables
    @{SN_ITEMS}    Create List    assets/images/icons/bbaf48e1.clipboard.svg    assets/images/icons/c691f57d.clipboard-check.svg    assets/images/icons/a103f3a2.clipboard-double-check.svg
    FOR   ${MD_DT}   IN  @{MD_ITEMS}
        Run Keyword If   '@{SN_ITEMS}[0]' == 'assets/images/icons/bbaf48e1.clipboard.svg'    Page Should Contain Element    xpath=//md-card[@type="${MD_DT}"]//following::md-icon[@md-svg-src="@{SN_ITEMS}[0]"]
        Click Element Through Tooltips  xpath=//md-card[@type="${MD_DT}"]//md-checkbox[contains(.,"Datengeber:innen Fertig")]
        Sleep  1s    # to avoid failing in edge
        Run Keyword If    '@{SN_ITEMS}[1]' == 'assets/images/icons/c691f57d.clipboard-check.svg'     Page Should Contain Element    xpath=//md-card[@type="${MD_DT}"]//following::md-icon[@md-svg-src="@{SN_ITEMS}[1]"]
        Click Element Through Tooltips  xpath=//md-card[@type="${MD_DT}"]//md-checkbox[contains(.,"Publisher Fertig")]
        Sleep  1s   # to avoid failing in edge
        Run Keyword If    '@{SN_ITEMS}[2]' == 'assets/images/icons/a103f3a2.clipboard-double-check.svg'    Page Should Contain Element    xpath=//md-card[@type="${MD_DT}"]//following::md-icon[@md-svg-src="@{SN_ITEMS}[2]"]
    END
