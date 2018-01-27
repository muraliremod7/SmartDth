package smartdth.murali.com.smartdth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import smartdth.murali.com.smartdth.services.SessionManager;

public class SplashActivity extends AppCompatActivity {
    private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(i);
            SplashActivity.this.finish();
        }
    };
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("first_time", false))
        {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("first_time", true);
            editor.commit();
            Intent i = new Intent(SplashActivity.this, SplashActivity.class);
            this.startActivity(i);
            this.finish();
        }
        else
        {
            this.setContentView(R.layout.activity_splash);
            handler.sendEmptyMessageDelayed(0, 2000);
        }

    }
}
