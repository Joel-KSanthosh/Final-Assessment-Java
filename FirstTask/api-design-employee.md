# API Design Document
## Employee

### API Methods

1. **GET** /api/v1/employees
    - query-param : starts-with 
    - query-param-required : true
    - response-status : 200
    - response : employee details as JSON
    ```json
   {
      "message" : "Successfully Fetched",
      "details" : [       
        {
          "id" : 1,
          "name"  : "Stark",
          "designation" : "Manager",
          "accountName" : "Smart ops",
          "manager" : 0
        }
      ]
   }
    ```
2. **GET** /api/v1/streams 
    - query-param-required : false
    - response-status : 200
    - response : streams details as JSON
    ```json
    {
        "message" : "Successfully Fetched",
        "streams" : ["Sales","QA","CA"]
    }
    ```
3. **PUT** /api/v1/employees
    - query-param : manager-id
    - query-param : employee-id
    - query-param-required : true
    - response-status : 200
    - response : 
    ```json
    {
        "message" : "Zoro's Manager has been changed from Max to Luffy"
    }
    ```