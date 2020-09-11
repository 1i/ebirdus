## eBirdus
Retrieve bird sightings from ebird.org APIs for the island of Ireland

- Allow users to say a county _Kerry_ and return only the sightings in Kerry.  
- Allow users to ask for rare bird sightings.  
- Allow users to say a day _Monday_ sightings and return the sightings for that specific day.  
- Allow users to say a day _Monday_ and a location _Kerry_ sightings and return only the sightings in Kerry that specfic day.  

 
## How to Build and Deploy
From parent directory. Build the jar via maven.  
`mvn clean install`  
Update existing the Lambda with the new jar.  
`aws lambda update-function-code --function-name ebirdus --zip fileb://./target/ebirdus-1.0-SNAPSHOT.jar`  
Invoke the Lambda remotely.  
`aws lambda invoke --function-name ebirdus --log-type Tail outfile`  
The LogResult is returned in Base64 so will need to be decoded.


## Ebird.org API

Excellent bird learning resource provided by Cornell University USA  

Notable birds
```
curl "https://api.ebird.org/v2/data/obs/IE/recent/notable?back=1" -H "X-eBirdApiToken: d4adr470eh9u"  
```
Docs [https://documenter.getpostman.com/view/664302/S1ENwy59?version=latest#intro]  
Postman collection in test resources.
