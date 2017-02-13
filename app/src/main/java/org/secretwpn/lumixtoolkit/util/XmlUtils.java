package org.secretwpn.lumixtoolkit.util;

import android.util.Xml;

import org.secretwpn.lumixtoolkit.model.CameraState;
import org.secretwpn.lumixtoolkit.ui.StreamDisplay;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;

/**
 * XML parsing helper methods
 */

public class XmlUtils {
    private static XmlPullParser parser = createParser();

    private static XmlPullParser createParser() {
        parser = Xml.newPullParser();
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return parser;
    }

    private static String readTag(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        String text = "";
        try {
            parser.require(XmlPullParser.START_TAG, null, tag);
            text = readText(parser);
            parser.require(XmlPullParser.END_TAG, null, tag);
            parser.nextTag();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    private static String readText(XmlPullParser parser) {
        String result = "";
        try {
            if (parser.next() == XmlPullParser.TEXT) {
                result = parser.getText();
                parser.nextTag();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @SuppressWarnings("TryFinallyCanBeTryWithResources")
    public static void parseState(StreamDisplay display, String rawStateXml) {
        if (rawStateXml == null || rawStateXml.length() == 0)
            return;
        CameraState state = display.getState();
        StringReader reader = new StringReader(rawStateXml);
        try {
            parser.setInput(reader);
            parser.nextTag();
            parser.nextTag();
            state.setResult(XmlUtils.readText(parser));
            parser.next();
            parser.next();
            state.setBattery(XmlUtils.readTag(parser, "batt"));
            state.setMode(XmlUtils.readTag(parser, "cammode"));
            state.setAvailablePhoto(XmlUtils.readTag(parser, "remaincapacity"));
            state.setSdCardStatus(XmlUtils.readTag(parser, "sdcardstatus"));
            state.setSdMemory(XmlUtils.readTag(parser, "sd_memory"));
            state.setAvailableVideo(XmlUtils.readTag(parser, "video_remaincapacity"));
            state.setIsRecording(XmlUtils.readTag(parser, "rec"));
            state.setBurstIntervalStatus(XmlUtils.readTag(parser, "burst_interval_status"));
            state.setSdAccess(XmlUtils.readTag(parser, "sd_access"));
            XmlUtils.readTag(parser, "rem_disp_typ");
            XmlUtils.readTag(parser, "version");
            System.out.println(state.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }
    }
}
