package com.antechdevelopment.todoapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import com.antechdevelopment.todoapp.R;

public class SplashActivity extends AppCompatActivity {
	public static final int SPLASH_SCREEN_TIME = 2000;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run () {
				// Create a new intent to start a new activity
				Intent newIntent = new Intent(SplashActivity.this, MainActivity.class);

				// Launch the new intent
				startActivity(newIntent);

				// Close the current activity
				SplashActivity.this.finish();
			}
		}, SPLASH_SCREEN_TIME);
	}
}
