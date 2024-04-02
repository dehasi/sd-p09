# sudo apt-get install -y default-jdk
set -xue

sudo apt update
sudo apt install -y openjdk-21-jdk
sudo apt install -y openjdk-21-jre
java -version
