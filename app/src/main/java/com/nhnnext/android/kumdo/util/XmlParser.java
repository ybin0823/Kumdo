package com.nhnnext.android.kumdo.util;

import android.util.Log;
import android.util.Xml;

import com.nhnnext.android.kumdo.model.User;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;

public class XmlParser {
    private static final String TAG = "XmlParser";

    public static User parse(String content) {
        try {
            XmlPullParser parser = Xml.newPullParser();
            /* namespace를 파싱할 것인지. 디폴트 값이 false */
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(content));
            int eventType = parser.getEventType();

            User user = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equals("response")) {
                            user = new User();
                            Log.d(TAG, "parser - start");
                        } else if (name.equals("email")) {
                            user.setEmail(parser.nextText());
                        } else if (name.equals("nickname")) {
                            user.setNickname(parser.nextText());
                        } else if (name.equals("enc_id")) {
                            user.setEnc_id(parser.nextText());
                        } else if (name.equals("profile_image")) {
                            user.setProfile_image(parser.nextText());
                        } else if (name.equals("age")) {
                            user.setAge(parser.nextText());
                        } else if (name.equals("gender")) {
                            user.setGender(parser.nextText());
                        } else if (name.equals("id")) {
                            user.setId(parser.nextText());
                        } else if (name.equals("name")) {
                            user.setName(parser.nextText());
                        } else if (name.equals("birthday")) {
                            user.setBirthday(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("response")) {
                            Log.d(TAG, "parer - end");
                        }
                        break;
                }
                eventType = parser.next();
            }
            return user;

        } catch (XmlPullParserException e) {
            Log.e(TAG, "XmlPullParserException", e);
        } catch (IOException e) {
            Log.e(TAG, "IOException occurred", e);
        }

        return null;
    }
}
