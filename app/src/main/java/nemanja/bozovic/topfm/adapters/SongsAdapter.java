package nemanja.bozovic.topfm.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nemanja.bozovic.topfm.R;
import nemanja.bozovic.topfm.models.Song;


public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongHolder> {
    private LayoutInflater mLayoutInflater;
    private List<Song> mData;

    public SongsAdapter(@NonNull Context context) {
        super();
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SongHolder(mLayoutInflater.inflate(R.layout.song_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SongHolder holder, int position) {
        holder.mAuthor.setText(mData.get(position).getAuthor());
        holder.mName.setText(mData.get(position).getSongName());
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    public void setSongs(List<Song> songs) {
        mData = songs;
        notifyDataSetChanged();
    }

    public List<Song> getData() {
        return mData;
    }

    public static class SongHolder extends RecyclerView.ViewHolder {
        /*@Bind(R.id.author)*/ TextView mAuthor;
        /*@Bind(R.id.name) */ TextView mName;

        public SongHolder(View itemView) {
            super(itemView);
            mAuthor = (TextView) itemView.findViewById(R.id.author);
            mName = (TextView) itemView.findViewById(R.id.name);
//            ButterKnife.bind(itemView);
        }
    }
}
