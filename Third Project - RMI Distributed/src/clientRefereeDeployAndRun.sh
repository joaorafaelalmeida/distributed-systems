echo "Referee"
echo "Compressing data to be sent to the client side node."
rm -rf clientSideReferee.zip
zip -rq clientSideReferee.zip dir_ClientReferee
echo "Transfering data to the client side node."
sshpass -f password ssh sd0308@l040101-ws05.ua.pt 'mkdir -p sd0308/GameOfRopeRMI'
sshpass -f password ssh sd0308@l040101-ws05.ua.pt 'rm -rf sd0308/GameOfRopeRMI/*'
sshpass -f password scp clientSideReferee.zip sd0308@l040101-ws05.ua.pt:sd0308/GameOfRopeRMI
echo "Decompressing data sent to the client side node."
sshpass -f password ssh sd0308@l040101-ws05.ua.pt 'cd sd0308/GameOfRopeRMI ; unzip -q clientSideReferee.zip'
sleep 7
echo "Executing program at the client side node."
sshpass -f password ssh sd0308@l040101-ws05.ua.pt 'cd sd0308/GameOfRopeRMI/dir_ClientReferee ; sh clientSide_com.sh'
