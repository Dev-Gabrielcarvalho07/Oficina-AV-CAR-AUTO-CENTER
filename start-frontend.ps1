$ErrorActionPreference = 'Stop'

$frontendDir = Join-Path $PSScriptRoot 'FrontEnd\avcar-frontend'

$portInUse = Get-NetTCPConnection -LocalPort 4200 -ErrorAction SilentlyContinue
if ($portInUse) {
    Write-Host 'Frontend ja esta rodando em http://127.0.0.1:4200'
    exit 0
}

if (-not (Test-Path (Join-Path $frontendDir 'node_modules'))) {
    Push-Location $frontendDir
    npm install
    Pop-Location
}

Start-Process -FilePath npm.cmd -ArgumentList 'start -- --host 127.0.0.1 --port 4200' -WorkingDirectory $frontendDir -RedirectStandardOutput (Join-Path $frontendDir 'ng-serve.out.log') -RedirectStandardError (Join-Path $frontendDir 'ng-serve.err.log') -WindowStyle Hidden
Start-Sleep -Seconds 8

if (-not (Get-NetTCPConnection -LocalPort 4200 -ErrorAction SilentlyContinue)) {
    Get-Content (Join-Path $frontendDir 'ng-serve.err.log') -ErrorAction SilentlyContinue
    throw 'Frontend nao iniciou. Veja FrontEnd\avcar-frontend\ng-serve.err.log.'
}

Write-Host 'Frontend rodando em http://127.0.0.1:4200'
