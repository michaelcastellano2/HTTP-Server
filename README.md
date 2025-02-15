# HTTP Server
Built my own HTTP Web Server which takes in requests and responds to them. Author: Michael Castellano (Oct 2024) 


# How to Run 
The following are instructions on how to run my project on the Linux system.
First, navigate to the project directory. Then, run:
```bash
make
```

Then, in one terminal, start up the server by running:
```bash
sudo java http.HTTPServer
```

In another terminal, establish a working telnet session that operates on port 80:
```bash
telnet localhost 80
```

Then, in this same window, enter a valid HTTP request message conforming to HTTP1.1:
```bash
GET / HTTP/1.1
Host: somehost
```
You can enter as many headers as you like, but remember the Host header is required for HTTP messages.

To see the content in the folders, make sure the server is running and navigate to http://localhost/ with any of the pages in the content folder.
If the requested page is not included in the folder, or if there is a server error, you will see an error page!
