# ads-searching-system

### Owner
@team: Monkey-Year-Horse-Month

## Description
Inspired by Jiayan Gan, the Ads searching system is an ads platform to rank and display ads for users.

## Motivation
By calculating the relativity between the user's query and the ads provided by merchants, this ads searching system displays the most relevant ads that users are most interested in so that user are more likely to click the ads.
In the meantime, merchants can easily target the most potential customers and put the right ads with the lowest cost.

### How does it work? 
1. Merchants provide a list of ads which contain keywords, bid price, and total budget. Our system store all the information and build the forwarded and inverted indexes.
2. When user types the query on the search box, the system parses the query and calculates the relevant score between the query and the ads. Then the system processes a workflow to select, filter, price, and allocate the relevant ads.
3. Finally the ads is sent and displayed to the user interface.

### Technology stack?
Lucene, Memcached, MongoDB, AngularJS+Bootstrap, Tomcat, JUnit, IntelliJ

## How to run ads searching system
__Step1. Install MongoDB, Memcached, and Tomcat__
1. Install mongodb, memcached, and tomcat by homebrew
2. Start mongodb server: `mongod --config /usr/local/etc/mongod.conf`
3. Start memcached server: `memcached`

__Step2. Get source code from Github and run ads-searching-system server__
1. Clone the project from github: ` $ git clone https://github.com/BitTigerInst/ads-searching-system.git `
2. Go to project folder and run `mvn clean install`
3. Setup and run the ads server

