@echo off
REM Duplo clique para subir a API (perfil local + H2, sem MySQL nem Docker)
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0run.ps1" %*
pause
