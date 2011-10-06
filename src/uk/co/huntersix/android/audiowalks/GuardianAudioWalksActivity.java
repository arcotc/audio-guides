package uk.co.huntersix.android.audiowalks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GuardianAudioWalksActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void startSecondActivity(View view) {
    	Intent myIntent = new Intent(GuardianAudioWalksActivity.this, WalksListViewActivity.class);
    	GuardianAudioWalksActivity.this.startActivity(myIntent);
    }
}