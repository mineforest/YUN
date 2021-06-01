package com.example.poke;

import android.os.StrictMode;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BarcodeApiCaller {

    String p_name;
    String p_cate;
    String p_date;
    String barcode_num;

    String key = "2b7ec673daf748c0af0a";
    String url = "http://openapi.foodsafetykorea.go.kr/api/"+key+"/C005/xml/1/5/BAR_CD=";

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_cate() {
        return p_cate;
    }

    public void setP_cate(String p_cate) {
        this.p_cate = p_cate;
    }

    public String getP_date() {
        return p_date;
    }

    public void setP_date(String p_date) {
        this.p_date = p_date;
    }

    public void getXmlData(String barcode_no){
        barcode_num = barcode_no;
        String queryUrl= url + barcode_no;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try{
            URL url= new URL(queryUrl);
            InputStream is= url.openStream();

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") );

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();

                        if(tag.equals("PRDLST_DCNM")){
                            xpp.next();
                            p_cate = xpp.getText();
                        }
                        else if(tag.equals("PRDLST_NM")){
                            xpp.next();
                            p_name = xpp.getText();
                        }
                        else if(tag.equals("POG_DAYCNT")){
                            xpp.next();
                            p_date = DateInverter(xpp.getText());
                        }
                        break;

                    case XmlPullParser.TEXT:
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType= xpp.next();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private String DateInverter(String date) {
        String[] tok = date.split(" ");
        String tmp = tok[tok.length-1];
        int howlong = Integer.parseInt(tmp.replaceAll("[^0-9]",""));
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        if (tmp.contains("개월") || tmp.contains("달") || tmp.contains("월")) {
            cal.add(Calendar.MONTH, howlong);
        }
        else {
            cal.add(Calendar.YEAR, 1);
        }
        return df.format(cal.getTime());
    }
}
