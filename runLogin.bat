@echo off
echo Compiling Java Program...
javac Login.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed.
    pause
    exit /b
)

echo Running Java Program...
java Login

pause
