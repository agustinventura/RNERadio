package es.aguasnegras.rneradio.utils;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import es.aguasnegras.rneradio.services.RadioService;

public class CallManager extends PhoneStateListener {

	private final RadioService radioService;

	public CallManager(RadioService radioService) {
		this.radioService = radioService;
	}

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		if (state == TelephonyManager.CALL_STATE_RINGING
				|| state == TelephonyManager.CALL_STATE_OFFHOOK) {
			if (this.radioService.getStatus() == MediaStatus.PLAYING) {
				this.radioService.pause();
			}
		} else if (state == TelephonyManager.CALL_STATE_IDLE) {
			if (this.radioService.getStatus() == MediaStatus.PAUSED) {
				this.radioService.play();
			}
		}
		super.onCallStateChanged(state, incomingNumber);
	}
}
