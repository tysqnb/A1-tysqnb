param(
    [Parameter(Mandatory = $true)][string]$Url,
    [switch]$Tenant
)
$ua = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/141.0 Safari/537.36"
$out = curl.exe -sS -L -A $ua -o "$env:TEMP\link_check.html" -w "%{http_code}|%{url_effective}" $Url
$status, $final = $out -split '\|', 2
$body = Get-Content "$env:TEMP\link_check.html" -Raw -ErrorAction SilentlyContinue
Write-Output "status : $status"
Write-Output "landed : $final"
$login = $final -match 'login\.microsoftonline\.com|login\.live\.com|account\.microsoft\.com'
if ($login -and $Tenant) {
    if ($final -match 'scu|southerncross') {
        Write-Output "result : tenant-only link, redirect names the SCU tenant - expected"
    } else {
        Write-Output "result : tenant-only link, but the redirect does not name SCU - check the tenant"
    }
    exit 0
}
if ($login) {
    Write-Output "result : NOT SHARED - bounces to a sign-in page (pass -Tenant if that is intended)"
    exit 1
}
if ($status -ne '200') { Write-Output "result : NOT OK - http $status"; exit 1 }
if ($body -match 'Access denied|You need permission') { Write-Output "result : NOT SHARED - permission needed"; exit 1 }
Write-Output "result : looks open. Play it in an InPrivate window to be sure."
