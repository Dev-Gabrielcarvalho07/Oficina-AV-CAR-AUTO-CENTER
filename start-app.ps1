$ErrorActionPreference = 'Stop'

& "$PSScriptRoot\init-db.ps1"
& "$PSScriptRoot\start-backend.ps1"
& "$PSScriptRoot\start-frontend.ps1"

Write-Host ''
Write-Host 'Aplicacao pronta:'
Write-Host '  Frontend: http://127.0.0.1:4200'
Write-Host '  Backend:  http://localhost:7000'
