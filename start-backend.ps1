$ErrorActionPreference = 'Stop'

$portInUse = Get-NetTCPConnection -LocalPort 7000 -ErrorAction SilentlyContinue
if ($portInUse) {
    Write-Host 'Backend ja esta rodando em http://localhost:7000'
    exit 0
}

$m2 = Join-Path $env:USERPROFILE '.m2\repository'
if (-not (Test-Path $m2)) {
    throw 'Repositorio Maven local nao encontrado em ~/.m2/repository. Abra o projeto pela IDE/Maven uma vez para baixar as dependencias.'
}

$jars = Get-ChildItem $m2 -Recurse -Filter *.jar | Select-Object -ExpandProperty FullName
$classpath = [string]::Join(';', (@('target\classes') + $jars))

New-Item -ItemType Directory -Force -Path 'target\classes' | Out-Null

$sources = Get-ChildItem 'src\main\java' -Recurse -Filter *.java | Select-Object -ExpandProperty FullName
javac --release 17 -encoding UTF-8 -cp $classpath -d 'target\classes' $sources

Get-ChildItem 'src\main\resources' -File | Copy-Item -Destination 'target\classes' -Force

$runtimeClasspath = [string]::Join(';', (@('target\classes','src\main\resources') + $jars))
$argsLine = '-cp "' + $runtimeClasspath + '" br.com.avcar.Main'

Start-Process -FilePath java -ArgumentList $argsLine -WorkingDirectory $PSScriptRoot -RedirectStandardOutput backend.out.log -RedirectStandardError backend.err.log -WindowStyle Hidden
Start-Sleep -Seconds 4

if (-not (Get-NetTCPConnection -LocalPort 7000 -ErrorAction SilentlyContinue)) {
    Get-Content backend.err.log -ErrorAction SilentlyContinue
    throw 'Backend nao iniciou. Veja backend.err.log.'
}

Write-Host 'Backend rodando em http://localhost:7000'
