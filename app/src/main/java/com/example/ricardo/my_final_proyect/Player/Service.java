package com.example.ricardo.my_final_proyect.Player;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ricardo.my_final_proyect.R;

import java.util.ArrayList;


public class Service extends AsyncTask<String, Integer, String>
{
    private ProgressDialog progressDialog;
    private Context context;
    private GetPlayersActivity activity;
    private static final String debugTag = "Service";


    public Service(GetPlayersActivity activity) {
        super();
        this.activity = activity;
        this.context = this.activity.getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(this.activity, "Search", this.context.getResources().getString(R.string.looking_for_players) , true, false);
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Log.d(debugTag, "Background:" + Thread.currentThread().getName());
            String result = ServiceHelper.downloadFromServer(params);
            return result;
        } catch (Exception e) {
            return new String();
        }
    }

    @Override
    protected void onPostExecute(String result)
    {

        ArrayList<Player> playerArrayList = new ArrayList<>();

        progressDialog.dismiss();
        if (result.length() == 0) {
            Log.w("Service","Unable to find track data. Try again later.");
            return;
        }

        try {
            JSONArray playerArray = new JSONArray(result);
            for(int i=0; i<playerArray.length(); i++) {
                JSONObject playerObj = playerArray.getJSONObject(i);
                String playerName = playerObj.getString("Name");
                String matriculaPlayer= playerObj.getString("Matricula");
                String statusPlayer = playerObj.getString("Status");
                playerArrayList.add(new Player(matriculaPlayer, playerName, statusPlayer));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.activity.setPlayer(playerArrayList);


    }
}