@echo off

start call E:\cloud\nacos\bin\startup.cmd -m standalone

ping -n 50 127.0.0.1>nul

echo "Waiting Nacos Start"

start call E:\cloud\seata-server-1.4.2\bin\seata-server.bat

ping -n 10 127.0.0.1>nul

start call E:\cloud\rocketmq-all\bin\mqnamesrv.cmd

ping -n 10 127.0.0.1>nul

start call E:\cloud\rocketmq-all\bin\mqbroker.cmd -n localhost:9876 autoCreateTopicEnable=true

ping -n 10 127.0.0.1>nul

start call E:\developer\es\elasticsearch-7.9.1\bin\elasticsearch.bat

ping -n 10 127.0.0.1>nul

start call java -jar E:\cloud\zipkin\zipkin-server-2.23.2-exec.jar