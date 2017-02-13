package org.secretwpn.lumixtoolkit.net.task;

/**
 * CallBack interface for async CallUrlTask.
 * Gives opportunity to specify how to process the response
 */

public interface CallUrlCallBack {
    void processResult(String result);
}
