package com.example.poke;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class BarcodeScannerActivity extends AppCompatActivity {
    TextView txt;
    EditText prod_name;
    EditText prod_cat;
    EditText daycnt;
    Dialog dialog01;


    String key = "2b7ec673daf748c0af0a";
    String url = "http://openapi.foodsafetykorea.go.kr/api/"+key+"/C005/xml/1/5/BAR_CD=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog01 = new Dialog(BarcodeScannerActivity.this);
        dialog01.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog01.setContentView(R.layout.ingre_popup);

        showDialog();
    }

    public void showDialog() {
        dialog01.show();

        txt = (TextView)dialog01.findViewById(R.id.txtText);
        prod_name = dialog01.findViewById(R.id.prod_name_txt);
        prod_cat = dialog01.findViewById(R.id.prod_cat_txt);
        daycnt = dialog01.findViewById(R.id.daycnt_txt);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Intent intent = getIntent();
        String data = intent.getStringExtra("RESULT");

        String[] apiout = getXmlData(data).split("-");

        prod_name.setText(apiout[0]);
        prod_cat.setText(apiout[1]);
        daycnt.setText(apiout[2]);
        txt.setText(data);
    }

    public void mOnClose(View v) {
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        finish();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        return;
    }
    private String getXmlData(String barcode_no){

        StringBuffer buffer=new StringBuffer();
        String queryUrl= url + barcode_no;
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
                            buffer.append(xpp.getText());
                        }
                        else if(tag.equals("PRDLST_NM")){
                            buffer.append("-");
                            xpp.next();
                            buffer.append(xpp.getText());
                        }
                        else if(tag.equals("POG_DAYCNT")){
                            buffer.append("-");
                            xpp.next();
                            buffer.append(xpp.getText());
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType= xpp.next();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return buffer.toString();

    }
}