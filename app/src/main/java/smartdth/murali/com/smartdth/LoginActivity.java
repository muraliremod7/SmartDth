package smartdth.murali.com.smartdth;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;
import smartdth.murali.com.smartdth.services.ConnectionDetector;
import smartdth.murali.com.smartdth.services.SessionManager;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {
    private EditText emailAdd,pinNum;
    private Button login;
    private TextView register;
    private ConnectionDetector connection;
    private ProgressDialog progressBar;
    private FirebaseAuth auth;
    private SessionManager session;
    private SharedPreferences.Editor editor;
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailAdd = (EditText)findViewById(R.id.loginEmail);
        pinNum = (EditText)findViewById(R.id.loginPin);
        login = (Button)findViewById(R.id.loginSignin);
        login.setOnClickListener(this);
        register = (TextView)findViewById(R.id.loginRegister);
        register.setOnClickListener(this);
        linearLayout = (LinearLayout)findViewById(R.id.snackbar);

        session = new SessionManager(LoginActivity.this);
        connection = new ConnectionDetector(LoginActivity.this);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Please Wait .....");
        progressBar.setProgress(0);//initially progress is 0
        progressBar.setCancelable(false);
        progressBar.setMax(100);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginSignin:
                progressBar.show();
                String mobile = emailAdd.getText().toString();
                String pin = pinNum.getText().toString();
                if(mobile.equals("")){
                    emailAdd.setError("Enter Email");
                }else if(pin.equals("")){
                    pinNum.setError("Enter Password");
                }
                else {
                    login(mobile,pin);
                }
                break;
            case R.id.loginRegister:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
        }
    }

    private void login(final String loginemail, final String loginpassword) {
        auth = FirebaseAuth.getInstance();
        //authenticate user
        auth.signInWithEmailAndPassword(loginemail, loginpassword)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            progressBar.dismiss();
                            setSnackBar(linearLayout,"Check Your Email and Password");
                            // there was an error
                        } else {
                            progressBar.dismiss();
                            session.createLoginSession(loginemail,loginpassword);
                            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            editor = settings.edit();
                            editor.putString("password", loginpassword);
                            editor.putString("email",loginemail);
                            String key = loginemail.replace("@","1");
                            String uniqkey = key.replace(".","2");
                            editor.putString("uid", uniqkey);
                            editor.commit();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            // Add new Flag to start new Activity
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            Toasty.success(LoginActivity.this,"Login Successfull", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(this,R.style.MyAlertDialogStyle)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                        //close();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                })
                .show();

    }
    public static void setSnackBar(View coordinatorLayout, String snackTitle) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, snackTitle, Snackbar.LENGTH_SHORT);
        snackbar.show();
        View view = snackbar.getView();
        TextView txtv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
    }
}

