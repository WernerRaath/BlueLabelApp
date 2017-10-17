package com.example.wraath.myapplication;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


// Using the Publicly available Volley library
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;


public class MainActivity extends AppCompatActivity {

    String url = "http://196.37.22.179:9011";

    /*
        A convenience function to wrap key-value pairs in the correct XML format
     */
    private String wrapXmlTag(String tag, String value){
        return "<" + tag + ">" + value + "</" + tag + ">";
    }

    /*
        A convenience function to convert input parameters into XML for the POST request
     */
    private String paramsToXML(String pin, String device_id, String device_ser, String device_ver,
                               String transType){
        String xml = "<request><EventType>Authentication</EventType>";
        xml += "<event>";
        xml += this.wrapXmlTag("UserPin", pin);
        xml += this.wrapXmlTag("DeviceId", device_id );
        xml += this.wrapXmlTag("DeviceSer", device_ser );
        xml += this.wrapXmlTag("DeviceVer", device_ver );
        xml += this.wrapXmlTag("TransType", transType );
        xml += "</event>";
        xml += "</request>";
        return xml;
    }

    /*
        Do a POST request with the authentication metadata in XML format and updates the UI as needed
     */
    private void authenticate(String pin, String device_id, String device_ser,
                               String device_ver, String trans_type){

        final TextView messageTextView = (TextView) findViewById(R.id.message);
        final TextView dataTextView = (TextView) findViewById(R.id.data);

        final String xml = this.paramsToXML(pin, device_id, device_ser, device_ver, trans_type);
        dataTextView.setText(xml);
        // Create a request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        messageTextView.setText(response);
                        messageTextView.setTextColor(Color.GREEN);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        messageTextView.setText(error.getLocalizedMessage());
                        messageTextView.setTextColor(Color.RED);
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/xml; charset=" +
                        getParamsEncoding();
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return xml.getBytes(getParamsEncoding());
                } catch (UnsupportedEncodingException uee) {
                    messageTextView.setText(uee.getLocalizedMessage());
                    messageTextView.setTextColor(Color.RED);
                    return null;
                }
            }
        };

        queue.add(request);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.authenticate("12345", "12345", "ABCDE", "ABCDE", "Users");
    }
}
