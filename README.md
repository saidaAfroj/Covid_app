# Covid Tracker App 


## Overview

### Description

This is an open-source Android COVID-19 tracking app which is built using core UI components from the Robinhood stock trading app. This app will display the key metrics around the growth of coronavirus, 
including the number of positive cases, negative cases, and deaths per day. This app will also have a feature to display the 
information about covid, how to take care during covid time , how to take care of a child during covid time etc.


This app uses data from the COVID tracking project as the data source. For the time being this app will display data for the US, including all the states and territories.

## App Evaluation
### Category: 
News/Informtation outlets mobile app .

### Mobile: 
Currently we will develop the mobile version of this app . In future we will try to make it available from computer.

### Story: 
User can check the covid data , current condition of US with the help of this app. The whole covid situation will be displayed with data analysis pattern so user can compare the before after situation /affect of covid. 
User can also get information about covid such as how to take care of themself during this time .

### Market: 
People of all age group can safely use this. This app will keep the user update about covid situation .

### Habit: 
As considering the current situation in the world, people from all age group every other day keep checking the covid update/data. This app will give an overall picture of the covid situation . Also by checking the app
user can get idea about "At Home Treatment " .

### Scope:

Right now , the whole world knows about this . People randomly keep checking about covid. 

## User Stories (Required and Optional)
### Required Must-have Stories
User can see the statistics of daily COVID cases.
User can see the COVID cases in United States
User can track the COVID cases with the help of graph(line plot)
User can toggle between positive, negative and death cases.

### Optional Nice-to-have Stories
Having an option to access COVID related news.
Having an option to recommend vitamins.
Having an option to suggest nearby COVID related test centers, centers providing booster shots and hospitals.

## Screen Archetypes
The moment user’s open’s the application he can directly access the COVID cases data.
User’s can toggle through different metrics(i.e. positive, negative and death cases).
User’s can also toggle through different time periods (i.e. week, month cases)
By clicking the line plot that will be displayed on the screen users can see the actual number of cases.
 
## Navigation
### Tab Navigation (Tab to Screen)
 
Date selection
Positive, Negative and Death cases selection
 
## Optional:
COVID News
Suggestions related to nearby COVID centers, Hospitals. 

## Flow Navigation (Screen to Screen)
* State Selection -> shows COVID cases in a selected state( Currently just United States)
* Different cases selection -> shows positive, negative, and death cases
* Date selection ->  by clicking on the line plot, exact no of COVID cases on that particular day will be displayed.
* Time period -> shows line plot chart of the COVID cases throughout the week, month etc.

## Wireframes


<img src='https://github.com/saidaAfroj/Covid_app/blob/main/projectimages/handdrawn_wireframe.jpeg' width='' />

## [BONUS] Digital Wireframes & Mockups

<img src='https://github.com/saidaAfroj/Covid_app/blob/main/projectimages/mockup1.jpeg' width='' />  <img src='https://github.com/saidaAfroj/Covid_app/blob/main/projectimages/mockup2.jpeg' width='' />

<img src='https://github.com/saidaAfroj/Covid_app/blob/main/projectimages/mockup3.jpeg' width='' /> <img src='https://github.com/saidaAfroj/Covid_app/blob/main/projectimages/mockup4.jpeg' width='' />


## Schema

## Models

 Post

|Property|Type|Description|
|-------|------|----------|
|DataPosted|DateTime|The date that the COVID-19 announcement was published|
|Name|Text|The short title of State or City name  in plain text|
|Covid|Number|Number of covid cases|
|Graph|LineGraph|Shows the covid cases in a graph|
|UpdatedAt|DateTime|Date when application was last updated|

## Networking

## List of network requests by screen

1 .National data per day :
        /api/v1/us/daily.json

2.State data per day 
        /api/v1/states/daily.json
        
We will use RETROFIT which is type-safe HTTP client for
android and java. 

``` val client = OkHttpClient()

val request = Request.Builder()
	.url("https://coronavirus-smartable.p.rapidapi.com/news/v1/US/")
	.get()
	.addHeader("x-rapidapi-host", "coronavirus-smartable.p.rapidapi.com")
	.addHeader("x-rapidapi-key", "538f48a7eemsh1b236df53126d80p12c0ffjsn56127e111c82")
	.build()

val response = client.newCall(request).execute()
```



