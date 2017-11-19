# Comment Miner

An application for gathering metrics on various social media platforms that will enable you to assess how popular something is.

Support for: 

* [ ] Reddit
* [x] Twitter
* [ ] Facebook
* [ ] Youtube
* [ ] Medium
* [ ] Slack

## What's the idea? 

I like to trade digital currencies and have been successful to date in increasing the value of my investment.
 
However, reportedly, having knowledge of how much a given digital currency is being talked about and whether the commentary is generally positive/negative, could be a reasonable predictor of sentiment towards price increases/decreases longer term.

e.g. an increase in active subscribers to a Reddit thread for r/litecoin could be an indicator of intent to buy. So a sudden surge could be a good buy indicator. Of course intent of the messages users are posting will also contribute as well.

## Before running the application...

You'll want to ensure you have docker installed so that you can see all the metrics via the graphite & graphana interface.

1. cd commentMiner/
2. docker-compose up

Your machine should pull down the latest graphite and grafana instances and start them up.

You can test graphite is up by visiting localhost:8094 - username password combination is `guest:guest`
You can test grafana is up by visiting localhost - username password is `admin:password`

## Usage:

You'll need to get OAuth credentials from apps.twitter.com to run the code.
You'll need to put these into the application.yml file under src/main/java/resources/application.yml where the relevant placeholders are.
That's the set up done...

To run the code:

1. open your command prompt/terminal 
2. cd into the commentMiner dir
3. `./gradlew bootRun` (mac/*nix) | `gradlew bootRun` (Windows)


