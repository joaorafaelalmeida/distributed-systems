echo "Referee Site"
echo "Compressing data to be sent to the server side node."
rm -rf serverSideRefereeSite.zip
zip -rq serverSideRefereeSite.zip set_rmiregistryRefereeSite.sh dir_ServerRefereeSite
echo "Transfering data to the server side node."
sshpass -f password ssh sd0308@l040101-ws02.ua.pt 'mkdir -p sd0308/GameOfRopeRMI'
sshpass -f password ssh sd0308@l040101-ws02.ua.pt 'rm -rf sd0308/GameOfRopeRMI/*'
sshpass -f password scp serverSideRefereeSite.zip sd0308@l040101-ws02.ua.pt:sd0308/GameOfRopeRMI
echo "Decompressing data sent to the server side node."
sshpass -f password ssh sd0308@l040101-ws02.ua.pt 'cd sd0308/GameOfRopeRMI ; unzip -q serverSideRefereeSite.zip'
sleep 1
echo "Register RMI."
sshpass -f password ssh sd0308@l040101-ws02.ua.pt 'cd sd0308/GameOfRopeRMI ; sh set_rmiregistryRefereeSite.sh 22371'
