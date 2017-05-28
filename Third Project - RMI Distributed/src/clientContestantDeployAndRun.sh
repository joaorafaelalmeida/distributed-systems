echo "Contestant"
echo "Compressing data to be sent to the client side node."
rm -rf clientSideContestant.zip
zip -rq clientSideContestant.zip dir_ClientContestant
echo "Transfering data to the client side node."
sshpass -f password ssh sd0308@l040101-ws07.ua.pt 'mkdir -p sd0308/GameOfRopeRMI'
sshpass -f password ssh sd0308@l040101-ws07.ua.pt 'rm -rf sd0308/GameOfRopeRMI/*'
sshpass -f password scp clientSideContestant.zip sd0308@l040101-ws07.ua.pt:sd0308/GameOfRopeRMI
echo "Decompressing data sent to the client side node."
sshpass -f password ssh sd0308@l040101-ws07.ua.pt 'cd sd0308/GameOfRopeRMI ; unzip -q clientSideContestant.zip'
sleep 7
echo "Executing program at the client side node."
sshpass -f password ssh sd0308@l040101-ws07.ua.pt 'cd sd0308/GameOfRopeRMI/dir_ClientContestant ; sh clientSide_com.sh'
