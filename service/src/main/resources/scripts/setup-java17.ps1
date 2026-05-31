# ============================================
# Java 17 + VS Code + Gradle Setup Script
# ============================================

Write-Host "Starting Java 17 setup..." -ForegroundColor Green

# -----------------------------
# VARIABLES
# -----------------------------
$downloadUrl = "https://github.com/adoptium/temurin17-binaries/releases/latest/download/OpenJDK17U-jdk_x64_windows_hotspot_17.0.16_8.msi"
$tempInstaller = "$env:TEMP\java17.msi"
$javaHome = "C:\Program Files\Eclipse Adoptium\jdk-17"
$vsCodeSettingsDir = "$env:APPDATA\Code\User"
$vsCodeSettingsFile = "$vsCodeSettingsDir\settings.json"

# -----------------------------
# DOWNLOAD JAVA 17
# -----------------------------
Write-Host "Downloading Java 17..." -ForegroundColor Yellow
Invoke-WebRequest -Uri $downloadUrl -OutFile $tempInstaller

# -----------------------------
# INSTALL JAVA 17
# -----------------------------
Write-Host "Installing Java 17..." -ForegroundColor Yellow
Start-Process msiexec.exe -Wait -ArgumentList "/i `"$tempInstaller`" /quiet"

# -----------------------------
# VERIFY INSTALLATION DIRECTORY
# -----------------------------
$jdkFolder = Get-ChildItem "C:\Program Files\Eclipse Adoptium" -Directory |
    Where-Object { $_.Name -like "jdk-17*" } |
    Select-Object -First 1

if ($jdkFolder -eq $null) {
    Write-Host "Java 17 installation not found!" -ForegroundColor Red
    exit 1
}

$javaHome = $jdkFolder.FullName

Write-Host "JAVA_HOME detected as: $javaHome" -ForegroundColor Cyan

# -----------------------------
# SET JAVA_HOME
# -----------------------------
[Environment]::SetEnvironmentVariable("JAVA_HOME", $javaHome, "Machine")

# -----------------------------
# UPDATE PATH
# -----------------------------
$oldPath = [Environment]::GetEnvironmentVariable("Path", "Machine")

if ($oldPath -notlike "*$javaHome\\bin*") {
    $newPath = "$oldPath;$javaHome\bin"
    [Environment]::SetEnvironmentVariable("Path", $newPath, "Machine")
}

Write-Host "JAVA_HOME and PATH updated" -ForegroundColor Green

# -----------------------------
# CREATE VS CODE SETTINGS FOLDER
# -----------------------------
if (!(Test-Path $vsCodeSettingsDir)) {
    New-Item -ItemType Directory -Path $vsCodeSettingsDir -Force
}

# -----------------------------
# CONFIGURE VS CODE JAVA RUNTIME
# -----------------------------
$settingsJson = @"
{
    "java.configuration.runtimes": [
        {
            "name": "JavaSE-17",
            "path": "$($javaHome.Replace('\\','/'))",
            "default": true
        }
    ]
}
"@

Set-Content -Path $vsCodeSettingsFile -Value $settingsJson

Write-Host "VS Code configured for Java 17" -ForegroundColor Green

# -----------------------------
# VERIFY JAVA INSTALLATION
# -----------------------------
Write-Host "Checking Java version..." -ForegroundColor Yellow
& "$javaHome\bin\java.exe" -version

# -----------------------------
# VERIFY GRADLE WRAPPER
# -----------------------------
if (Test-Path ".\gradlew.bat") {
    Write-Host "Gradle wrapper found" -ForegroundColor Green
    .\gradlew.bat -version
} else {
    Write-Host "gradlew.bat not found in current folder" -ForegroundColor Yellow
}

Write-Host "Setup completed successfully!" -ForegroundColor Green