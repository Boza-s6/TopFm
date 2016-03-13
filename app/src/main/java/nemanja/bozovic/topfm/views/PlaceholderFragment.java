package nemanja.bozovic.topfm.views;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import nemanja.bozovic.topfm.R;
import nemanja.bozovic.topfm.services.StreamingService;
import nemanja.bozovic.topfm.utils.Utils;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements StreamingService.Listener, View.OnClickListener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private String TAG = "PlaceholderFragment";
    private boolean mIsPlaying = false;
    private boolean mIsbinded = false;

    private StreamingService mService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service instanceof StreamingService.StreamingBinder) {
                mService = ((StreamingService.StreamingBinder) service).getService();
                mService.setListener(PlaceholderFragment.this);
                mIsbinded = true;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ");
        }
    };

    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        fragment.TAG += " " + sectionNumber;
        Bundle args = new Bundle(1);
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int arg = getArguments().getInt(ARG_SECTION_NUMBER);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, arg));

        Button play = (Button) rootView.findViewById(R.id.play_button);
        play.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Utils.isMyServiceRunning(getActivity(), StreamingService.class)) {
            bindToService();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        unbindFromService();
    }

    @Override
    public void onStopped() {
        Log.d(TAG, "onStopped: ");
        mIsPlaying = false;
    }

    @Override
    public void onStarted() {
        Log.d(TAG, "onStarted: ");
        mIsPlaying = true;
    }

    @Override
    public void onChange() {
        Log.d(TAG, "onChange: ");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_button:
                if (!mIsPlaying) {
                    StreamingService.startService(getActivity());
                    bindToService();
                } else {
                    unbindFromService();
                    StreamingService.stopService(getActivity());
                }
                break;
        }
    }

    private void bindToService() {
        getActivity().bindService(StreamingService.getServiceIntent(getActivity()),
                mServiceConnection, Context.BIND_ABOVE_CLIENT);
    }

    private void unbindFromService() {
        if (mService != null) {
            mService.setListener(null);
            mService = null;
        }
        if (mIsbinded) {
            getActivity().unbindService(mServiceConnection);
        }
    }
}
