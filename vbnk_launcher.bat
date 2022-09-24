@ECHO OFF
TITLE vBNK_SYSTEM
echo.
echo.
echo	 	[1;36m vBNK.sys [1;37m
echo		[1m Bank system web app.
echo		 by Didac Llorens (ironhack)
echo 	 --------------
echo 	 --------------
echo.
echo.
REM
echo 		Loading discovery service...
start /min "vBNK_DISCOVERYservice" cmd /k java -jar "%~dp0vBNK_discovery-service\target\vBNK_discovery-service-0.0.1-SNAPSHOT.jar"
timeout 10> nul
echo 			please, be patient...
timeout 10> nul
echo							[1;32m.OK 	[1;37m
echo.
echo 		Launching keycloak-19 server...
start /min "vBNK_Keycloak-19" cmd /k "%~dp0keycloak-19.0.1\bin\kc.bat start-dev"
timeout 10 > nul
echo			[1;32m				.OK[1;37m
echo.
echo 		Loading authentication service...
start /min "vBNK_AUTHENTICATIONservice" cmd /k  java -jar "%~dp0vBNK_security-service\target\vBNK-authentication-service-0.0.1-SNAPSHOT.jar"
timeout 5 > nul
echo						[1;32m	.OK[1;37m
echo.
echo 		Loading anti fraud service...
start /min "vBNK_ANTIFRAUDservice" cmd /k  java -jar "%~dp0vBNK_anti-fraud-service\target\vBNK_anti-fraud-service-0.0.1-SNAPSHOT.jar"
timeout 5 > nul
echo						[1;32m	.OK[1;37m
echo.
echo 		Loading gateway service...
timeout 10 > nul
start  /min "vBNK_GATEWAYservice" cmd /k  java -jar ""%~dp0vBNK_gateway-service\target\vbnk-gateway-0.0.1-SNAPSHOT.jar"
echo 					[1;32m	.OK[1;37m
echo.
timeout 1 > nul
echo.
echo.
:PROMPT
endlocal
echo.
echo.
echo.
echo 		Automated services are runing.
timeout 1 > nul
echo			Press any key to close all services.
echo.
echo.
echo.
timeout 1 > nul
pause
echo.
echo.
setlocal
SET /P AREYOUSURE="		     [1;37m Are you sure to stop all vBNK running services? [37m(Y/[N])?"

IF /I "%AREYOUSURE%" NEQ "Y" GOTO PROMPT 
echo.
ELSE GOTO PROMPT

:END
endlocal
start cmd /k "%~dp0vbnk_stopper.bat"
exit
