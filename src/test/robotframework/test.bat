ECHO OFF



start cmd /k "cd ..\..\.. & robot -P src\test\robotframework\libs -d target\test\robotframework\logs -v BROWSER:ie .\src\test\robotframework"
