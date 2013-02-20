package es.aguasnegras.rneradio.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import es.aguasnegras.rneradio.R;
import es.aguasnegras.rneradio.services.RadioService;
import es.aguasnegras.rneradio.services.binders.RadioServiceBinder;
import es.aguasnegras.rneradio.utils.MediaStatus;

public class MainActivity extends Activity {

	private RadioService radioService = null;
	private final ServiceConnection radioServiceConnection = new RadioServiceConnection();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent intent = new Intent(this, RadioService.class);
		this.startService(intent);
	}

	@Override
	public void onStart() {
		super.onStart();
		Intent intent = new Intent(this, RadioService.class);
		this.bindService(intent, radioServiceConnection,
				Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onStop() {
		this.unbindService(radioServiceConnection);
		super.onStop();
	}

	@Override
	public void onDestroy() {
		if (!this.radioService.getStatus().equals(MediaStatus.PLAYING)
				&& !this.radioService.getStatus().equals(MediaStatus.PAUSED)) {
			Intent intent = new Intent(getBaseContext(), RadioService.class);
			this.stopService(intent);
		}
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void play(View v) {
		this.radioService
				.play("http://195.10.10.206/rtve/radioclasica.mp3?GKID=e795ab58f1f911e1bc0500163ea2c743&fspref=aHR0cDovL2Jsb3FudW0uY29tL2RpcmVjY2lvbmVzLWh0dHAtZGUtbGFzLXJhZGlvcy1lc3Bhbm9sYXMtZW4tc3RyZWFtaW5nLXdlYi8%3D");
		this.renderStopButton();
	}

	public void stop(View v) {
		this.radioService.stop();
		this.renderPlayButton();
	}

	private void renderStopButton() {
		Button playButton = (Button) this.findViewById(R.id.button1);
		playButton.setVisibility(View.GONE);
		Button stopButton = (Button) this.findViewById(R.id.button2);
		stopButton.setVisibility(View.VISIBLE);
	}

	private void renderPlayButton() {
		Button playButton = (Button) this.findViewById(R.id.button1);
		playButton.setVisibility(View.VISIBLE);
		Button stopButton = (Button) this.findViewById(R.id.button2);
		stopButton.setVisibility(View.GONE);
	}

	private final class RadioServiceConnection implements ServiceConnection {
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			if (radioService == null) {
				RadioServiceBinder radioServiceBinder = (RadioServiceBinder) service;
				radioService = radioServiceBinder.getRadioService();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			radioService = null;
		}
	}

}
