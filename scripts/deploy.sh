#!/bin/bash

# 이미지 이름
image_name="givemesomefoodplz/boong-o-bbang_server:1.0"

# 컨테이너 이름
container_name="boong"

# 이미지 확인
if sudo docker images | grep -q "$image_name"; then
    # 이미지가 존재하면 삭제
    sudo docker rmi "$image_name"
fi

# 컨테이너 확인 및 중지/제거
if sudo docker ps -a -q --filter "name=$container_name" | grep -q .; then
    sudo docker stop "$container_name" && sudo docker rm "$container_name"
fi

# 이미지 다운로드
sudo docker pull "$image_name"

# 컨테이너 실행
docker run -d -p 80:8080 --name "$container_name" "$image_name"

# 미사용 이미지 있는 경우 삭제
if [ "$(docker images -f "dangling=true" -q)" != "" ]; then
    docker rmi -f $(docker images -f "dangling=true" -q)
fi
# Redis 설치
sudo apt-get install -y redis-server
