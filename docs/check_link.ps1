# Quick check that a share link opens without signing in.
#   powershell -File check_link.ps1 "https://1drv.ms/v/s!..."
param([Parameter(Mandatory = $true)][string]$Url)

$page = curl.exe -sS -L -o "$env:TEMP\link_check.html" -w "%{http_code} %{url_effective}" $Url
$status, $final = $page -split ' ', 2
$body = Get-Content "$env:TEMP\link_check.html" -Raw -ErrorAction SilentlyContinue

Write-Output "status : $status"
Write-Output "landed : $final"

if ($final -match 'login\.(microsoftonline|live)\.com|account\.microsoft\.com') {
    Write-Output "result : NOT SHARED - this is a sign-in page"
    exit 1
}
if ($status -ne '200') {
    Write-Output "result : check the link, status $status"
    exit 1
}
if ($body -match 'Sign in to your account|登录到你的帐户') {
    Write-Output "result : NOT SHARED - the page wants a sign-in"
    exit 1
}
Write-Output "result : looks open. Confirm by playing it in an InPrivate window."
