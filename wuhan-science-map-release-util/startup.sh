#! /bin/bash
echo -e "\e[42;33m开始启动\e[0m"

echo -e "\e[42;33m构建server镜像\e[0m"
docker-compose -f docker-compose-build.yaml build
echo -e "\e[44;33mserver镜像构建完成\e[0m"

echo -e "\e[42;33m停止当前运行实例\e[0m"
docker stop $(docker ps -a | grep "service-name" | awk '{print $1}')
docker rm -f $(docker ps -a | grep "service-name" | awk '{print $1}')
echo -e "\e[44;32m当前运行实例已停止\e[0m"

echo -e "\e[42;33m运行docker镜像\e[0m"
docker-compose up -d
echo -e "\e[44;32m镜像启动成功\e[0m"

echo -e "\e[42;33m同步时区\e[0m"
docker cp /etc/localtime $(docker ps -a | grep "service-name" | awk '{print $1}'):/etc/localtime

echo -e "\e[43;34m容器<service-name>时间\e[0m"
docker exec $(docker ps -a | grep "service-name" | awk '{print $1}') date

echo -e "\e[44;32m启动完成\e[0m"
echo -e "\e[42;33m打印server端日志\e[0m"
tail -10000f ./logs/`ls ./logs`

