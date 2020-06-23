FROM tomcat:8.5-jdk8-openjdk
WORKDIR $CATALINA_HOME
ENV POSTGRES_URL dlbroadwadb.cpbqys5iu3x8.us-east-2.rds.amazonaws.com
ENV POSTGRES_PORT 5432
ENV POSTGRES_DATABASE_NAME postgres
ENV POSTGRES_USERNAME postgres
ENV POSTGRES_PASSWORD enter123
ENV POSTGRES_DEFAULT_SCHEMA ebay_schema
ARG WAR_FILE=./target/*.war
COPY $WAR_FILE ./webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]