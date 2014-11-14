@echo off
echo [INFO] Be careful!This operation will refresh the database
pause

%~d0
cd %~dp0
cd ..

call mvn antrun:run -Prefresh-db

cd bin
pause