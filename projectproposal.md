# Access Code 2.1 Final Project Proposal

---
**Project Name**  
ChipChop

**Team Name**  
ChipChoppers

**Team Members**  
Madelyn, Anthony and Alvin

## The Problem 
Describe the problem that your app will address. This does not need to be a novel idea or product, but your app should not directly recreate an existing product. If you are basing the app on an existing product, there should be some edge to your design or feature that solves a problem with that existing product.   

In this section, please frame the issue with supporting statistics about the need (market size, competitors, use cases). You should fully think through your user - who is your user? Why do they need this app? How are they using it?   

##### Problem: People want home cooked meals but don't have time to make them and nowadays people aren't very involved in their local communities. This app will foster a bigger sense of community by allowing people to share and interact with their neighbors. 

#### Competitors:
##### <u>Websites</u>: 
Josephine.com<br>
Homemadegoods.com

##### <u>Apps</u>: 
Foodie Shares(Local to California)


##### Market-
Consumers: Targeting young professionals and everyone who is hungry for homemade food
Providers: People who love to cook

##### Use Cases:
-Professinals or anyone who is too busy to cook during the work day but want an authentic, fresh homecooked meal from someone in their community.
-A stay at home mom who loves to cook and would love to contribute to the household income while staying at
home to take care of their children.


## The Solution 
Please provide a detailed description of the app here. Map out how the app solves the problem described in section I. You should also include:
  *  Baseline features you plan to implement by Demo 1 (what + why).
  *  Bonus features you plan to implement if baseline features are completed in time (what + why).
  *  A wireframe of the app. 
  *  
  
 ##### Baseline: 
 - Individual User accounts (Allow the users to create their own profile and store it within the Firebase cloud database with all relevant information to the user to allow easier future access to using the product with saved searches, etc.)
 - Profiles with user/chef information, location, photos and a live update of dishes currently available at the time (including any allergens)
 - Review system for all sellers (Similar to yelp's star ratings that would allow users to rate the chef in general)
 - Utilizing geolocation to locate local chefs and populate onto map (FOR FRESH FOOD!) also allow entry for zip code when searching as a backup for geolocation fail
 -DELIVERY: chefs can decide whether they want to deliver and/or allow pick-upusers can filter through one, other, no preference

##### Bonus Features
-integrate FB login upon account creation to verify users are real and also traceable to a certain extent (Using the Facebook option provided by the Firebase API that will allow us to verify legitamacy of user/chef, and also provide a social platform for our app to be shared and marketed via media).
- Credit Card Payments (To allow contracted chefs to conveniently receive payments and for users to conveniently pay for food)
- Bluetooth connection when order is close by (Using Bluetooth LE to create a connection within 30 ft when the deliverer is outside the door of the recipient to provide a location-based alert notification)
- Shake phone for random suggestion (Fun feature that would utilize sensors of the phone to sense when it is being shaken to randomly select a location and cuisine for the user when the user feels too indecisive to search for something specific but would like to enjoy a homecooked meal)
- Responsive UI (That has the ability to change according to the time of day, Hunger Level, other parameters; simply to delight the user)
- Create a website (Allow for multiple forms of access to our product on different device types to increase accessibility overall and convenience of use)

## Execution
Please describe how you will build this app. Include: 
  *  A detailed timeline for building the product, broken out by weekly sprints. Implement the training from the [Project Management workshop](https://github.com/accesscode-2-1/unit-3/blob/master/lessons/16_ProjectManagement.md) to plan your sprints and outline which features you want to build out over the next 4 weeks. Think about the user stories you would frame your sprints around. *Projects should be planned to complete the build by Demo 1 on September 1, 2015.*  
  
  *  A breakdown of team member responsibilities. Team members should take ownership over a specific feature or aspect of the app. 
  *  
  ##### Breakdown
  Week 1: Basic Functioning MVP
  Week 2: Finalize functioning MVP And begin bonus features
  Week 3: Finalize bonus features and design and debugging/testing

 ##### Roles and Responsibilities for Week 1:
 - Alvin: Front End Design and Layout 
 - Madelyn: Setting up and entering information to Cloud Database
 - Anthony: Getting information from Database and implementing maps
 
