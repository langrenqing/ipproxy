#!/bin/bash
pid=`ps aux | grep "com.yuanbaopu.proxy.ProxyMain" | grep java | awk '{print $2}' | sort | head -1`

if [ -n "$pid" ]; then
    echo "Stop old ip-proxy server: $pid"
    kill $pid
else
    echo "No old ip-proxy server"
fi

sleep 2

for jar in `ls lib/*.jar`
do
    jars="$jars:""$jar"
done
java $JAVA_OPTS -Dprop.config=./resources/ -cp $jars com.yuanbaopu.ProxyMain 1>/dev/null 2>&1 &