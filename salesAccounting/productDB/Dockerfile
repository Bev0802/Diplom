# Используйте базовый образ с Java
FROM openjdk:17

# Копируйте собранный jar-файл в образ
COPY target/productDB-0.0.1-SNAPSHOT.jar app.jar

# Копируйте файл инициализации базы данных внутрь контейнера
#COPY init/data.sql /docker-entrypoint-initdb.d/

# Запустите приложение
ENTRYPOINT ["java","-jar","/app.jar"]