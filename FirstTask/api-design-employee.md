# API Design Document
## Employee

### API Methods

1. **GET** /api/v1/employees/{employee-id}/?starts-with={char}
    - query-param-required : false
    - response-status : 200
    - response : list of employee details as JSON
    ```json
   {
      "message" : "Successfully Fetched",
      "employees" : [       
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
    - response-status : 200
    - response : streams details as JSON
    ```json
    {
        "message" : "Successfully Fetched",
        "streams" : ["Sales","QA","CA"]
    }
    ```
3. **PUT** /api/v1/employee/update/?employee-id={emp-id}&manager-id={mgr-id}
    - query-param-required : true
    - response-status : 200
    - response : message showing if the update is success or not
    ```json
    {
        "message" : "Zoro's Manager has been changed from Max to Luffy"
    }
    ```

4. **DELETE** /api/v1/employee/{id}/delete
    - path-variable-required : true
    - response-status : 200
    - response : message showing if the deletion is success or not
   ```json
   {
       "message" : "Successfully deleted Zoro from organizations list"
   }
   ```

5. **POST** /api/v1/employee/add
    - request-body :
   ```json
   {
        "name" : "Zoro",
        "stream" : "Sales",
        "designation" : "Manager",
        "accountName" : "Smart ops",
        "managerId" : 0
   }
   ```
    - response-status : 201
    - response : message showing successfully created employee
   ```json
   {
       "message" : "Successfully added Zoro to organizations list"
   }
   ```