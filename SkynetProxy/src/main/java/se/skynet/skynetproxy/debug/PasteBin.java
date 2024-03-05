package se.skynet.skynetproxy.debug;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Deprecated
public class PasteBin {

    public static String publishJson(String json){
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();


            String postUrl = "http://paste.vibevault.se:8000/upload/";

            HttpPost httpPost = new HttpPost(postUrl);
            httpPost.setHeader("Content-Type", "Content-Type: multipart/form-data");

            // make form
            MultipartEntity reqEntity = new MultipartEntity();

            reqEntity.addPart("expiration", new StringBody("10min"));
            reqEntity.addPart("burn_after", new StringBody("0"));
            reqEntity.addPart("syntax_highlight", new StringBody("json"));
            reqEntity.addPart("privacy", new StringBody("public"));
            reqEntity.addPart("content", new StringBody(json));

            httpPost.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(httpPost);

            String content = EntityUtils.toString(response.getEntity());
            int responseCode = response.getStatusLine().getStatusCode();

            if(responseCode != 302){
                System.out.println("Could not read (" + responseCode + ")\n" + content);
                return "Could not publish to pastebin";
            } else {
                return response.getFirstHeader("Location").getValue();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return "Could not publish to pastebin";
    }

}
