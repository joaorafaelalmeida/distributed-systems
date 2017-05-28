echo "Contestants Bench"
echo "Compressing data to be sent to the server side node."
rm -rf serverSideContestantsBench.zip
zip -rq serverSideContestantsBench.zip set_rmiregistryContestantsBench.sh dir_ServerContestantsBench
echo "Transfering data to the server side node."
sshpass -f password ssh sd0308@l040101-ws01.ua.pt 'mkdir -p sd0308/GameOfRopeRMI'
sshpass -f password ssh sd0308@l040101-ws01.ua.pt 'rm -rf sd0308/GameOfRopeRMI/*'
sshpass -f password scp serverSideContestantsBench.zip sd0308@l040101-ws01.ua.pt:sd0308/GameOfRopeRMI
echo "Decompressing data sent to the server side node."
sshpass -f password ssh sd0308@l040101-ws01.ua.pt 'cd sd0308/GameOfRopeRMI ; unzip -q serverSideContestantsBench.zip'
sleep 1
echo "Register RMI."
sshpass -f password ssh sd0308@l040101-ws01.ua.pt 'cd sd0308/GameOfRopeRMI ; sh set_rmiregistryContestantsBench.sh 22370'
