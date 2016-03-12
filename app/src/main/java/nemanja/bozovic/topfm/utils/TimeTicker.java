package nemanja.bozovic.topfm.utils;

import android.os.Handler;


public class TimeTicker {
    private final Handler mHandler;
    private final TimeChangedListener mListener;
    private final long mDuration;
    private final Runnable mRunnable = new Broadcast();

    public TimeTicker(TimeChangedListener listener, long delay) {
        mListener = listener;
        mDuration = delay;
        mHandler = new Handler();
    }

    public void start() {
        mHandler.postDelayed(mRunnable, mDuration);
    }

    public void stop() {
        mHandler.removeCallbacks(mRunnable);
    }

    public interface TimeChangedListener {
        void onTimeChanged();
    }

    private class Broadcast implements Runnable {
        @Override
        public void run() {
            mListener.onTimeChanged();
            mHandler.postDelayed(this, mDuration);
        }
    }
}
