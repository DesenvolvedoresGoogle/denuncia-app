package br.com.br.com.userController;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import org.json.JSONObject;

import br.com.br.com.utils.Constants;
import br.com.login.LoginActivity;

/**
 * Created by Fernando on 06/07/2014.
 */
public class Control {
    private static Control sControl;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    private ConnectionResult mConnectionResult;
    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;

    public Control(GoogleApiClient.ConnectionCallbacks connectionsCallback, GoogleApiClient.OnConnectionFailedListener onConnectionFailed, Context context) {
        this.mGoogleApiClient = mGoogleApiClient;
        this.mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(connectionsCallback)
                .addOnConnectionFailedListener(onConnectionFailed).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
        this.mGoogleApiClient.connect();
    }

    public static Control getInstance(GoogleApiClient.ConnectionCallbacks connectionsCallback, GoogleApiClient.OnConnectionFailedListener onConnectionFailed, Context context) {
        if (sControl == null)
            sControl = new Control(connectionsCallback, onConnectionFailed, context);
        return sControl;
    }

    public void verificaToken(LoginActivity loginActivity) {
        if (Constants.TOKEN != null) {
            logoutGp(loginActivity);
        }
    }

    /**
     * Verifica se o JSON apresenta mensagem de erro e se o usuário deve ser deslogado
     */
    public void testaJSON(JSONObject jsonObject, Context context) {
        try {
            String erro = (String) jsonObject.get("erro");
            if (erro != null && erro.equalsIgnoreCase("logout")) {
                logoutGp(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Sign-out from google
     */
    public void logoutGp(Context context) {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            Constants.TOKEN = null;
        }
    }

    /**
     * Sign-in into google
     */
    public void signInWithGplus(LoginActivity loginActivity) {
        if (!mGoogleApiClient.isConnecting()) {
            resolveSignInError(loginActivity);
        }
    }

    /**
     * Method to resolve any signin errors
     */
    public void resolveSignInError(Activity activity) {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(activity, Constants.RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }


    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    public ConnectionResult getmConnectionResult() {
        return mConnectionResult;
    }

    public void setmConnectionResult(ConnectionResult mConnectionResult) {
        this.mConnectionResult = mConnectionResult;
    }
}
