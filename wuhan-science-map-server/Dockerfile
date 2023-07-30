FROM openjdk:17
MAINTAINER edoclin
RUN mkdir -p /target
#进入到app目录下面，类似cd
WORKDIR /target
#移动当前目录下面的文件到app目录下
COPY ./build/libs /target/
RUN cd /target
RUN chmod -R 777 *
#对外暴露的端口
EXPOSE 18000
#程序启动脚本
CMD ["sh", "-c", "java -jar ./pinhui-trip-1.0.2-alpha.jar --spring.profiles.active=prod --mpw.key=4db68cf200d3852a --system.mobile=19113902594 --system.password=Aa123456 --log.batch.size=50000"]
