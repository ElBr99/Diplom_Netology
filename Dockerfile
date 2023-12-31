FROM openjdk:11

VOLUME /tmp

EXPOSE 8888

COPY target/DiplomaWork_cloud_storage-0.0.1-SNAPSHOT.jar app-cloud-storage-back.jar

ADD src/main/resources/application.yml src/main/resources/application.yml

ENTRYPOINT ["java", "-jar", "/app-cloud-storage-back.jar"]