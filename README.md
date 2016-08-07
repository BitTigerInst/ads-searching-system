# ads-searching-system

### Owner
@team: Monkey-Year-Horse-Month

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
__Step1. Install MongoDB, Memcached, and Tomcat__

1. Install mongodb, memcached, and tomcat by [homebrew](http://brew.sh)
2. Start mongodb server: `mongod --config /usr/local/etc/mongod.conf`
3. Start memcached server: `memcached`

__Step2. Get source code from Github and run ads-searching-system server__

1. Clone the project from github: `git clone https://github.com/BitTigerInst/ads-searching-system.git`
2. Go to project folder, open the project in IntelliJ by the POM file
3. Run `mvn clean install` to compile and install the project
4. Right click the `Main` file under java folder, click on `Run Main.main()` button

### Deploy on Heroku
1. Create an account on Heroku
2. Login to Heroku, create an app on Heroku
3. Add Memcached Cloud, mLab Mongodb as add-ons
4. Link your Github's targeted branch(in this case it's master branch) to Heroku so that latest changes can be shown on Heroku.
5. Type the url(https://ads-searching-system.herokuapp.com/) to open the project on your browser

## Technology Stack
### Front End
__Bootstrap__ [Bootstrap](http://getbootstrap.com/) is a free and open-source front-end web framework for designing websites and web applications. Unlike many web framework, it concerns itself with front-end development only.
__AngularJS__ [AngularJS](https://angularjs.org/) is a complete JavaScript-based open-source client and server-side web application framework. It aims to simplify both the development and the testing of such applications by providing a framework for client-side MVC architectures, along with components commonly used in rich Internet applications.

### Back End
__Lucene__ [Lucene](https://lucene.apache.org/) is recognized for its utility in the implementation of Internet search engines and local, single-site searching. We utilize Lucene to parse the input query to lists of words in our project.
__JUnit__ [JUnit](junit.org/) is a unit testing framework to write repeatable tests. These unit tests are typically written and run by software developers to ensure that code meets its design and behaves as intended.

### Cache
__Memcached__ [Memcached](https://memcached.org/) is a general-purpose distributed memory caching system. It is often used to speed up dynamic database-driven websites by caching data and objects in RAM to reduce the number of times an external data source (such as a database or API) must be read. In our project, we cache inverted index in order to speed up searching.

### Database
__MongoDB__ [MongoDB](https://www.mongodb.com/) is a free and open-source cross-platform document-oriented database. MongoDB avoids the traditional table-based relational database structure in favor of JSON-like documents with dynamic schemas, making the integration of data in certain types of applications easier and faster. 

## Resources
- [Project Overview](https://www.bittiger.io/microproject/KrPpRGNyDEpk4nSdn)
- [Project Video by Jiayan Gan](https://www.bittiger.io/classpage/d8hva54gPra9EnSRE)
- [Slack](https://slack.com/) for team meeting twice a week
- [Trello](https://trello.com/) for taking notes
- [Heroku](https://dashboard.heroku.com/) for deploying project in the cloud


## License
[MIT](/LICENSE.md)