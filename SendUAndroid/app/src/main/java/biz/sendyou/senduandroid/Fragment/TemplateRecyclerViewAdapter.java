package biz.sendyou.senduandroid.Fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.datatype.CardTemplate;
import biz.sendyou.senduandroid.thread.BitmapFromURLThread;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link CardTemplate} and makes a call to the
 * specified {@link SelectTemplateFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TemplateRecyclerViewAdapter extends RecyclerView.Adapter<TemplateRecyclerViewAdapter.ViewHolder> {

    private final ImageLoader imageLoader;
    private final List<CardTemplate> mValues;
    private final SelectTemplateFragment.OnListFragmentInteractionListener mListener;
    private final String LOGTAG = "TemplateRecylcerAdapter";

    public TemplateRecyclerViewAdapter(List<CardTemplate> items, ImageLoader imageLoader, SelectTemplateFragment.OnListFragmentInteractionListener listener) {
        this.imageLoader = imageLoader;
        this.mValues = items;

        Log.i(LOGTAG, "mValues Length : " + mValues.size());
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_template, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.i(LOGTAG, "onBindViewHolder called");
        holder.mItem = mValues.get(position);

        /*
        Bitmap bitmap = null;
        BitmapFromURLThread bitmapFromURLThread = new BitmapFromURLThread("https://s3.ap-northeast-2.amazonaws.com/cardbackground/" + mValues.get(position).getUrl());
        bitmapFromURLThread.start();

        try {
            bitmapFromURLThread.join();

            bitmap = bitmapFromURLThread.getBitmap();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        imageLoader.displayImage("https://s3.ap-northeast-2.amazonaws.com/cardbackground/" + mValues.get(position).getUrl(), holder.mImageView);
    //    holder.mImageView.setImageDrawable(new BitmapDrawable(bitmap));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
       // public final TextView mIdView;
       // public final TextView mContentView;
        public final ImageView mImageView;
        public CardTemplate mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
           // mIdView = (TextView) view.findViewById(R.id.id);
           // mContentView = (TextView) view.findViewById(R.id.content);

            mImageView = (ImageView)view.findViewById(R.id.templateimage);
        }

        //TODO ReWrite toString method
        /*
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
        */
    }
}
