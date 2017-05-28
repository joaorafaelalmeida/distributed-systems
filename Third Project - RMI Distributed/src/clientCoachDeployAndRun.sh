echo "Coach"
echo "Compressing data to be sent to the client side node."
rm -rf clientSideCoach.zip
zip -rq clientSideCoach.zip dir_ClientCoach
echo "Transfering data to the client side node."
sshpass -f password ssh sd0308@l040101-ws06.ua.pt 'mkdir -p sd0308/GameOfRopeRMI'
sshpass -f password ssh sd0308@l040101-ws06.ua.pt 'rm -rf sd0308/GameOfRopeRMI/*'
sshpass -f password scp clientSideCoach.zip sd0308@l040101-ws06.ua.pt:sd0308/GameOfRopeRMI
echo "Decompressing data sent to the client side node."
sshpass -f password ssh sd0308@l040101-ws06.ua.pt 'cd sd0308/GameOfRopeRMI ; unzip -q clientSideCoach.zip'
sleep 7
echo "Executing program at the client side node."
sshpass -f password ssh sd0308@l040101-ws06.ua.pt 'cd sd0308/GameOfRopeRMI/dir_ClientCoach ; sh clientSide_com.sh'
