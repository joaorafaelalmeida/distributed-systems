echo "Repository"
echo "Compressing data to be sent to the server side node."
rm -rf serverSideRepository.zip
zip -rq serverSideRepository.zip set_rmiregistryRepository.sh dir_ServerRepository
echo "Transfering data to the server side node."
sshpass -f password ssh sd0308@l040101-ws04.ua.pt 'mkdir -p sd0308/GameOfRopeRMI'
sshpass -f password ssh sd0308@l040101-ws04.ua.pt 'rm -rf sd0308/GameOfRopeRMI/*'
sshpass -f password scp serverSideRepository.zip sd0308@l040101-ws04.ua.pt:sd0308/GameOfRopeRMI
echo "Decompressing data sent to the server side node."
sshpass -f password ssh sd0308@l040101-ws04.ua.pt 'cd sd0308/GameOfRopeRMI ; unzip -q serverSideRepository.zip'
sleep 1
echo "Register RMI."
sshpass -f password ssh sd0308@l040101-ws04.ua.pt 'cd sd0308/GameOfRopeRMI ; sh set_rmiregistryRepository.sh 22373'
