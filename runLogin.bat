@echo off
echo Compiling Java Program...
javac LoginBani.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed.
    pause
    exit /b
)

echo Running Java Program...
java LoginBani

pause
