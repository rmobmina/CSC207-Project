package utils;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HttpUtilsTest {
    private HttpUtils httpUtils;

    @BeforeEach
    void setUp() {
        httpUtils = new HttpUtils();
    }

    @Test
    void HttpUtils_makeApiCall_validLink() throws IOException, JSONException {
        assertFalse(httpUtils.makeApiCall("https://www.teach.cs.toronto.edu/~csc110y/fall/notes/").isEmpty());
    }

    @Test
    void httpUtils_makeApiCall_invalidLink() throws IOException, JSONException {
        try {
            httpUtils.makeApiCall("Th1sI54nInval1dL1nk.com").isEmpty();
            assertFalse(true);
        }

        catch (MalformedURLException exception) {
            assertTrue(true);
        }

    }

}

