# Quick check that a share link opens without signing in.
#   powershell -File check_link.ps1 "https://1drv.ms/v/s!..."
#
# ASCII only on purpose: Windows PowerShell 5.1 reads this file as ANSI and
# chokes on non-ASCII characters in a string literal.
param([Parameter(Mandatory = $true)][string]$Url)

$ua = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/141.0 Safari/537.36"
$out = curl.exe -sS -L -A $ua -o "$env:TEMP\link_check.html" -w "%{http_code}|%{url_effective}" $Url
$status, $final = $out -split '\|', 2
$body = Get-Content "$env:TEMP\link_check.html" -Raw -ErrorAction SilentlyContinue

Write-Output "status : $status"
Write-Output "landed : $final"

if ($final -match 'login\.microsoftonline\.com|login\.live\.com|account\.microsoft\.com') {
    Write-Output "result : NOT SHARED - this link bounces to a sign-in page"
    exit 1
}
if ($status -ne '200') {
    Write-Output "result : NOT OK - http $status"
    exit 1
}
if ($body -match 'Access denied|You need permission|does not exist') {
    Write-Output "result : NOT SHARED - the page says permission is needed"
    exit 1
}
Write-Output "result : looks open. Play it in an InPrivate window to be sure."
