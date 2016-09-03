package com.arqamahmad.languageslearnandtalk;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener,
        FragmentManager.OnBackStackChangedListener{

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
    private static final int REQUEST_INVITE = 1;

    public static final String ANONYMOUS = "anonymous";
    private String mUsername;
    private static final String TAG = "MainActivity";

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAnalytics mFirebaseAnalytics;

    // Whether or not we're showing the back of the card (otherwise showing the front)
    private boolean mShowingBack = false;
    private Timer timer;

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
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
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
                .addApi(AppInvite.API)
                .build();

        mChatButton = (Button)findViewById(R.id.buttonChat);
        mLearnButton = (Button)findViewById(R.id.buttonLearn);
        //mTextViewHeading = (TextView)findViewById(R.id.textView);

        //CardFlip Fragment
        if (savedInstanceState == null) {
            // If there is no saved instance state, add a fragment representing the
            // front of the card to this activity. If there is saved instance state,
            // this fragment will have already been added to the activity.
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new CardFrontFragment())
                    .commit();
        } else {
            mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
        }
        // Monitor back stack changes to ensure the action bar shows the appropriate
        // button (either "photo" or "info").
        getFragmentManager().addOnBackStackChangedListener(this);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {flipCard();}
        }, 0, 1500);//here time 1000 milliseconds=1 second

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
            case R.id.invite_menu:
                sendInvitation();
                return true;
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

    private void sendInvitation() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                Bundle payload = new Bundle();
                payload.putString(FirebaseAnalytics.Param.VALUE, "sent");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE,
                        payload);
                // Check how many invitations were sent and log.
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode,
                        data);
                //Log.d(TAG, "Invitations sent: " + ids.length);
            } else {
                Bundle payload = new Bundle();
                payload.putString(FirebaseAnalytics.Param.VALUE, "not sent");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE,
                        payload);
                // Sending failed or it was canceled, show failure message to
                // the user
                //Log.d(TAG, "Failed to send invitation.");
            }
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
                        //Log.i("Got Songs", familyResponse);

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
        //Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }


    //Flipping the fragment Card
    private void flipCard() {
        if (mShowingBack) {
            getFragmentManager().popBackStack();
            return;
        }

        // Flip to the back.

        mShowingBack = true;

        // Create and commit a new fragment transaction that adds the fragment for
        // the back of the card, uses custom animations, and is part of the fragment
        // manager's back stack.

        getFragmentManager()
                .beginTransaction()

                // Replace the default fragment animations with animator resources
                // representing rotations when switching to the back of the card, as
                // well as animator resources representing rotations when flipping
                // back to the front (e.g. when the system Back button is pressed).
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)

                // Replace any fragments currently in the container view with a
                // fragment representing the next page (indicated by the
                // just-incremented currentPage variable).
                .replace(R.id.container, new CardBackFragment())

                // Add this transaction to the back stack, allowing users to press
                // Back to get to the front of the card.
                .addToBackStack(null)

                // Commit the transaction.
                .commit();
    }

    @Override
    public void onBackStackChanged() {
        mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);

        // When the back stack changes, invalidate the options menu (action bar).
        invalidateOptionsMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.numbersResponse == ""){
            fetchDataFromWeb();
        }
    }


    @Override
    protected void onPause() {
        timer.cancel();
        super.onPause();
    }

    @Override
    protected void onStop() {
        timer.cancel();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //timer.cancel();
        super.onDestroy();
    }
}
