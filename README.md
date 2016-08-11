# ads-searching-system

### Owner
@team: [Monkey-Year-Horse-Month](https://github.com/orgs/BitTigerInst/teams/monkey-year-horse-month)

## Description
Inspired by Jiayan Gan, the Ads searching system is an ads platform to rank and display ads for users.

## Motivation
By calculating the relativity between the user's query and the ads provided by merchants, this ads searching system displays the most relevant ads that users are most interested in so that user are more likely to click the ads.
In the meantime, merchants can easily target the most potential customers and put the right ads with the lowest cost.

### How does it work 
1. Merchants provide a list of ads which contain keywords, bid price, and total budget. Our system store all the information and build the forwarded and inverted indexes.
2. When user types the query on the search box, the system parses the query and calculates the relevant score between the query and the ads. Then the system processes a workflow to select, filter, price, and allocate the relevant ads.
3. Finally the ads is sent and displayed to the user interface.

### How to build it
Lucene, Memcached, MongoDB, AngularJS+Bootstrap, Tomcat, JUnit, IntelliJ

## How to run ads searching system
### Run the project locally
__Step1. Install MongoDB and Memcached__

1. Install mongodb and memcached by [homebrew](http://brew.sh)
2. Start mongodb server: `$ mongod --config /usr/local/etc/mongod.conf`
3. Start memcached server: `$ memcached`

__Step2. Get source code from Github and run ads-searching-system server__

1. Clone the project from github: `$ git clone https://github.com/BitTigerInst/ads-searching-system.git`
2. Go to project folder, open the project in IntelliJ by the POM file
3. Run `$ mvn clean install` to compile and install the project
4. Right click the `Main` java file under java folder, click on `Run Main.main()` button

### Deploy on Heroku
1. Create an account on Heroku
2. Login to Heroku, create an app on Heroku. Then you will get a random name as the first part of the url(i.e. https://random-name.herokuapp.com/). You can also specify a name for your project. So in our case the project url is https://ads-searching-system.herokuapp.com/.
3. Add Memcached Cloud, mLab Mongodb as add-ons. These add-ons need account verification. So you need to set up a valid payment information. As for our project mLab MongoDB is free of charge if storage is under 496MB, and Memcached Cloud is free as well if memory size is under 30MB. More to read [here](https://elements.heroku.com/addons/memcachedcloud) and [here](https://elements.heroku.com/addons/mongolab)
4. Link your Github's targeted branch(in this case it's master branch) to Heroku so that latest changes can be shown on Heroku
5. Update relevant settings for remote Memcached and MongoDB in the project, and push the latest changes to targeted branch
6. Type the url(https://ads-searching-system.herokuapp.com/) to open the project on your browser

## Technology Stack
### Front End
##### CSS Framework: Bootstrap
[Bootstrap](http://getbootstrap.com/) is a free and open-source front-end web framework for designing websites and web applications. Unlike many web framework, it concerns itself with front-end development only.

##### Javascript Framework: AngularJS
[AngularJS](https://angularjs.org/) is a complete JavaScript-based open-source client and server-side web application framework. It aims to simplify both the development and the testing of such applications by providing a framework for client-side MVC architectures, along with components commonly used in rich Internet applications.

### Back End
##### Web Server: Jetty
[Jetty](http://www.eclipse.org/jetty/) is a Java HTTP (Web) server and Java Servlet container. It is often used for machine to machine communications, and it supports the latest Java Servlet API (with JSP support) as well as protocols HTTP/2 and WebSocket.

##### Query Parsing: Lucene
[Apache Lucene](https://lucene.apache.org/) is recognized for its utility in the implementation of Internet search engines and local, single-site searching. We utilize Lucene to parse the input query to lists of words in our project.

##### Cache: Memcached
[Memcached](https://memcached.org/) is a general-purpose distributed memory caching system. It is often used to speed up dynamic database-driven websites by caching data and objects in RAM to reduce the number of times an external data source (such as a database or API) must be read. In our project, we cache inverted index in order to speed up searching.

##### Database: MongoDB
[MongoDB](https://www.mongodb.com/) is a free and open-source cross-platform document-oriented database. MongoDB avoids the traditional table-based relational database structure in favor of JSON-like documents with dynamic schemas, making the integration of data in certain types of applications easier and faster. 

##### Cloud Platform: Heroku
[Heroku]() is a cloud Platform-as-a-Service (PaaS) supporting several programming languages. It provides us one free instance to deploy our project without any cost. Plus, it contains lots of add-ons which are easy to use for our project. 

##### Unit Testing: JUnit
[JUnit](junit.org/) is a unit testing framework to write repeatable tests. These unit tests are typically written and run by software developers to ensure that code meets its design and behaves as intended.

##### Build Tool: Maven
[Apache Maven](https://maven.apache.org/) is a software project management and comprehension tool. Based on the concept of a project object model (POM), Maven can manage a project's build, reporting and documentation from a central piece of information.

## Resources
- [Project Overview](https://www.bittiger.io/microproject/KrPpRGNyDEpk4nSdn)
- [Project Video](https://www.bittiger.io/classpage/d8hva54gPra9EnSRE) by Jiayan Gan the project creator
- [Slack](https://slack.com/) for team meeting twice a week
- [Trello](https://trello.com/) for taking notes
- [Heroku](https://dashboard.heroku.com/) for deploying project in the cloud

## License
[MIT](/LICENSE.md)