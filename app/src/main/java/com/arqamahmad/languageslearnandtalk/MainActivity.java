package com.arqamahmad.languageslearnandtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static String familyResponse = "";
    public static String numbersResponse = "";
    public static String pronounsResponse = "";
    public static String phrasesResponse = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchDataFromWeb();

        String[] languages = {"Kannada","Tamil","Telugu","Malayalam","Bengali"}; //String array with language name

        ListAdapter arrayAdapter = new ArrayAdapter<String>(this,R.layout.main_activity_array_adapter,
                R.id.textView,languages);

        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter); //populating listview with the adapter

        //Starting activities for the particular languages on clicking
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch(position){
                    case 0:
                        Intent kannadaIntent = new Intent(MainActivity.this,Kannada.class);
                        startActivity(kannadaIntent);
                        break;
                }
            }
        });
    }

    //Getting Data From Internet
    private void fetchDataFromWeb(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                InputStream inputStream = null;
                int statusCode ;
                try{
                    URL urlFamily = new URL("http://arqamahmad.com/language_app/kannadaFamilyPrint.php");
                    URL urlNumbers = new URL("http://arqamahmad.com/language_app/kannadaNumbersPrint.php");
                    URL urlPronouns = new URL("http://arqamahmad.com/language_app/kannadaPronounsPrint.php");
                    URL urlPhrases = new URL("http://arqamahmad.com/language_app/kannadaPhrasesPrint.php");

                    //Family Data
                    urlConnection = (HttpURLConnection) urlFamily.openConnection();
                    urlConnection.setRequestMethod("GET");
                    statusCode = urlConnection.getResponseCode();
                    if(statusCode == 200){
                        inputStream = new BufferedInputStream(urlConnection.getInputStream());
                        familyResponse = convertInputStreamToString(inputStream);
                        Log.i("Got Songs", familyResponse);

                    }
                    //Numbers Data
                    urlConnection = (HttpURLConnection) urlNumbers.openConnection();
                    urlConnection.setRequestMethod("GET");
                    statusCode = urlConnection.getResponseCode();
                    if(statusCode == 200){
                        inputStream = new BufferedInputStream(urlConnection.getInputStream());
                        numbersResponse = convertInputStreamToString(inputStream);

                    }
                    //Pronouns Data
                    urlConnection = (HttpURLConnection) urlPronouns.openConnection();
                    urlConnection.setRequestMethod("GET");
                    statusCode = urlConnection.getResponseCode();
                    if(statusCode == 200){
                        inputStream = new BufferedInputStream(urlConnection.getInputStream());
                        pronounsResponse = convertInputStreamToString(inputStream);

                    }
                    //Phrases Data
                    urlConnection = (HttpURLConnection) urlPhrases.openConnection();
                    urlConnection.setRequestMethod("GET");
                    statusCode = urlConnection.getResponseCode();
                    if(statusCode == 200){
                        inputStream = new BufferedInputStream(urlConnection.getInputStream());
                        phrasesResponse = convertInputStreamToString(inputStream);

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    if(urlConnection != null){
                        urlConnection.disconnect();
                    }
                }
            }
        });
        thread.start();
    }

    //Converting InputStream to String
    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null){
            result += line;
        }
        if(inputStream != null){
            inputStream.close();
        }
        return result;
    }



}
