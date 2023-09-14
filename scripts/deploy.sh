sudo docker ps -a -q --filter "name=boong" | grep -q . && docker stop boong && docker rm boong | true

sudo docker rmi givemesomefoodplz/boong-o-bbang_server:1.0

sudo docker pull givemesomefoodplz/boong-o-bbang_server:1.0

docker run -d -p 80:8080 --name boong givemesomefoodplz/boong-o-bbang_server:1.0

docker rmi -f $(docker images -f "dangling=true" -q) || true
