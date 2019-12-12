# Modularly Create a Form App Using Android Studio & Google Sheet

Set up the structure to modularly create your forms inside an Android app. Basically any expansion or modification to the forms will only take place in the layout (.xml) which are just changing tag & text of a new component, and no further main program adjustment necessary. You can copy-paste the layout components, adjust some things there, and it’ll be functional — modularity achieved.

Complete explanation can be found in the [LearningDollars blog](https://blog.learningdollars.com/?p=2443).

## Requirements

- Android Studio (used here is 4.0 Canary 2).
- Phone or AVD (Android Virtual Device) for testing.
- Google account.

## Setup

Modify as necessary following the procedures listed in the blog mentioned above. Remember to change the URL inside ScrollingActivity.java to the link to your own Google Scrip web-app. Lastly, just build & deploy the app into a phone or AVD.

The Google Script example is placed inside `extras/` directory alongside with a sample app package.