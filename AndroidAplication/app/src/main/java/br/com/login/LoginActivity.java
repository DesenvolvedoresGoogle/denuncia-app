package br.com.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.br.com.userController.Control;
import br.com.br.com.utils.Constants;
import br.com.denuncia.MainActivity;

public class LoginActivity extends Activity implements OnClickListener,
        ConnectionCallbacks, OnConnectionFailedListener, Json.PostListener {

    // Logcat tag
    private static final String TAG = "LoginActivity";

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;

    private String idUSer, photoUser, nameUser;

    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;


    private boolean mSignInClicked;

    private Control mControl;

    private SignInButton btnSignIn;
    private Button btnSignOut, btnRevokeAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);

        // Button click listeners
        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnRevokeAccess.setOnClickListener(this);

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        mControl = Control.getInstance(mGoogleApiClient);
        
    }

    protected void onStart() {
        super.onStart();
        mControl.getmGoogleApiClient().connect();
    }

    protected void onStop() {
        super.onStop();
        if (mControl.getmGoogleApiClient().isConnected()) {
            mControl.getmGoogleApiClient().disconnect();
        }
    }



    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mControl.setmConnectionResult(result);

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                mControl.resolveSignInError(this);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        if (requestCode == Constants.RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mControl.getmGoogleApiClient().isConnecting()) {
                mControl.getmGoogleApiClient().connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mControl.getmGoogleApiClient().connect();
        Toast.makeText(this, "Não foi possível autenticar.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Button on click listener
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                // Signin button clicked
                mControl.resolveSignInError(this);
                break;
            case R.id.btn_sign_out:
                // Signout button clicked
                mControl.logoutGp(this);
                break;
            case R.id.btn_revoke_access:
                // Revoke access button clicked
                revokeGplusAccess();
                break;
        }
    }

    /**
     * Revoking access from google
     */
    private void revokeGplusAccess() {
        if (mControl.getmGoogleApiClient().isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mControl.getmGoogleApiClient());
            Plus.AccountApi.revokeAccessAndDisconnect(mControl.getmGoogleApiClient())
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(TAG, "User access revoked!");
                            mControl.getmGoogleApiClient().connect();
                            Toast.makeText(LoginActivity.this, "Não foi possível autenticar.", Toast.LENGTH_SHORT).show();
                        }

                    });
        }
    }

    /**
     * function make Login Request
     */

    public void userLogin() {
        try {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);


            params.add(new BasicNameValuePair("google_id", idUSer));
            params.add(new BasicNameValuePair("name", nameUser));
            params.add(new BasicNameValuePair("photo", photoUser));

            // getting JSON Object
            Json.post(this, Constants.URL + Constants.LOGIN_USER, params);
            // return json

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Fetching user's information name, email, profile pic
     */
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mControl.getmGoogleApiClient()) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mControl.getmGoogleApiClient());
                nameUser = currentPerson.getDisplayName();
                photoUser = currentPerson.getImage().getUrl();
                idUSer = currentPerson.getId();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mControl.getmGoogleApiClient());

                Log.e(TAG, "Google ID: " + idUSer + ", Name: " + nameUser + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + photoUser);


                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                photoUser = photoUser.substring(0,
                        photoUser.length() - 2)
                        + PROFILE_PIC_SIZE;

                userLogin();

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostSent(JSONObject jsonObject){
        try {
            String status = (String) jsonObject.get("status");
            if(status.equalsIgnoreCase("sucess")){
                Constants.TOKEN = (String) jsonObject.get("token");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

