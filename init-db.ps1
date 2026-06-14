$ErrorActionPreference = 'Stop'

$psql = 'C:\Program Files\PostgreSQL\14\bin\psql.exe'
if (-not (Test-Path $psql)) {
    $found = Get-ChildItem 'C:\Program Files\PostgreSQL' -Recurse -Filter psql.exe -ErrorAction SilentlyContinue | Select-Object -First 1 -ExpandProperty FullName
    if (-not $found) {
        throw 'psql.exe nao encontrado. Instale o PostgreSQL ou adicione o psql ao PATH.'
    }
    $psql = $found
}

if (-not $env:DB_PASSWORD) {
    $env:DB_PASSWORD = 'admin'
}
$env:PGPASSWORD = $env:DB_PASSWORD

$dbUser = if ($env:DB_USER) { $env:DB_USER } else { 'postgres' }
$dbName = if ($env:DB_NAME) { $env:DB_NAME } else { 'avcar' }

$exists = & $psql -h localhost -p 5432 -U $dbUser -d postgres -tAc "SELECT 1 FROM pg_database WHERE datname='$dbName'"
if ($exists -ne '1') {
    & $psql -h localhost -p 5432 -U $dbUser -d postgres -c "CREATE DATABASE $dbName"
}

$tableCount = & $psql -h localhost -p 5432 -U $dbUser -d $dbName -tAc "SELECT count(*) FROM information_schema.tables WHERE table_schema='public'"
if ([int]$tableCount -eq 0) {
    Get-Content "$PSScriptRoot\database\01_ddl.sql" |
        Where-Object { $_ -notmatch '^\\(restrict|unrestrict)\b' -and $_ -notmatch '^SET transaction_timeout' } |
        & $psql -h localhost -p 5432 -U $dbUser -d $dbName -v ON_ERROR_STOP=1
}

Write-Host "Banco '$dbName' pronto."
