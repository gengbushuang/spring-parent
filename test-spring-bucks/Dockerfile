FROM java:8
EXPOSE 8080
ARG JAR_FILE
ADD target/${JAR_FILE} /opt/waiter-service.jar
ENTRYPOINT ["java","-jar","/opt/waiter-service.jar"]
