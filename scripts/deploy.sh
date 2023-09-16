#!/bin/bash

# 이미지 이름
image_name="givemesomefoodplz/boong-o-bbang_server:1.0"

# 컨테이너 이름
container_name="boong"

# 컨테이너 확인 및 중지/제거
if docker ps -a -q --filter "name=$container_name" | grep -q .; then
    docker stop "$container_name" && docker rm "$container_name"
fi

# 이미지 다운로드
docker pull "$image_name"

# 컨테이너 실행
docker run -d -p 80:8080 --name "$container_name" "$image_name"

# 미사용 이미지 삭제
docker rmi -f $(docker images -f "dangling=true" -q) || true

# Redis 설치
sudo apt-get install -y redis-server
