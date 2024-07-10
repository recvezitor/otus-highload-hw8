# otus-highload-hw8

* разбиение на микросервисы
* реактивщина
* без ОРМ
* добавлены трассировочные id-шники
* версионнирование АПИ


### Инструкция

Выделенный сервис под хранение только диалогов из ДЗ6. 

### install

mvn clean install
docker build -f docker/Dockerfile.jvm -t otus-highload-hw8:latest .
docker images

### publish

docker tag otus-highload-hw8:latest recvezitor/otus-highload-hw8:latest
docker login -> recvezitor/password
docker push recvezitor/otus-highload-hw8:latest

### deploy

docker compose -f ./docker/docker-compose.yml