package wy.chris.movieapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class MediaFireAPI {

    public static String BASEURL="https://channelbox.apkmm.net/api/v1/";
    public static String getMedaiFireFile(String mediaurl) throws IOException, JSONException {
        String query=String.format("url=%s", URLEncoder.encode(mediaurl,"UTF-8"));
        URL url=new URL(BASEURL+"mediafire?"+query);
        HttpURLConnection connection=(HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type","application/json;charset=ut-8");
        connection.setRequestProperty("Accept","application/json");
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(true);
        BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
        String responseline="";
        StringBuffer response=new StringBuffer();
        while ((responseline=br.readLine())!=null)
        {

            response.append(responseline);
        }

        JSONObject jsonObject=new JSONObject(response.toString());
        JSONObject data=jsonObject.getJSONObject("data");
        String link=data.getString("file");
        return  link;

    }
}
