package org.secretwpn.lumixtoolkit.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.secretwpn.lumixtoolkit.ui.StreamDisplay;
import org.secretwpn.lumixtoolkit.util.NetUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

/**
 * Class responsible for receiving live video stream from the camera
 */

public class VideoFeed extends AsyncTask<String, Void, String> {
    private StreamDisplay display;
    private boolean isRunning;
    private DatagramSocket serverSocket = null;

    public VideoFeed(StreamDisplay display) {
        this.display = display;
    }

    private void readData() {
        try {
            this.serverSocket = new DatagramSocket(NetUtils.serverPort);
            serverSocket.setSoTimeout(1500);
            System.out.println("UDP Socket ready on port " + NetUtils.serverPort);
        } catch (SocketException e) {
            System.out.println("Socket creation error : " + e.getMessage());
        }
        int n = 132;
        byte[] arrby = new byte[30000];
        isRunning = true;
        while (isRunning) {
            try {
                DatagramPacket datagramPacket = new DatagramPacket(arrby, arrby.length);
                serverSocket.receive(datagramPacket);
                byte[] data = datagramPacket.getData();
                for (int i = 130; i < 320; ++i) {
                    if (data[i] != -1 || data[i + 1] != -40) continue;
                    n = i;
                }
                byte[] bitmapdata = Arrays.copyOfRange(data, n, datagramPacket.getLength());
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                display.setImage(bitmap);
                display.postInvalidate();
            } catch (SocketTimeoutException ste) {
                System.err.println("timeout");
            } catch (IOException e) {
                System.out.println("Error with client request : " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        serverSocket.close();
        NetUtils.makeRequest(NetUtils.apiUrl("stopstream"));
    }

    public void kill() {
        isRunning = false;
    }

    @Override
    protected String doInBackground(String... strings) {
        readData();
        return null;
    }
}
