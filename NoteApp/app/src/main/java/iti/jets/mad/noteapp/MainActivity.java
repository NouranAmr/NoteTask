package iti.jets.mad.noteapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private LoginButton fbLoginButton;
    private CallbackManager callbackManager;
    private SharedPreferences sharedpreferences;
    public static final String MY_PERFERENCES = "MyPerf";
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //sharedpreferences = getSharedPreferences(MY_PERFERENCES, Context.MODE_PRIVATE);
        sharedpreferences= PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedpreferences.edit();
        fbLoginButton=findViewById(R.id.login_button);
        callbackManager= CallbackManager.Factory.create();
        String userID=sharedpreferences.getString("userID",null);
        if(userID!="" && userID!=null)
        {
            
            startActivity(new Intent(MainActivity.this, NoteListActivity.class));
        }
        fbLoginButton.setReadPermissions(Arrays.asList("email","public_profile"));
        fbLoginButton.setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY);
        fbLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookLogin();
            }
        });
    }

    private void facebookLogin() {
        // Callback registration
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                final AccessToken accessToken = loginResult.getAccessToken();
                GraphRequestAsyncTask request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                        LoginManager.getInstance().logOut();
                       // make shared perfrences here and insert id

                        editor.putString("userID",user.opt("id").toString());
                        editor.commit();
                       // Toast.makeText(MainActivity.this, sharedpreferences.getString("userID","no"), Toast.LENGTH_SHORT).show();
                    }
                }).executeAsync();


                Toast.makeText(getApplicationContext(), "Login Success with facebook", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, NoteListActivity.class));


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

