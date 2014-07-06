package br.com.br.com.userController;

import android.content.IntentSender;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import org.json.JSONObject;

import java.security.spec.ECField;

import br.com.br.com.utils.Constants;
import br.com.login.LoginActivity;

/**
 * Created by Fernando on 06/07/2014.
 */
public class Control {
    public static void verificaToken(LoginActivity loginActivity){
        if(Constants.TOKEN != null){
            logoutGp(loginActivity);
        }
    }

    /**
     * Verifica se o JSON apresenta mensagem de erro e se o usuário deve ser deslogado
     */
    public static void testaJSON(JSONObject jsonObject , LoginActivity loginActivity){
        try {
            String erro = (String) jsonObject.get("erro");
            if(erro != null && erro.equalsIgnoreCase("logout")){
                logoutGp(loginActivity);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    /**
     * Sign-out from google
     */
    public static void logoutGp(LoginActivity loginActivity){
        if (loginActivity.getmGoogleApiClient().isConnected()) {
            Plus.AccountApi.clearDefaultAccount(loginActivity.getmGoogleApiClient());
            loginActivity.getmGoogleApiClient().disconnect();
            loginActivity.getmGoogleApiClient().connect();
            loginActivity.updateUI(false);
            Constants.TOKEN = null;
        }
    }

    /**
     * Sign-in into google
     */
    public static void signInWithGplus(LoginActivity loginActivity) {
        if (! loginActivity.getmGoogleApiClient().isConnecting()) {
            loginActivity.setmSignInClicked(true);
            resolveSignInError(loginActivity);
        }
    }

    /**
     * Method to resolve any signin errors
     */
    public static void resolveSignInError(LoginActivity loginActivity) {
        if (loginActivity.getmConnectionResult().hasResolution()) {
            try {
                loginActivity.setmIntentInProgress(true);
                loginActivity.getmConnectionResult().startResolutionForResult(loginActivity, Constants.RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                loginActivity.setmIntentInProgress(false);
                loginActivity.getmGoogleApiClient().connect();
            }
        }
    }


}
