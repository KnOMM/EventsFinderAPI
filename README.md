# EventsFinderBot
**API-based** application that implements Telegram API to communicate with the user. The bot is searching for the events (upcoming, recent, relevant) and provides this information for the user

## NB
Yoh must create a credentials.properties file with Telegram bot credentials (name and key) 
## Useful commands
Creates a fat .jar file that can be run with java -jar command

    mvn clean compile assembly:single

Runs the command in the background and sends logs to a file

    java -jar yourfile.jar > output.log 2>&1 &
