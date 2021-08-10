## Contract Testing:
The contract testing can be thought of as testing (and therefore guaranteeing) the communication layer between services. Contract testing tests that any pair of dependent services can properly send and decode messages between each other, but doesn’t test the services’ internal logic. As such, contract testing exists somewhere on the boundary between integration testing and end-to-end testing.

## Example participants:
We will use the following participants - or pacticipants as they are called in Pact -
   ``` The provider  ``` - it is a menu service which has the endpoint GET /menu/{menuId} which returns a menu’s data in the following form:
  
  ```
{
    "id": "219bda19-90ca-436d-8723-28390ec4ba4a",
    "restaurantId": "74b858a4-d00f-11ea-87d0-0242ac130003",
    "name": "Dessert Menu",
    "description": "Menu description",
    "categories": [],
    "enabled": true
}
  ``` 

 ``` The consumer  ``` - is a an app that shows the menu’s name. 
 It expects that a call to GET menu/{menuId} returns:
```
 - a 200 success code,
 - content type JSON with UTF-8 encoding and
 - a JSON body that contains the field name of type string
```                     
  
  
## Prerequisite:
Please provide the ```Pact_Broker_URL``` and ```Pact_Broker_Token``` to the provider's pom
      
``` 
    <pactBrokerUrl>Pact_Broker_URL</pactBrokerUrl>
    <pactBrokerToken>Pact_Broker_Token</pactBrokerToken>
```    
####Note: The pact broker URL and Token values can be provided directly in the command line as system properties:  
``` -Dpact.broker.url=url_value -Dpact.broker.token=token_value```


## Steps:   
1. Consumer: Creating the contract
    - Run the 'GenericMenuConsumer.java' class from the following path:
    ```api-tests/src/test/java/com/xxAMIDOxx/xxSTACKSxx/api/pact/GenericMenuConsumer.java ```
    
    Note: this step can be skipped in case the pact file already exists in .pact/pacts directory.
2. - Execute  ```mvn pact:publish``` from 'api-tests' directory to publish the consumer pact to broker.
3. - Execute ```mvn pact:verify``` from the provider (java directory).
4. - Execute  ```mvn pact:publish``` from 'java' directory to publish this pact to broker.
5. - Execute  ```mvn pact:can-i-deploy -Dpacticipant=YOUR_CONSUMER_NAME -DpacticipantVersion=CONSUMER_VERSION -Dto=ENV_TO_DEPLOY``` from 'java' directory including this variables:
 to check if the versions of consumer and provider are compatible.    
 
 ####Note: At the moment the default values for name, protocol, host and port are:
 
         pact.api.name = JavaMenuAPI
         pact.api.protocol = http
         pact.api.host = localhost
         pact.api.port = 9000
To execute the command with other values, please provide them as system properties as follows:
  ```
  mvn pact:verify -Dpact.api.name=JavaMenuAPI -Dpact.api.protocol=http -Dpact.api.host=localhost -Dpact.api.port=9000
  ```
