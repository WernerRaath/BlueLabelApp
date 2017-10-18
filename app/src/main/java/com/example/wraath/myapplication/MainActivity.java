package com.example.wraath.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {

    String ip = "196.37.22.179";
    int port = 9011;
    int attempt_count = 0;

    /*
        A convenience function to wrap key-value pairs in the correct XML format
     */
    private String wrapXmlTag(String tag, String value){
        return "<" + tag + ">" + value + "</" + tag + ">\n";
    }

    /*
        A convenience function to convert input parameters into XML for the POST request
     */
    private String paramsToXML(String pin, String device_id, String device_ser, String device_ver,
                               String transType){
        String xml = "<request>\n";
        xml += this.wrapXmlTag("EventType", "Authentication");
        xml += "<event>\n";
        xml += this.wrapXmlTag("UserPin", pin);
        xml += this.wrapXmlTag("DeviceId", device_id );
        xml += this.wrapXmlTag("DeviceSer", device_ser );
        xml += this.wrapXmlTag("DeviceVer", device_ver );
        xml += this.wrapXmlTag("TransType", transType );
        xml += "</event>\n";
        xml += "</request>\n";
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
        String message = "Connecting to " + ip + ":" + port;

        messageTextView.setText(message);
        messageTextView.setTextColor(Color.BLUE);
        attempt_count++;

        try {
            TelnetClient telnet = new TelnetClient();
            telnet.connect(ip, port);
            InputStream in = telnet.getInputStream();
            PrintStream out = new PrintStream(telnet.getOutputStream());
            out.println(xml);
            out.flush();

            String m, response = "";
            while (true){
                if ((m = in.read() + "") == null) break;
                response += m;
            }
            in.close();
            out.close();
            telnet.disconnect();

            Socket socket = new Socket(ip, port);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.print(xml + "\r\n");
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true){
                if ((m = reader.readLine()) == null) break;
                response += m + "\n";
            }
            writer.close();
            reader.close();
            socket.close();

            message = response;
            messageTextView.setTextColor(Color.GREEN);
        } catch (Exception e){
            message = e.getLocalizedMessage() +
                    "\nConnection to " + ip + ":" + port + " failed...";
            messageTextView.setTextColor(Color.RED);
        }
        messageTextView.setText("[Attempt " + attempt_count + "]: " + message );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.authenticate("12345", "12345", "ABCDE", "ABCDE", "Users");

        final Button refreshButton = (Button) findViewById(R.id.button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticate("12345", "12345", "ABCDE", "ABCDE", "Users");
            }
        });
    }
}
