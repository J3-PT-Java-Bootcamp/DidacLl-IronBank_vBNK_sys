@ECHO OFF
echo 		Loading discovery service...
start /min "vBNK_DISCOVERYservice" cmd /k java -jar C:\Users\SCVRI\IdeaProjects\vBNKsys\vBNK_discovery-service\target\vBNK_discovery-service-0.0.1-SNAPSHOT.jar
start /min /wait timeout 10
echo 			please, be patient...
start /min /wait timeout 10
echo 		Launching keycloak-19 server...
start /min "vBNK_Keycloak-19" cmd /k C:\Users\SCVRI\keycloak-19.0.1\bin\kc.bat start-dev
start /min /wait timeout 10
echo 		Loading authentication service...
start /min "vBNK_AUTHENTICATIONservice" cmd /k  java -jar C:\Users\SCVRI\IdeaProjects\vBNKsys\vBNK_security-service\target\vBNK-authentication-service-0.0.1-SNAPSHOT.jar
start /min /wait timeout 5
echo 		Loading anti fraud service...
start /min "vBNK_ANTIFRAUDservice" cmd /k  java -jar C:\Users\SCVRI\IdeaProjects\vBNKsys\vBNK_anti-fraud-service\target\vBNK_anti-fraud-service-0.0.1-SNAPSHOT.jar
start /min /wait timeout 5
echo 		Loading gateway service...
start /min /wait timeout 5
start  /min "vBNK_GATEWAYservice" cmd /k  java -jar C:\Users\SCVRI\IdeaProjects\vBNKsys\vBNK_gateway-service\target\vbnk-gateway-0.0.1-SNAPSHOT.jar
exit
