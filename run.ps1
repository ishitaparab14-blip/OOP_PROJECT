# CampusFind - PowerShell Startup Script
Write-Host "=====================================================================" -ForegroundColor Cyan
Write-Host "          CampusFind - Digital Lost and Found System" -ForegroundColor Green
Write-Host "=====================================================================" -ForegroundColor Cyan
Write-Host ""

# Verify Java
Write-Host "[1/3] Checking Java environment..." -ForegroundColor Yellow
$javaCmd = Get-Command java -ErrorAction SilentlyContinue
if (-not $javaCmd) {
    Write-Host "[ERROR] Java is not installed or not in PATH." -ForegroundColor Red
    Write-Host "Please install Java (JDK 17 or 21) from https://adoptium.net/" -ForegroundColor White
    Read-Host "Press Enter to exit..."
    exit 1
}

Write-Host "Found Java: $($javaCmd.Source)" -ForegroundColor Green

# Verify Maven / Wrapper
Write-Host "[2/3] Checking Maven Wrapper..." -ForegroundColor Yellow
if (Test-Path ".\mvnw.cmd") {
    Write-Host "[3/3] Starting Spring Boot Server on http://localhost:8080 ..." -ForegroundColor Cyan
    Write-Host "Default Demo Accounts:" -ForegroundColor White
    Write-Host "  Admin:   admin@campusfind.edu / Admin@123Password" -ForegroundColor Gray
    Write-Host "  Student: aarav.student@campusfind.edu / Password@123" -ForegroundColor Gray
    Write-Host ""
    .\mvnw.cmd spring-boot:run
} elseif (Get-Command mvn -ErrorAction SilentlyContinue) {
    mvn spring-boot:run
} else {
    Write-Host "[ERROR] Neither mvn nor mvnw.cmd was found." -ForegroundColor Red
}
