package com.v2soft.apsf.shared;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by srikanth.m on 10/11/2017.
 */

public class WebServiceCall {

    private static final String TAG = WebServiceCall.class.getSimpleName();

    public static String callGETRequest(String strURL, String strInput) {

        try {
            strURL = strURL + strInput;
            strURL = strURL.replace(" ", "%20");
            Log.d("URL", strURL);

            // Code to make a webservice HTTP request
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.setRequestProperty("congresskey", "wX72VZ9Pf7OizuYQb9NC8ljPqW15EO72VZ9Pf7OizuYQb9N");
            connection.connect();

            InputStream stream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            return convertStandardJSONString(buffer.toString());
        } catch (Exception e) {
            StringWriter stackTrace = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTrace));

            e.printStackTrace();
            return stackTrace.toString();
        }
    }

    public static String callPOSTRequest(String urlString, String request) {

        String outPutValue = "";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(request);
        } catch (Exception e) {}

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(urlString);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        try {

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setContentCharset(params, "UTF-8");

            /*List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username", jsonObject.getString("username")));
            nameValuePairs.add(new BasicNameValuePair("password", jsonObject.getString("password")));*/

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            Iterator<String> itr = jsonObject.keys();
            String strKey = "";
            while (itr.hasNext()) {
                strKey = itr.next();
                nameValuePairs.add(new BasicNameValuePair(strKey, jsonObject.getString(strKey)));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            return convertInputStreamToString(is);

        } catch (Exception e) {
            outPutValue = "";
            e.printStackTrace();
        }

        return outPutValue;
    }

    public static String callRSSThread(String url, String request) {
        HttpURLConnection connection = null;
        String outPutValue = "";

        try {
            URL url_webservice = new URL(url);
            connection = (HttpURLConnection) url_webservice.openConnection(Proxy.NO_PROXY);
            connection.setRequestMethod("POST");

            //Content-Type: application/json
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setRequestProperty("congresskey", "wX72VZ9Pf7OizuYQb9NC8ljPqW15EO72VZ9Pf7OizuYQb9N");
            connection.setConnectTimeout(6 * 60 * 1000);
            connection.setReadTimeout(6 * 60 * 1000);
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(request);
            wr.flush();
            wr.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    outPutValue += line;
                }
            } else {
                outPutValue = "";
            }
            return outPutValue;
        } catch (Exception e) {
            if (connection != null)
                connection.disconnect();

            e.printStackTrace();

            return null;
        }
    }

    public static String GetDataFromServer(String URL) throws IOException {
        InputStream getstream = null;
        String ResultGet = null;

        HttpGet httpget = new HttpGet(URL);
        httpget.setHeader("Accept", "application/json");
        httpget.setHeader("Content-type", "application/json");
        httpget.setHeader("congresskey", "wX72VZ9Pf7OizuYQb9NC8ljPqW15EO72VZ9Pf7OizuYQb9N");
        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpclient = new DefaultHttpClient(httpParameters);
        HttpResponse httpResponse;
        try {
            httpResponse = httpclient.execute(httpget);
            HttpEntity entity = httpResponse.getEntity();
            getstream = entity.getContent();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        }
        if (getstream != null) {
            ResultGet = convertInputStreamToString(getstream);
        } else {
            ResultGet = "";
        }
        return ResultGet;
    }

    private static String convertInputStreamToString(InputStream inputstream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputstream, "iso-8859-1"), 8);
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputstream.close();
        return result;
    }

    private static Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String convertStandardJSONString(String json) {
        json = json.replace("\\", "");
        json = json.replace("\"{", "{");
        json = json.replace("}\",", "},");
        json = json.replace("}\"", "}");
        json = json.replace("\"[", "[");
        json = json.replace("]\"", "]");

        if (json.contains("\"null\""))
            json = json.replaceAll("\"null\"", "\"\"");

        if (json.contains("null"))
            json = json.replaceAll("null", "\"\"");

        return json;
    }
}