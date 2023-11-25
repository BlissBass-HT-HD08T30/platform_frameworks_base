package org.blissos.server.powermanager;

import android.content.Context;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import com.android.server.SystemService;
import org.blissos.powermanager.BlissPowerManager;
import org.blissos.powermanager.IBlissPower;

public class BlissPowerService extends SystemService {
    private static final String TAG = "BlissPowerService";

    private final Context mContext;
    private PowerManager mPowerManager;

    public BlissPowerService(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void onStart() {
        mPowerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);

        publishBinderService(BlissPowerManager.SERVICE_NAME, mService);
    }

    private final IBlissPower.Stub mService = new IBlissPower.Stub() {
        @Override
        public void shutdown() throws RemoteException {
            long token = clearCallingIdentity();
            mPowerManager.shutdown(false, TAG, false);
            restoreCallingIdentity(token);
        }

        @Override
        public void reboot() throws RemoteException {
            long token = clearCallingIdentity();
            mPowerManager.reboot(TAG);
            restoreCallingIdentity(token);
        }

        @Override
        public void sleep() throws RemoteException {
            long token = clearCallingIdentity();
            mPowerManager.goToSleep(SystemClock.uptimeMillis());
            restoreCallingIdentity(token);
        }
    };
}

