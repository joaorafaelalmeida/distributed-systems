echo "Playground"
echo "Compressing data to be sent to the server side node."
rm -rf serverSidePlayground.zip
zip -rq serverSidePlayground.zip set_rmiregistryPlayground.sh dir_ServerPlayground
echo "Transfering data to the server side node."
sshpass -f password ssh sd0308@l040101-ws03.ua.pt 'mkdir -p sd0308/GameOfRopeRMI'
sshpass -f password ssh sd0308@l040101-ws03.ua.pt 'rm -rf sd0308/GameOfRopeRMI/*'
sshpass -f password scp serverSidePlayground.zip sd0308@l040101-ws03.ua.pt:sd0308/GameOfRopeRMI
echo "Decompressing data sent to the server side node."
sshpass -f password ssh sd0308@l040101-ws03.ua.pt 'cd sd0308/GameOfRopeRMI ; unzip -q serverSidePlayground.zip'
sleep 1
echo "Register RMI."
sshpass -f password ssh sd0308@l040101-ws03.ua.pt 'cd sd0308/GameOfRopeRMI ; sh set_rmiregistryPlayground.sh 22372'
