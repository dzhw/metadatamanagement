ECHO OFF

start cmd /k "cd ..\..\.. & robot -d target\test\robotframework\logs -v BROWSER:firefox .\src\test\robotframework\public_user"
