# API Design Document
## Inventory Management System

### API Methods

1. **POST** api/v1/category/add
   - request body : 
   ```json
   {
    "category-name" : "grocery" 
   }
   ```
   - response status : 201
   - response : 
   ```json
   {
   "message" : "Successfully Created category"
   }
   ```

2. **POST** api/v1/product/add
    - request body :
   ```json
    {
    "name" : "pen",
    "price" : 45.87,
    "quantity" : 3,
    "categoryId" : 1
    }
   ```
   - response status : 201
   - response :
   ```json
   {
    "message" : "Successfully created product"
   }
   ```
 
3. **GET**  api/v1/products?category-id = {id}
    - query-param-required : false
    - response-status : 200
    - response:product details as JSON
      ```json
      {
      "message" : "Successfully Fetched",
      "products" : [
      {
       "productId": 1,
       "productName": "pen",
       "price": 23.0,
       "quantity": 1,
       "categoryId":1
        }
      ]
      }
      ```
    
4. **GET** api/v1/category?category-id = {id}
   - query-param-required:false
   - response-status:200
   - response:product detail by category-id as JSON
   ```json
   {
   "message" : "Successfully fetched",
   "category" : {
      "category-id": 1,
      "category-name":"groceries"
    }
   }
   ```
   
5. **PUT** api/v1/update/product?id={product-id}&name={product-name}&price={price}&quantity={quantity} 
   - query-param-required:false(except id)
   - response-status:201
   - response:
    ```json
      {
        "message" : "product updated successfully"
      }
   ```
6. **DELETE** api/v1/remove/category?category-id = {id}
   - query-param-required:true
   - response-status:200
   - response:
     ```json
        {
        "message" :"successfully deleted category id"
        }

     ```
7. **DELETE** api/v1/remove/product?product-id = {id}
   - query-param-required : true
   - response-status:200
   - response:
     ```json
        {
       "message" : "successfully deleted product id"
        }

     ```

8. **GET** api/v1/product?product-id={id}
   - query-param-required : true
   - response-status:200
   - response:
   ```json
      {
       "message" : "successfully fetched",
       "product" : {
       "productId": 1,
       "productName": "pen",
       "price": 23.0,
       "quantity": 1,
       "categoryId":1
        }
      }
   ```
 