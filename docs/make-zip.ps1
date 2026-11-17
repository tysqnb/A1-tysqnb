# Rebuilds A1-25259382.zip from this folder.
# Run it after adding docs/wakatime/A1-25259382-WakaTime.png, so the zip and
# the uploaded copy are the same. Usage:  pwsh -File docs/make-zip.ps1

$root = Split-Path -Parent $PSScriptRoot          # the project folder
$name = "A1-25259382"
$zip  = Join-Path (Split-Path -Parent $root) "$name.zip"
$temp = Join-Path $env:TEMP "a1zip"
$stage = Join-Path $temp $name

if (Test-Path $temp) { Remove-Item -Recurse -Force $temp }
New-Item -ItemType Directory -Path $stage -Force | Out-Null

# Everything except build output, VCS metadata and IDE state.
robocopy $root $stage /E /XD bin out .git .settings __pycache__ /NFL /NDL /NJH /NJS /NP | Out-Null

if (Test-Path $zip) { Remove-Item $zip -Force }
Compress-Archive -Path $stage -DestinationPath $zip -CompressionLevel Optimal

"built $zip ($((Get-Item $zip).Length) bytes)"
