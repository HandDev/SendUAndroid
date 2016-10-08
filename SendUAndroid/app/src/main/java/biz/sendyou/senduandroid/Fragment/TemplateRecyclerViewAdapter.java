package biz.sendyou.senduandroid.Fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import biz.sendyou.senduandroid.ContextManager;
import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.URLManager;
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

    private FragmentManager fragmentManager;

    public TemplateRecyclerViewAdapter(FragmentManager fragmentManager, List<CardTemplate> items, ImageLoader imageLoader, SelectTemplateFragment.OnListFragmentInteractionListener listener) {
        this.imageLoader = imageLoader;
        this.mValues = items;

        Log.i(LOGTAG, "mValues Length : " + mValues.size());
        mListener = listener;

        this.fragmentManager = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_template, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.i(LOGTAG, "onBindViewHolder called");
        holder.mItem = mValues.get(position);

        imageLoader.displayImage(URLManager.s3URL + mValues.get(position).getUrl(), holder.mImageView);
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

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOGTAG, "ImageClicked" + mValues.get(position).getUrl());

                CardSelectDialogFragment cardSelectDialogFragment = CardSelectDialogFragment.newInstance(mValues.get(position).getUrl());
                cardSelectDialogFragment.show(fragmentManager, "");
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

            /*
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            */
        }


    }
}
