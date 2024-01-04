# EasyShop
The Third and Final Capstone for Pluralsight LTCA

## Details
This project contains the skeleton of a web app, originally supplied by my instructor Raymond, and then filled where requested by my own code.

It uses Spring Boot to connect to a SQL database, which the build script for is also included, and API requests to interact with said database. 

## Contributions
While the bulk of the code was provided by Raymond, the parts listed below were created by me and me alone.
- Phase 1 had me completely implement the CategoriesController and CategoriesDao. The functions were provided beforehand.
- In Phase 3 I built the logic for the shopping cart effectively from scratch. 
  - All that was provided was a single method inside the ShoppingCartDAO interface, and comments inside the ShoppingCartController class file.
  - The bulk of ShoppingCartDao and MySqlShoppingCartDao were written entirely by me.

## Issues
- Phase 2 of the Capstone had me find and fix several bugs intentionally left in the Skeleton by Raymond and Pluralsight. The first involved fixing the search functionality in ProductsController, and the second involved adjusting the Update method to change an existing entry in the database, instead of adding a new one every time.
- I was not able to fully complete Phase 3 in time All 4 methods (GET, POST, PUT, and DELETE) are present, but all are limited in some way.
  - For example, adding to one's cart is possible, but the counter in the top right does not account for duplicates of the same item.
- Despite being present earlier in development, all the images have ceased rendering, and I do not know why.

## Screenshots


### Postman
Fixed Product Search
![Fixed Search Function](https://images2.imgbox.com/4f/c1/EAvwTa7R_o.png)

All tests pass
![All tests pass](https://images2.imgbox.com/7d/27/NqsQJrSc_o.png)

### Web App
Main page
![Web App main page](https://images2.imgbox.com/e0/cb/iRCQSeSy_o.png)

Shopping cart
![Shopping cart](https://images2.imgbox.com/23/a6/4OWnXkha_o.png)