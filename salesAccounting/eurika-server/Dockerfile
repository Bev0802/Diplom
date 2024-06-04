# Используйте базовый образ с Java
FROM openjdk:17

# Копируйте собранный jar-файл в образ
COPY target/eurika-server-0.0.1-SNAPSHOT.jar app.jar

# Запустите приложение
ENTRYPOINT ["java","-jar","/app.jar"]