@IF "%DEBUG%" == "" @ECHO OFF
@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script with full whitespace protection for Windows
@REM ----------------------------------------------------------------------------

setlocal EnableExtensions EnableDelayedExpansion

set "DIRNAME=%~dp0"
if "%DIRNAME:~-1%"=="\" set "DIRNAME=%DIRNAME:~0,-1%"
set "MAVEN_PROJECTBASEDIR=%DIRNAME%"

@REM Locate java
set "JAVACMD=java.exe"
if not "%JAVA_HOME%" == "" (
    if exist "%JAVA_HOME%\bin\java.exe" (
        set "JAVACMD=%JAVA_HOME%\bin\java.exe"
    )
)

set "WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"

if exist "%WRAPPER_JAR%" goto runWrapper

echo Downloading maven-wrapper.jar...
powershell -NoProfile -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object Net.WebClient).DownloadFile('https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar', '%WRAPPER_JAR%')"

if not exist "%WRAPPER_JAR%" (
    echo [ERROR] Failed to download maven-wrapper.jar. Please ensure internet access is available.
    exit /b 1
)

:runWrapper
"%JAVACMD%" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" -cp "%WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*
exit /b %ERRORLEVEL%
