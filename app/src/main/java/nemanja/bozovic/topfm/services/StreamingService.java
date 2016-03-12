package nemanja.bozovic.topfm.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

import nemanja.bozovic.topfm.utils.TimeTicker;


public class StreamingService extends Service implements MediaPlayer.OnPreparedListener,
        TimeTicker.TimeChangedListener {
    public interface Listener {
        Listener EMPTY = new Empty();
        void onSomething();
        class Empty implements Listener {
            @Override public void onSomething() {}
        }
    }

    private static final String TAG = "StreamingService";
    public static final String ACTION_START_PLAYBACK = TAG + ".action.start.playback";
    public static final String ACTION_STOP_PLAYBACK = TAG + ".action.stop.playback";
    public static final String STARTED = TAG + ".started";
    public static final String STOPPED = TAG + ".stopped";
    private static final Intent sStoppedIntent = new Intent(STOPPED);
    private static final Intent sStartedIntent = new Intent(STARTED);

    private IBinder mStreamingServiceBinder = new StreamingBinder();
    private MediaPlayer mPlayer;
    private boolean mIsStarted = false;
    private Listener mListener;
    private Handler mHandler = new Handler();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mIsStarted) {
            mPlayer.stop();
        }
        mPlayer.release();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mStreamingServiceBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ACTION_START_PLAYBACK.equals(intent.getAction()) && !mIsStarted) {
            try {
                mPlayer.setDataSource("http://live192.topfm.rs:8000/");
                mPlayer.setOnPreparedListener(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.prepareAsync();
        } else if (ACTION_STOP_PLAYBACK.equals(intent.getAction()) && mIsStarted) {
            mPlayer.stop();
            mIsStarted = false;
            sendBroadcast(sStoppedIntent);
            getListener().onSomething();
        }
        return START_STICKY;

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mPlayer.start();
        mIsStarted = true;
        sendBroadcast(sStartedIntent);
        getListener().onSomething();
    }

    @Override
    public void onTimeChanged() {
        update();
    }

    public void setListener(Listener listener) {
        mListener = listener;
        if (mListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    getListener().onSomething();
                }
            });
        }
    }


    public class StreamingBinder extends Binder {
        public StreamingService getService() {
            return StreamingService.this;
        }
    }

    private Listener getListener() {
        return mListener != null ? mListener : Listener.EMPTY;
    }

    private void update() {
    }
}
