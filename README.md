# Slutprojekt - <i>Fulköpings bibliotek (webb)</i>
Web-Fulkoping är en webbapplikation utvecklad i Java som använder MVC-mönstret för att hantera en biblioteksdatabas. Applikationen möjliggör för användare att söka efter, låna och återlämna böcker samt annat media. Applikationen har även en administrationspanel för användare av sorten Admin. Den är byggd med Tomcat 10.1.34 och JSP-sidor och hanterar användarroller inklusive administratörer.

## Funktioner
* <b>Sökfunktion:</b> Användare kan söka efter böcker, tidskrifter och annat media alternativt klicka på en länk för att se en viss mediakategori.
* <b>Listvy:</b> Resultaten visas i en lista med bild, titel och författare
* <b>Detaljvy:</b> Vid klick på en sökträff visas detaljerad information om median med bild, inklusive tillgänglighet och eventuell förväntad återlämningsdag. Inloggade användare har även möjlighet att låna median om den finns tillgänglig. 
#### Användarfunktioner:
* Registrering och inloggning med användarnamn och lösenord
* Inloggade användare kan låna och återlämna böcker via webbapplikationen, se sin lånehistorik med tidigare lån och se en lista över aktiva/pågående lån
#### Adminfunktioner:
* Admins kan skapa nya användare, samt söka efter och redigera befintliga användare
* Admins kan även lägga till ny media via ett formulär
#### Säkerhet:
* Databasanrop är skyddade mot SQL-injektion
* Lösenord lagras säkert med hashing

## Byggt med
* Java (JDK 21)
* Jakarta EE (Servlet API 6.1.0, JSP/JSTL 3.0.2)
* Hibernate (6.6.6.Final) för databasåtkomst
* PostgreSQL (42.7.5) för databasen
* Tomcat 10.1.34 som webbserver
* JUnit 5.11.0-M2 för tester
* Docker för containerisering

## Installation och körning
1. Klona projektet:<br>
   <i>git clone https://github.com/MH-GRIT/fulk-pings-bibliotek-webb-fridastephanie.git</i>
2. Skapa och konfigurera databasen
   * Starta Docker Desktop
   * Navigera till projektmappen och starta databasen med Docker Compose:<br>
      <i>docker-compose up -d</i>
   * Verifiera att databasen körs:<br>
      <i>docker ps</i>
   * Importera databasdumpen till PostgreSQL:<br>
      <i>docker cp fulkoping_dump.sql postgres-db:/docker-entrypoint-initdb.d/<br>
      docker exec -i postgres-db psql -U dbuser -d fulkoping -f /docker-entrypoint-initdb.d/fulkoping_dump.sql</i>   
#### Alternativ 1: Kör via Docker
1. Starta applikationen från Docker Hub:<br>
   <i>docker pull fridahassel/fulkoping:v13<br>
   docker run -p 8080:8080 --name fulkoping --network app-network fridahassel/fulkoping:v13</i>
2. Öppna webbläsaren och besök: http://localhost:8080/
#### Alternativ 2: Kör via IntelliJ IDEA
1. Öppna projektet i IntelliJ IDEA
2. Se till att Tomcat 10.1.34 är konfigurerad som en server i IntelliJ
3. Kör applikationen genom att starta Tomcat-servern i IntelliJ
4. Öppna webbläsaren och besök: http://localhost:8080/

## Docker Hub Repository
https://hub.docker.com/repository/docker/fridahassel/fulkoping/tags/v13/sha256-403587bb01046b9735a5203c1510a3aa9e3f1bdbb0bc8e1906a510d3f7b13cd8
