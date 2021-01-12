package com.e.top10iphonesapps;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class PassAplications {
    private static final String TAG = "PassAplications";

    private ArrayList<FeedEntry> applications;

    public PassAplications() {
        this.applications = new ArrayList<>();

    }

    public ArrayList<FeedEntry> getApplications() {
        return applications;
    }

    public boolean parse(String xmlData){
        boolean status = true;
        FeedEntry currentRecord = null;
        boolean inEntry = false;
        String textValue = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int evenType = xpp.getEventType();
            while (evenType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();
                switch (evenType){
                    case XmlPullParser.START_TAG:
 //                       Log.d(TAG, "parse: Staring tag for: " + tagName);
                        if("entry".equalsIgnoreCase(tagName)){
                            inEntry = true;
                            currentRecord = new FeedEntry();
                        }
                    break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        Log.d(TAG, "parse: Staring text for " + textValue);
                    break;


                    case XmlPullParser.END_TAG:
                        Log.d(TAG, "parse: Ending Tag for: " + tagName);
                        if(inEntry){
                            if("entry".equalsIgnoreCase(tagName)){
                                applications.add(currentRecord);
                                inEntry = false;
                            } else if("name".equalsIgnoreCase(tagName)){
                                currentRecord.setName(textValue);
                            } else if("artist".equalsIgnoreCase(tagName)){
                                currentRecord.setArtist(textValue);
                            } else if("releaseDate".equalsIgnoreCase(tagName)){
                                currentRecord.setRealeasedate(textValue);
                            }else if("summary".equalsIgnoreCase(tagName)){
                                currentRecord.setSummary(textValue);
                            }else if("image".equalsIgnoreCase(tagName)){
                                currentRecord.setImageUrl(textValue);
                            }
                        }
                    break;
                    default:
                        // nothing else to do
                }

                evenType  = xpp.next();
            }

            for(FeedEntry i: applications){
                Log.d(TAG, "******************");
                Log.d(TAG, i.toString());
            }

        }catch (Exception e){
           status = false;
           e.printStackTrace();
        }

        return status;
    }
}
