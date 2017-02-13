package org.secretwpn.lumixtoolkit.net.task;

import android.os.AsyncTask;

import org.secretwpn.lumixtoolkit.util.NetUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Async task to perform network calls outside of main thread.
 * Executes callBack onPostExecute, if callBack is set
 */

public class CallUrlTask extends AsyncTask<String, Void, String> {
    private CallUrlCallBack callBack;

    public CallUrlTask() {
        super();
    }

    public CallUrlTask(CallUrlCallBack callBack) {
        this();
        this.callBack = callBack;
    }

    @Override
    protected String doInBackground(String... params) {
        System.out.println("Make request " + params[0]);
        URL url = null;
        try {
            url = new URL(params[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return NetUtils.makeRequest(url);
    }

    @Override
    protected void onPostExecute(String result) {
        if (callBack != null)
            callBack.processResult(result);
    }

}
