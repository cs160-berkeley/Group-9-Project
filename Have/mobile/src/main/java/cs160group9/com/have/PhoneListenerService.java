package cs160group9.com.have;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class PhoneListenerService extends WearableListenerService {

    private static String TAG = "PhoneListenerService";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equalsIgnoreCase("xxxxx")) {

        } else {
            super.onMessageReceived(messageEvent);
        }

    }
}
