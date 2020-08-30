## eBirdus
Retrieve bird sightings from ebird.org APIs  
Parse the records and save into a DynamoDB and S3  
Allows for data to be used by other downstream Lambdas
 

## How to Build and Deploy
From parent directory. Build the jar via maven.  
`mvn clean install`  
Update existing the Lambda with the new jar.  
`aws lambda update-function-code --function-name ebirdus --zip fileb://./target/ebirdus-1.0-SNAPSHOT.jar`  
Invoke the Lambda.  
`aws lambda invoke --function-name ebirdus --log-type Tail outfile`  
The LogResult is returned in Base64 so will need to be decoded.


## Ebird.org API

Excellent bird learning resource provided by Cornell University 

Notable birds vs recent birds
```
curl -v --location --request GET 'https://api.ebird.org/v2/data/obs/IE/recent' \
--header '_X-eBirdApiToken: d4adr470eh9u'  
```
Docs [https://documenter.getpostman.com/view/664302/S1ENwy59?version=latest#intro]
