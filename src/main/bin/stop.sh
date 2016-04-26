#!/bin/bash
pid=`ps aux | grep "com.yuanbaopu.proxy.ProxyMain" | grep java | awk '{print $2}' | sort | head -1`
kill $pid
