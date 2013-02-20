package es.aguasnegras.rneradio.services;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.IBinder;
import es.aguasnegras.rneradio.services.binders.RadioServiceBinder;
import es.aguasnegras.rneradio.utils.Logger;
import es.aguasnegras.rneradio.utils.MediaStatus;

public class RadioService extends Service {

	private final MediaPlayer radioPlayer = new MediaPlayer();
	private final RadioServiceBinder radioServiceBinder = new RadioServiceBinder(
			this);
	private MediaStatus status = MediaStatus.STOPPED;
	private String url;

	@Override
	public void onCreate() {
		super.onCreate();
		// TODO:Registrar notificationmanager y callmanager, etc...
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		this.radioPlayer.stop();
		this.radioPlayer.release();
	}

	@Override
	public IBinder onBind(Intent bindingIntent) {
		return this.radioServiceBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	public MediaStatus play(String url) {
		try {
			if (!this.status.equals(MediaStatus.PAUSED)) {
				if (!url.equals(this.url)) {
					this.url = url;
					if (this.radioPlayer.isPlaying()) {
						this.radioPlayer.stop();
					}
					this.radioPlayer.reset();
					this.radioPlayer.setDataSource(url);
					this.radioPlayer.prepare();
				}
			}
			this.radioPlayer.start();
			this.radioPlayer.setOnErrorListener(new OnErrorListener() {
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					Logger.e(this, "Error on mediaplayer instance: " + what
							+ " " + extra);
					return true;
				}
			});
			this.status = MediaStatus.PLAYING;
		} catch (IllegalArgumentException e) {
			Logger.e(this, "Error on play(): " + e.getLocalizedMessage());
			this.status = MediaStatus.ERROR;
		} catch (IllegalStateException e) {
			Logger.e(this, "Error on play(): " + e.getLocalizedMessage());
			this.status = MediaStatus.ERROR;
		} catch (IOException e) {
			Logger.e(this, "Error on play(): " + e.getLocalizedMessage());
			this.status = MediaStatus.ERROR;
		}
		return this.status;
	}

	public MediaStatus stop() {
		if (this.status.equals(MediaStatus.PLAYING)
				|| this.status.equals(MediaStatus.PAUSED)) {
			this.radioPlayer.stop();
			this.status = MediaStatus.STOPPED;
		}
		return this.status;
	}

	public MediaStatus getStatus() {
		return this.status;
	}

}
