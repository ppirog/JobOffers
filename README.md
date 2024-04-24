<div align="center">
  <code><img width="50" src="https://user-images.githubusercontent.com/25181517/117201156-9a724800-adec-11eb-9a9d-3cd0f67da4bc.png" alt="Java" title="Java"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117201470-f6d56780-adec-11eb-8f7c-e70e376cfd07.png" alt="Spring" title="Spring"/></code>
  <code><img width="50" src="https://user-images.githubusercontent.com/25181517/182884177-d48a8579-2cd0-447a-b9a6-ffc7cb02560e.png" alt="mongoDB" title="mongoDB"/></code>
  <code><img width="50" src="https://user-images.githubusercontent.com/25181517/182884894-d3fa6ee0-f2b4-4960-9961-64740f533f2a.png" alt="redis" title="redis"/></code>	
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117207330-263ba280-adf4-11eb-9b97-0ac5b40bc3be.png" alt="Docker" title="Docker"/></code>
  <code><img width="50" src="https://user-images.githubusercontent.com/25181517/117533873-484d4480-afef-11eb-9fad-67c8605e3592.png" alt="JUnit" title="JUnit"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/183892181-ad32b69e-3603-418c-b8e7-99e976c2a784.png" alt="mocikto" title="mocikto"/></code>
  <code><img width="50" src="https://user-images.githubusercontent.com/25181517/184097317-690eea12-3a26-4f7c-8521-729ebbbb3f98.png" alt="Testcontainers" title="Testcontainers"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/192107854-765620d7-f909-4953-a6da-36e1ef69eea6.png" alt="HTTP" title="HTTP"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/192107858-fe19f043-c502-4009-8c47-476fc89718ad.png" alt="REST" title="REST"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/192108890-200809d1-439c-4e23-90d3-b090cf9a4eea.png" alt="IntelliJ" title="IntelliJ"/></code>
	<code><img width="50" src="https://cdn.brighttalk.com/ams/california/images/channel/19357/image_840418.png" alt="Auth0" title="Auth0"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117207242-07d5a700-adf4-11eb-975e-be04e62b984b.png" alt="Maven" title="Maven"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/190229463-87fa862f-ccf0-48da-8023-940d287df610.png" alt="Lombok" title="Lombok"/></code>
	<div align="center">
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/183896132-54262f2e-6d98-41e3-8888-e40ab5a17326.png" alt="AWS" title="AWS"/></code>
		
</div>
</div>
<br>
<div align="center">
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/183890595-779a7e64-3f43-4634-bad2-eceef4e80268.png" alt="Angular" title="Angular"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/183890598-19a0ac2d-e88a-4005-a8df-1ee36782fde1.png" alt="TypeScript" title="TypeScript"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/183898674-75a4a1b1-f960-4ea9-abcb-637170a00a75.png" alt="CSS" title="CSS"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/192158954-f88b5814-d510-4564-b285-dff7d6400dad.png" alt="HTML" title="HTML"/></code>
</div>
<br>

# LIVEDEMO

CLICK LINK BELOW TO SEE ON YOUTUBE HOW APP WORKS:
https://www.youtube.com/watch?v=-vTgTVoMRhs

CLICK LINK BELOW TO USE APP:
http://ec2-3-79-167-135.eu-central-1.compute.amazonaws.com/login

# Frontend

Frontend to this app: https://github.com/ppirog/joboffers-front 

# Info


This apllication fetches job offers from an external service using a rest api and stores it in mongodb database.



Application was deployed on AWS.  (instruction how to use it in LIVEDEMO section)  
To boost time efficiency during making request redis db was added.

Fetching new offers is done by app automaticly using scheduler.

To use application user has to register, login and fetch JWT token

Then user can fetch data using endpoints:
- GET /offers
- GET /offers/{id}

Application enables adding offers manualy using endpoint:
- POST /offers

All functionality are unit and integration tested. 

# Core
- Java  
- Spring  
- MongoDb
- Redis
- Spring Security
- Scheduler
- Docker
- JWT
# Testing:
- JUnit  
- AssertJ
- Mockito
- Testcontainers
- Wiremock
- MockMvc

# Architecture
![img](https://github.com/ppirog/JobOffers/assets/126290295/c0a5aa63-4b33-467d-adbe-7b013c43340e)

# HOW TO USE IT FROM POSTMAN
Application was deployed on AWS. 
If you want to use deployed app use postman as in an example below BUT INSTEAD OF
http://localhost:8080 add http://ec2-3-79-167-135.eu-central-1.compute.amazonaws.com:8000

http://ec2-3-79-167-135.eu-central-1.compute.amazonaws.com:8000/register  
http://ec2-3-79-167-135.eu-central-1.compute.amazonaws.com:8000/token  
http://ec2-3-79-167-135.eu-central-1.compute.amazonaws.com:8000/offers  

# Postman AWS
![image](https://github.com/ppirog/JobOffers/assets/126290295/fc30f795-17e5-4879-935b-e15d8fe38e83)


# Postman Localhost
![image](https://github.com/ppirog/JobOffers/assets/126290295/423242ea-d62f-411f-9011-02f569c16898)
![image](https://github.com/ppirog/JobOffers/assets/126290295/adc880e9-57a0-4afb-9a3e-32653bae169f)
![image](https://github.com/ppirog/JobOffers/assets/126290295/438f160d-add5-43e5-9ff9-796cb5f3ceee)
![image](https://github.com/ppirog/JobOffers/assets/126290295/08550690-b446-49e8-b64b-03b9f5ee11e4)
![image](https://github.com/ppirog/JobOffers/assets/126290295/e5448ad9-a7e8-4682-aaf9-4c2edc499aa0)

##### To start app go to go to file docker-compose.yml first and launch it







