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
import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/9/19.
 */
public class HttpRequestTest {

    @Test
    public void getRequestTest() throws IOException {
//        getRequestByGet("http://127.0.0.1:7070/3/camle_test/camel/camelService?username=lisi&password=1234");

        String data = getRequestByGet("http://192.168.0.90:2087/90/wfs?VERSION=1.1.0&SERVICE=WFS&BODY=<?xml version=\"1.0\"?><GetFeature><Query typeName=\"CS_BGFD_PT\"><Filter><PropertyIsLike><PropertyName>MC</PropertyName><Literal>天津%25</Literal></PropertyIsLike></Filter></Query></GetFeature>");
        System.out.println(data);
    }

    @Test
    public void postRequestTest() throws IOException {
        String data = getRequestByPost("http://192.168.0.90:2087/90/wfs", "VERSION=1.1.0&SERVICE=WFS&BODY=<?xml version=\"1.0\"?><GetFeature><Query typeName=\"CS_BGFD_PT\"><Filter><PropertyIsLike><PropertyName>MC</PropertyName><Literal>天津%25</Literal></PropertyIsLike></Filter></Query></GetFeature>");
        System.out.println(data);
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
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "GBK"));
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
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "GBK"));
        String line;
        while ((line = rd.readLine()) != null) {
            data.append(line);
        }
        rd.close();
        return data.toString();
    }

}
