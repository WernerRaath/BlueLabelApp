*Blue Label Assignment*

**Solution Description**

As the job listing is for Android development, I have written an Android app.
The requirements were simple, write some data to a TCP/IP server and print the result.
I created a vanilla app with some basic UI components to visualise the change in app state.
The page has two TextViews, one displaying the data to be posted and the other to display the response.
When the request succeeds I show the response in the result TextView, otherwise I display the error.
To make the request I used a Socket connection, attempting both the built-in java Socket module and also the Apache Commons Telnet module.
Both methods threw a Permission denied error, so the connection could not be established and the xml could not be written to the server.

The file of interest is: https://github.com/WernerRaath/BlueLabelApp/blob/master/app/src/main/java/com/example/wraath/myapplication/MainActivity.java

**Response Received**
None


