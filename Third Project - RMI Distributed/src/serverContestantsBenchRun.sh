echo "Contestants Bench"
echo "Run server."
sshpass -f password ssh sd0308@l040101-ws01.ua.pt 'cd sd0308/GameOfRopeRMI/dir_ServerContestantsBench ; sh serverSide_com.sh'
echo "Server  shutdown."
