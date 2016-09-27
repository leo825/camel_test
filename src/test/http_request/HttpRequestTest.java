/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 2016/9/19
 * Time: 15:34
 * To change this template use File | Settings | File Templates.
 */
package http_request;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/9/19.
 */
public class HttpRequestTest {

    @Test
    public void getRequestTest() throws IOException {
        getRequestByGet("http://127.0.0.1:7070/3/camle_test/camel/camelService?username=lisi&password=1234");
    }

    @Test
    public void postRequestTest() throws IOException {
        String result = getRequestByPost("http://localhost:8080/HGIS_TL_FWZX/server/postTest", "username=lisi&password=1234");
    }

    public static String getRequestByPost(String url, String params) throws IOException {
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoOutput(true);
        PrintWriter out = new PrintWriter(conn.getOutputStream());
        out.print(params);
        out.flush();
        out.close();

        StringBuffer data = new StringBuffer();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String line;
        while ((line = rd.readLine()) != null) {
            data.append(line);
        }
        rd.close();
        return data.toString();
    }


    public static String getRequestByGet(String url) throws IOException {
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoOutput(false);
        conn.setDoInput(true);

        StringBuffer data = new StringBuffer();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            data.append(line);
        }
        rd.close();
        return data.toString();
    }

}
