package org.secretwpn.lumixtoolkit.util;

import android.os.AsyncTask;

import org.secretwpn.lumixtoolkit.net.task.CallUrlCallBack;
import org.secretwpn.lumixtoolkit.net.task.CallUrlTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Network request helper methods
 */

public class NetUtils {
    public static final int serverPort = 49199;
    private static final String serverIp = "192.168.0.1";
    private static final String baseUrl = String.format("http://%s/cam.cgi?mode=", serverIp);

    public static String apiUrl(String mode) {
        return String.format("%s%s", baseUrl, mode);
    }

    public static String apiUrl(String mode, int value) {
        return apiUrl(mode, String.valueOf(value));
    }

    public static String apiUrl(String mode, String value) {
        return String.format("%s%s&value=%s", baseUrl, mode, value);
    }

    public static void makeRequest(String address) {
        CallUrlTask task = new CallUrlTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, address);
    }

    public static void makeRequest(String address, CallUrlCallBack callBack) {
        CallUrlTask task = new CallUrlTask(callBack);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, address);
    }

    public static String makeRequest(URL url) {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(false);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Connection", "close");
            urlConnection.connect();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            return readStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return null;
    }

    private static String readStream(InputStream in) {
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
                System.out.println(line);
            }
            return result.toString();
        } catch (Exception e) {
            System.err.println("Error");
            e.printStackTrace();
        }
        return "";
    }
}
