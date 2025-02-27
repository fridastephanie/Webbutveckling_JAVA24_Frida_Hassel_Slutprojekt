# Välj rätt tomcat version och jdk version för projektet
FROM tomcat:10.1-jdk21

# Ta bort default applikationen för tomcat
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Kopierar vår applikation in i image
COPY target/*.war /usr/local/tomcat/webapps/ROOT.war

# Öppnar porten som används i appen av tomcat
EXPOSE 8080

# Startar tomcat
CMD ["catalina.sh", "run"]