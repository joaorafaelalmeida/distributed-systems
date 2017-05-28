javac ClientCoach/*.java ClientContestant/*.java ClientReferee/*.java ComInf/*.java Interfaces/*.java ServerContestantsBench/*.java ServerPlayground/*.java ServerRefereeSite/*.java ServerRepository/*.java 
#Copy client coach
cp ClientCoach/*.class dir_ClientCoach/ClientCoach/
cp ClientCoach/*.class dir_ClientReferee/ClientCoach/
cp ClientCoach/*.class dir_ClientContestant/ClientCoach/
cp ClientCoach/*.class dir_ServerContestantsBench/ClientCoach/
cp ClientCoach/*.class dir_ServerRefereeSite/ClientCoach/
cp ClientCoach/*.class dir_ServerPlayground/ClientCoach/
cp ClientCoach/*.class dir_ServerRepository/ClientCoach/
#Copy client referee
cp ClientReferee/*.class dir_ClientReferee/ClientReferee/
cp ClientReferee/*.class dir_ClientCoach/ClientReferee/
cp ClientReferee/*.class dir_ClientContestant/ClientReferee/
cp ClientReferee/*.class dir_ServerContestantsBench/ClientReferee/
cp ClientReferee/*.class dir_ServerRefereeSite/ClientReferee/
cp ClientReferee/*.class dir_ServerPlayground/ClientReferee/
cp ClientReferee/*.class dir_ServerRepository/ClientReferee/
#Copy client contestant
cp ClientContestant/*.class dir_ClientContestant/ClientContestant/
cp ClientContestant/*.class dir_ClientReferee/ClientContestant/
cp ClientContestant/*.class dir_ClientCoach/ClientContestant/
cp ClientContestant/*.class dir_ServerContestantsBench/ClientContestant/
cp ClientContestant/*.class dir_ServerRefereeSite/ClientContestant/
cp ClientContestant/*.class dir_ServerPlayground/ClientContestant/
cp ClientContestant/*.class dir_ServerRepository/ClientContestant/
#Copy ComInf
cp ComInf/*.class dir_ClientCoach/ComInf/
cp ComInf/*.class dir_ClientContestant/ComInf/
cp ComInf/*.class dir_ClientReferee/ComInf/
cp ComInf/*.class dir_ServerContestantsBench/ComInf/
cp ComInf/*.class dir_ServerRefereeSite/ComInf/
cp ComInf/*.class dir_ServerPlayground/ComInf/
cp ComInf/*.class dir_ServerRepository/ComInf/
#Copy Interfaces
cp Interfaces/*.class dir_ClientCoach/Interfaces/
cp Interfaces/*.class dir_ClientContestant/Interfaces/
cp Interfaces/*.class dir_ClientReferee/Interfaces/
cp Interfaces/*.class dir_ServerContestantsBench/Interfaces/
cp Interfaces/*.class dir_ServerRefereeSite/Interfaces/
cp Interfaces/*.class dir_ServerPlayground/Interfaces/
cp Interfaces/*.class dir_ServerRepository/Interfaces/
#Copy Interfaces
cp ServerContestantsBench/*.class dir_ServerContestantsBench/ServerContestantsBench/
cp ServerRefereeSite/*.class dir_ServerRefereeSite/ServerRefereeSite/
cp ServerPlayground/*.class dir_ServerPlayground/ServerPlayground/
cp ServerRepository/*.class dir_ServerRepository/ServerRepository/
#Register and Run
xterm  -T "Server Register Repository" -hold -e "./serverRepositoryDeployAndRegister.sh" &
xterm  -T "Server Register Playground" -hold -e "./serverPlaygroundDeployAndRegister.sh" &
xterm  -T "Server Register Referee Site" -hold -e "./serverRefereeSiteDeployAndRegister.sh" &
xterm  -T "Server Register Contestants Bench" -hold -e "./serverContestantsBenchDeployAndRegister.sh" &
sleep 15
xterm  -T "Server Run Repository" -hold -e "./serverRepositoryRun.sh" &
sleep 5
xterm  -T "Server Run Playground" -hold -e "./serverPlaygroundRun.sh" &
xterm  -T "Server Run Referee Site" -hold -e "./serverRefereeSiteRun.sh" &
xterm  -T "Server Run Contestants Bench" -hold -e "./serverContestantsBenchRun.sh" &
#Clientes
xterm  -T "Client Coachs" -hold -e "./clientCoachDeployAndRun.sh" &
xterm  -T "Client Contestants" -hold -e "./clientContestantDeployAndRun.sh" &
xterm  -T "Client Referee" -hold -e "./clientRefereeDeployAndRun.sh" &