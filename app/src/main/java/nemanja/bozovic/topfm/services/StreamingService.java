package nemanja.bozovic.topfm.services;

import android.app.Service;
import android.content.Context;
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
        void onStopped();
        void onStarted();
        void onChange();
        class Empty implements Listener {
            @Override public void onStopped() {}
            @Override public void onStarted() {}
            @Override public void onChange() {}
        }
    }

    private static final String TAG = "StreamingService";
    public static final String ACTION_START_PLAYBACK = TAG + ".action.start.playback";
    public static final String ACTION_STOP_PLAYBACK = TAG + ".action.stop.playback";

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
            getListener().onStopped();
        }
        return START_STICKY;

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mPlayer.start();
        mIsStarted = true;
        getListener().onStarted();
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
                    if (mIsStarted) {
                        getListener().onStarted();
                    }
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

    public static Intent getServiceIntent(Context context) {
        return new Intent(context, StreamingService.class);
    }

    public static void startService(Context context) {
        Intent intent = new Intent(context, StreamingService.class);
        intent.setAction(ACTION_START_PLAYBACK);
        context.startService(intent);
    }
    public static void stopService(Context context) {
        Intent intent = new Intent(context, StreamingService.class);
        context.stopService(intent);
    }
}
