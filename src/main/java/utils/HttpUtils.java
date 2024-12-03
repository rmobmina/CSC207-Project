package utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * A class used for HTTP and API utilities.
 * Used for gathering and scraping API calls for data.
 */
public class HttpUtils {

    /**
     * Given a valid API call, returns a String object storing the data scraped from said API call.
     * @param urlString a String object storing the exact URL to call an API.
     * @return a String storing all data from the API call using urlString.
     * @throws IOException if the user disconnects from the internet.
     */
    public static String makeApiCall(String urlString) throws IOException {
        String result = null;

        final HttpURLConnection conn = callApi(urlString);

        // the response code, if the call is successful, must be 200 = Constants.RESPONSE_TRESHOLD
        if (conn.getResponseCode() == Constants.RESPONSE_TRESHOLD) {

            final StringBuilder resultJson = new StringBuilder();

            // here, we use a Scanner to parse through the retrieved data and attach it into a StringBuilder
            try (Scanner scanner = new Scanner(conn.getInputStream())) {

                while (scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }

            }

            result = resultJson.toString();
        }

        return result;
    }

    private static HttpURLConnection callApi(String urlString) throws IOException {
        final URL url = new URL(urlString);

        // after converting the urlString to URL format, we connect to the URL and request information retrieval.
        //  critically, the returned HttpURLConnection contains a response code which signifies a success/failure.
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        return conn;
    }

}
