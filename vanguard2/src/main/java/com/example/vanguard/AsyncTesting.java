package com.example.vanguard;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

/**
 * Created by BertTurtle on 4/12/2017.
 */

public class AsyncTesting extends AsyncTask<URL, Boolean, String> {
    @Override
    protected String doInBackground(URL... urls) {
        String input = "";
        try {
            System.out.println("Running");
            URL blueAlliance = new URL("https://www.thebluealliance.com/api/v2/team/frc254?X-TBA-App-Id=frc5940:scouter:v01");

            HttpURLConnection blueAllianceConnection = (HttpURLConnection) blueAlliance.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(blueAllianceConnection.getInputStream()));

            while ((input = in.readLine()) != null)
                System.out.println(input);
            in.close();
        }
        catch (Exception e) {
            System.out.println("Running");
            e.printStackTrace();
        }
        return input;
    }
}
