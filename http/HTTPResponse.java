package http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

/**
 * Class representing a single HTTP response message.
 *
 * @version 1.0
 */
public class HTTPResponse {
    /* TODO
     * 1) Create methods and member variables to represent an HTTP response.
     * 2) Set response fields based on the request message received.
     *      a) If the request was invalid, send a 400 Bad Request response, with errors/400.html.
     *      b) If the request path doesn't exist, send a 404 Not Found, with errors/404.html.
     *      c) Otherwise, send a 200 OK, with the full contents of the file.
     *      d) Every response must have these four headers: Server, Date, Content-Length, and Content-Type.
     *          i) Note that Date must follow the required HTTP date format.
     * 3) Craft the response message and send it out the socket with DataOutputStream.
     *      a) Be sure that you use "\r\n" to separate each line.
     */

	private String server = "castelmi";
	private String date;
	private long contentLength;
	private String contentType;

	private HTTPRequest request;
	private Path path;

	private DataOutputStream output;
	private Map<String, String> headerMap;


	/*
	 * Class constructor
	 */
	public HTTPResponse(HTTPRequest request, DataOutputStream output) throws IOException {
		this.request = request;
		this.output = output;

		path = getFilePath();

		headerMap = new HashMap<String, String>();
		populateHeaderMap();
	}
	
	/* Method writes all data to the DataOutputStream instance */
	public void sendResponse() throws IOException {	
		if (!request.isValid()) {
			output.writeBytes("\r\n");
		}
		output.writeBytes("HTTP/1.1 " + getStatusCode());
		output.writeBytes("\r\n");
		writeHeaders();
		output.writeBytes("\r\n");
		writeFileContent();
		output.flush();
	}

	/* Writes the header variables to the output stream */
	private void writeHeaders() throws IOException {
		for (Map.Entry<String, String> header : headerMap.entrySet()) {
			output.writeBytes(header.getKey() + ": " + header.getValue());
			output.writeBytes("\r\n");
		}
	}

	/* Writes the content of a file to a DataOutputStream socket */
        private void writeFileContent() throws IOException {
                byte[] bytes = Files.readAllBytes(path);
                output.write(bytes);
        }

	/* Populates the header map with the header variables and values */
	private void populateHeaderMap() throws IOException {
		headerMap.put("Server", getServer());
		headerMap.put("Content-Length", getContentLength());
		headerMap.put("Date", getDate());
		headerMap.put("Content-Type", getContentType());
	}

	/* Returns the Path object of the given name of the file */
	private Path getFilePath() {
		String pathString = request.getPath();

		if (!request.isValid()) {
			return Paths.get("errors/400.html");
		} else if (!Files.exists(Paths.get("content" + pathString))) {
			return Paths.get("errors/404.html");
		} else if (request.getPath().equals("/")) {
			return Paths.get("content/index.html");
		} else {
			return Paths.get("content" + pathString);
		}
        }

	/* Returns the status code */
	public String getStatusCode() {
		if (!request.isValid()) {
      			return "400";
                } else if (!isValidPath()) {
                        return "404 Not Found";
		}

		return "200 OK";
	}

	/* Checks whether a given path is valid or not */ 
	private boolean isValidPath() {
		if (Files.exists(path)) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Below are methods to get the values 
	 * of the header variables in String format
	 */

	/* Returns the name of a server */
	private String getServer() { 
		return server; 
	}

	/* Gets the date in HTTP format */
	private String getDate() {
                ZonedDateTime date = ZonedDateTime.now(ZoneOffset.UTC);
                return date.format(DateTimeFormatter.RFC_1123_DATE_TIME);
	}

	/* Gets the content of a given file */
        private String getContentType() throws IOException { 
		return Files.probeContentType(path); 
	}

        /* Gets the content length of a given file */
        private String getContentLength() throws IOException { 
		long size = Files.size(path);
		return Long.toString(size);
	}
}
