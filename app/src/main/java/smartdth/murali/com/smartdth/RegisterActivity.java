package smartdth.murali.com.smartdth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText email,password;
    private Button register;
    private TextView loginhere;
    private FirebaseAuth auth;
    private ProgressDialog progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        email = (EditText)findViewById(R.id.regemailId);
        password = (EditText)findViewById(R.id.regpassWord);
        register = (Button)findViewById(R.id.register);
        register.setOnClickListener(this);
        loginhere = (TextView)findViewById(R.id.regRegister);
        loginhere.setOnClickListener(this);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Please Wait .....");
        progressBar.setCancelable(false);
        progressBar.setProgress(0);//initially progress is 0
        progressBar.setMax(100);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                progressBar.show();
                String emailreg = email.getText().toString().trim();
                String passwordreg = password.getText().toString().trim();

                if (TextUtils.isEmpty(emailreg)) {
                    Toasty.info(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(passwordreg)) {
                    Toasty.info(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toasty.info(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //create user
                auth.createUserWithEmailAndPassword(emailreg, passwordreg)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toasty.success(RegisterActivity.this, "Registration Is Completed", Toast.LENGTH_SHORT).show();
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    progressBar.dismiss();
                                } else {
                                    progressBar.dismiss();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    finish();
                                }
                            }
                        });
                break;
            case R.id.regRegister:
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
