@echo off
cd /d %~dp0
mvn clean test
pause
