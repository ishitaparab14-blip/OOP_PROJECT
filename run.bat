@echo off
title CampusFind - Digital Lost and Found System
color 0A

echo =====================================================================
echo           CampusFind - Digital Lost and Found System
echo =====================================================================
echo.
echo [1/3] Checking Java installation...

where java >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Java was not found in your PATH.
    echo Please install Java 17 or Java 21 from: https://adoptium.net/
    pause
    exit /b 1
)

java -version
echo.

echo [2/3] Checking Maven Wrapper...
if not exist "mvnw.cmd" (
    echo [ERROR] mvnw.cmd not found in the current directory!
    pause
    exit /b 1
)

echo [3/3] Starting CampusFind Spring Boot Application...
echo The web server will start at: http://localhost:8080
echo Press Ctrl+C in this terminal when you want to stop the application.
echo =====================================================================
echo.

call mvnw.cmd spring-boot:run

if %errorlevel% neq 0 (
    echo.
    echo [NOTE] If Maven is not installed locally, you can install Maven or run via IntelliJ/Eclipse/VSCode.
    pause
)
