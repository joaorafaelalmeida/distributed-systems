echo "Referee Site"
echo "Run server."
sshpass -f password ssh sd0308@l040101-ws02.ua.pt 'cd sd0308/GameOfRopeRMI/dir_ServerRefereeSite ; sh serverSide_com.sh'
echo "Server  shutdown."
