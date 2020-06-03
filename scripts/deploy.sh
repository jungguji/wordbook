#1/bin/bash

REPOSITORY=/home/ec2-user/app/travis

cd $REPOSITORY/

echo "> Build 파일복사"

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 현재 구동중인 어플리케이션 pid 확인"

CURRENT_PID=$(pgrep -fl EnglishWord-0.0.1-SNAPSHOT | grep jar | awk '{print $1}')

echo "현재 구동 중인 어플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
        echo "> 현재 구동중인 어플리케이션이 없으므로 종료하지 않습니다."
else
        echo "> kill -15 $CURRENT_PID"
        kill -15 $CURRENT_PID
        sleep 5
fi

echo "> 새 어플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/ | tail -n 1)
#JAR_NAME=EnglishWord-0.0.1-SNAPSHOT.jar

echo "> Jar name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $REPOSITORY/$JAR_NAME

nohup java -jar \
  -Dspring.config.location=classpath:/application.properties,classpath:/application-prod.properties \
  -Dspring.profiles.active=prod \
  $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &

