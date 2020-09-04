#! /bin/bash
JUE_APP="/root/jue/jueapp.jar"
LOG_FILE="/root/jue/log/app.log"
SERVER_PORT=443
PATTERN="Tomcat started on port(s): "${SERVER_PORT}" (https) with context path"
APP_NAME="jueapp"
PID=`ps aux|grep "${APP_NAME}" |grep -v "grep"|awk '{print $2}'` 
if [[ "$PID" ]];then
	echo "Old pid is "${PID}", kill it"
    	ps aux|grep "${APP_NAME}" |grep -v "grep"|awk '{print $2}'|xargs kill -9 
	nohup java -Ddebug=true -Dserver.port=${SERVER_PORT} -jar ${JUE_APP} >/dev/null 2>&1 &
	if [[ $? -eq 0 ]]; then
        	echo "Checking instance status:"
		tail -fn0 "${LOG_FILE}" | \
		while read line ; do
        		echo "$line" | grep "${PATTERN}" >/dev/null
        		if [[ $? = 0 ]];	then
				printf "\n========Application Started Successful!========\n"
               			exit 1	
			else
				echo -n "."
       			fi
		done
      	else
        	echo "[Failed]"
      	fi
fi
