*Blue Label Assignment*

**Solution Description**

As the job listing is for Android development, I have written an Android app.
The requirements were simple, write some data to a TCP/IP server and print the result.
I created a vanilla app with some basic UI components to visualise the change in app state.
The page has two TextViews, one displaying the data to be posted and the other to display the response.
When the request succeeds I show the response in the result TextView, otherwise I display the error.
To make the request I used a Socket connection, attempting both the built-in java Socket module and also the Apache Commons Telnet module.
Both methods threw a Permission denied error, so the connection could not be established and the xml could not be written to the server.

To debug this issue, I did the following:

```
To simulate a connection request from the terminal, I used the telnet command.

> sudo telnet -d 196.37.22.179 9011

Trying 196.37.22.179...
Connected to 196.37.22.179.
Escape character is '^]'.

The connection didn't close, but no promts were provided.
sudo was required as without it I would get this message: setsockopt (SO_DEBUG): Permission denied

Next I tested with a server that I knew would allow access.

> sudo telnet -d 52.88.68.92 1234

In this example I could continue providing input, meaning that I might need to send special parameters to proceed.

To investigate further I tried to see which service is running on that port.

> nmap 196.37.22.179 -Pn -p 9011

Nmap scan report for mail.tritech.co.za (196.37.22.179)
Host is up (0.0042s latency).
PORT     STATE SERVICE
9011/tcp open  unknown

Nmap done: 1 IP address (1 host up) scanned in 0.04 seconds

Thus the port is open, but the service is *unknown*. So no hints there. 
```

The file of interest is: https://github.com/WernerRaath/BlueLabelApp/blob/master/app/src/main/java/com/example/wraath/myapplication/MainActivity.java

**Response Received**
None


