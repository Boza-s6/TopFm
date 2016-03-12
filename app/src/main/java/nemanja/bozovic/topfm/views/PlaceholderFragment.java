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

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements StreamingService.Listener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private String TAG = "PlaceholderFragment";
    private Button mPlay;
    private boolean mIsPlaying = false;

    private StreamingService mService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service instanceof StreamingService.StreamingBinder) {
                mService = ((StreamingService.StreamingBinder) service).getService();
                mService.setListener(PlaceholderFragment.this);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ");
        }
    };

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (StreamingService.STARTED.equals(intent.getAction())) {
                mIsPlaying = true;
                mPlay.setText("stop");
            } else if (StreamingService.STOPPED.equals(intent.getAction())) {
                mIsPlaying = false;
                mPlay.setText("Start");
            }
        }
    };

    public PlaceholderFragment() {
        Log.d(TAG, "PlaceholderFragment: ");
    }

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
        Log.d(TAG, "onCreateView: arg = " + arg);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, arg));
        mPlay = (Button) rootView.findViewById(R.id.play_button);
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsPlaying) {
                    Intent service = new Intent(getActivity(), StreamingService.class);
                    service.setAction(StreamingService.ACTION_START_PLAYBACK);
                    getActivity().startService(service);
                    getActivity().bindService(new Intent(getActivity(), StreamingService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
                } else {
                    Intent service = new Intent(getActivity(), StreamingService.class);
                    service.setAction(StreamingService.ACTION_STOP_PLAYBACK);
                    getActivity().stopService(service);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(StreamingService.STARTED);
        filter.addAction(StreamingService.STOPPED);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onSomething() {

    }
}
