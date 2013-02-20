package es.aguasnegras.rneradio.services.binders;

import android.os.Binder;
import es.aguasnegras.rneradio.services.RadioService;

public class RadioServiceBinder extends Binder {

	private final RadioService radioService;

	public RadioServiceBinder(RadioService radioService) {
		this.radioService = radioService;
	}

	public RadioService getRadioService() {
		return this.radioService;
	}
}
