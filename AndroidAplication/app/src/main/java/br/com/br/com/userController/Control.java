package br.com.br.com.userController;

import android.content.Context;
import android.content.Intent;

import org.json.JSONObject;

import br.com.br.com.utils.Constants;
import br.com.login.LoginActivity;

/**
 * Created by Fernando on 06/07/2014.
 */
public class Control {
    private static Control sControl;
    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;

    public void verificaToken(Context context) {
        if (Constants.TOKEN != null) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra("signout", true);
            context.startActivity(intent);
        }
    }

    /**
     * Verifica se o JSON apresenta mensagem de erro e se o usuário deve ser deslogado
     */
    public void testaJSON(JSONObject jsonObject, Context context) {
        try {
            String erro = (String) jsonObject.get("erro");
            if (erro != null && erro.equalsIgnoreCase("logout")) {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.putExtra("signout", true);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
