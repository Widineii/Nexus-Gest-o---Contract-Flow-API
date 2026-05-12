# =====================================================================
# Nexus Gestao - Contract Flow | Script de execucao zero-config
# Faz download do Apache Maven se nao estiver instalado e sobe a API
# no perfil "local" (H2 em memoria), sem precisar de MySQL nem Docker.
# Uso: clicar 2x em run.bat OU rodar: powershell -File run.ps1
# =====================================================================
$ErrorActionPreference = "Stop"

$ProjectRoot = $PSScriptRoot
$MvnVersion  = "3.9.9"
$MvnHome     = Join-Path $ProjectRoot ".mvn\apache-maven-$MvnVersion"
$MvnCmd      = Join-Path $MvnHome "bin\mvn.cmd"

function Test-MavenOnPath {
    try {
        $null = Get-Command mvn -ErrorAction Stop
        return $true
    } catch {
        return $false
    }
}

function Install-PortableMaven {
    Write-Host ""
    Write-Host "[setup] Maven nao encontrado. Baixando Apache Maven $MvnVersion (uso unico, fica em .mvn\)..." -ForegroundColor Yellow
    $zipUrl   = "https://dlcdn.apache.org/maven/maven-3/$MvnVersion/binaries/apache-maven-$MvnVersion-bin.zip"
    $zipPath  = Join-Path $env:TEMP "apache-maven-$MvnVersion-bin.zip"
    $extract  = Join-Path $env:TEMP "nexus-mvn-extract"

    try {
        [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
        Invoke-WebRequest -Uri $zipUrl -OutFile $zipPath -UseBasicParsing
    } catch {
        Write-Host "[setup] Falha no mirror principal, tentando archive..." -ForegroundColor Yellow
        $zipUrl  = "https://archive.apache.org/dist/maven/maven-3/$MvnVersion/binaries/apache-maven-$MvnVersion-bin.zip"
        Invoke-WebRequest -Uri $zipUrl -OutFile $zipPath -UseBasicParsing
    }

    if (Test-Path $extract) { Remove-Item -Recurse -Force $extract }
    Expand-Archive -Path $zipPath -DestinationPath $extract -Force

    New-Item -ItemType Directory -Force -Path (Join-Path $ProjectRoot ".mvn") | Out-Null
    if (Test-Path $MvnHome) { Remove-Item -Recurse -Force $MvnHome }
    Move-Item -Path (Join-Path $extract "apache-maven-$MvnVersion") -Destination $MvnHome -Force

    Remove-Item -Force $zipPath
    Remove-Item -Recurse -Force $extract
    Write-Host "[setup] Maven instalado em $MvnHome" -ForegroundColor Green
}

function Resolve-MavenExe {
    if (Test-MavenOnPath) {
        return "mvn"
    }
    if (-not (Test-Path $MvnCmd)) {
        Install-PortableMaven
    }
    return $MvnCmd
}

function Resolve-JavaHome {
    if ($env:JAVA_HOME -and (Test-Path (Join-Path $env:JAVA_HOME "bin\java.exe"))) {
        return $env:JAVA_HOME
    }
    try {
        $props = & java -XshowSettings:properties -version 2>&1
        $line  = $props | Where-Object { $_ -match "java\.home\s*=\s*(.+)$" } | Select-Object -First 1
        if ($line -and ($line -match "java\.home\s*=\s*(.+)$")) {
            $home = $matches[1].Trim()
            if (Test-Path (Join-Path $home "bin\java.exe")) {
                return $home
            }
        }
    } catch { }

    $candidates = @(
        "$env:ProgramFiles\Java",
        "$env:ProgramFiles\Eclipse Adoptium",
        "$env:ProgramFiles\Microsoft",
        "$env:ProgramFiles\Zulu",
        "$env:ProgramFiles\Amazon Corretto",
        "$env:ProgramFiles\Common Files\Oracle\Java"
    )
    foreach ($dir in $candidates) {
        if (Test-Path $dir) {
            $jdk = Get-ChildItem -Path $dir -Directory -ErrorAction SilentlyContinue |
                   Where-Object { Test-Path (Join-Path $_.FullName "bin\java.exe") } |
                   Sort-Object Name -Descending |
                   Select-Object -First 1
            if ($jdk) { return $jdk.FullName }
        }
    }
    return $null
}

$resolvedJavaHome = Resolve-JavaHome
if (-not $resolvedJavaHome) {
    Write-Host "[erro] Nao consegui localizar um JDK 17+ valido. Instale o JDK e defina JAVA_HOME, ou rode 'winget install Microsoft.OpenJDK.21'." -ForegroundColor Red
    exit 1
}
$env:JAVA_HOME = $resolvedJavaHome
$env:Path      = "$env:JAVA_HOME\bin;$env:Path"
Write-Host "[setup] JAVA_HOME = $env:JAVA_HOME" -ForegroundColor DarkGray

$mvn = Resolve-MavenExe

Write-Host ""
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host " Nexus Gestao - Contract Flow" -ForegroundColor Cyan
Write-Host " Subindo em http://localhost:8080 (perfil local + H2)" -ForegroundColor Cyan
Write-Host " Swagger:    http://localhost:8080/swagger-ui.html" -ForegroundColor Cyan
Write-Host " H2 Console: http://localhost:8080/h2-console" -ForegroundColor Cyan
Write-Host "             (JDBC URL: jdbc:h2:mem:nexus  | user: sa)" -ForegroundColor Cyan
Write-Host " Login:      admin@nexus.com / admin123" -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host ""

Set-Location $ProjectRoot
& $mvn spring-boot:run "-Dspring-boot.run.profiles=local"
