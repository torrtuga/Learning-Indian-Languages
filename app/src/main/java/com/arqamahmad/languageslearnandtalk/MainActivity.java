package com.arqamahmad.languageslearnandtalk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener{

    public static String familyResponse = "";
    public static String numbersResponse = "";
    public static String pronounsResponse = "";
    public static String phrasesResponse = "";

    private Button mChatButton;
    private Button mLearnButton;
    private TextView mTextViewHeading;
    private SharedPreferences mSharedPreferences;
    private GoogleApiClient mGoogleApiClient;
    private String mPhotoUrl;

    public static final String ANONYMOUS = "anonymous";
    private String mUsername;
    private static final String TAG = "MainActivity";

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchDataFromWeb(); //Call to download data

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // default username is anonymous.
        mUsername = ANONYMOUS;
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        mChatButton = (Button)findViewById(R.id.buttonChat);
        mLearnButton = (Button)findViewById(R.id.buttonLearn);
        mTextViewHeading = (TextView)findViewById(R.id.textView);

        mChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Calling chat Activity
                Intent chatIntent = new Intent(view.getContext(),ChatActivity.class);
                startActivity(chatIntent);
            }
        });
        mLearnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Calling Kannada Activity
                Intent learnIntent = new Intent(view.getContext(),Kannada.class);
                startActivity(learnIntent);
            }
        });


    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mUsername = ANONYMOUS;
                startActivity(new Intent(this, SignInActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }


}
