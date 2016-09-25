package biz.sendyou.senduandroid.Fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.drivemode.android.typeface.TypefaceHelper;

import java.util.List;

import biz.sendyou.senduandroid.Fragment.item.SendCheckItem;
import biz.sendyou.senduandroid.R;

/**
 * Created by Chan_Woo_Kim on 2016-07-31.
 */
public class SendCheckRecyclerViewAdapter extends RecyclerView.Adapter<SendCheckRecyclerViewAdapter.ViewHolder>{
    private final Context mContext;
    private final List<SendCheckItem> mItems;

    public SendCheckRecyclerViewAdapter(List<SendCheckItem> items, Context context) {
        mContext = context;
        mItems = items;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.fragment_send_check, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.mReceiver_bold.setTypeface(holder.mTypeface_bold);
//        holder.mReceiver_regular.setTypeface(holder.mTypeface_regular);
//        holder.mAddress.setTypeface(holder.mTypeface_regular);
//        holder.mDate.setTypeface(holder.mTypeface_regular);
//        holder.mDate_num.setTypeface(holder.mTypeface_regular);
//        holder.mAddress.setTypeface(holder.mTypeface_regular);
//        holder.mDueDate.setTypeface(holder.mTypeface_regular);
//        holder.mDueDate_num.setTypeface(holder.mTypeface_regular);

        /*holder.mAddress.setText(mItems.get(position).getAddress());
        holder.mReceiver_bold.setText(mItems.get(position).getReceiver());
        holder.mDate_num.setText(mItems.get(position).getDate());
        holder.mDueDate_num.setText(mItems.get(position).getDueDate());
        holder.mStatusImg.setImageResource(mItems.get(position).getStatus_img());
        TypefaceHelper.getInstance().setTypeface(holder.mReceiver_bold,"NotoSansCJKkr-Bold.otf");
        TypefaceHelper.getInstance().setTypeface(holder.mReceiver_regular,"NotoSansCJKkr-Regular.otf");
        TypefaceHelper.getInstance().setTypeface(holder.mAddress,"NotoSansCJKkr-Regular.otf");
        TypefaceHelper.getInstance().setTypeface(holder.mDate,"NotoSansCJKkr-Regular.otf");
        TypefaceHelper.getInstance().setTypeface(holder.mDate_num,"NotoSansCJKkr-Regular.otf");
        TypefaceHelper.getInstance().setTypeface(holder.mDueDate,"NotoSansCJKkr-Regular.otf");
        TypefaceHelper.getInstance().setTypeface(holder.mDueDate_num,"NotoSansCJKkr-Regular.otf");*/
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView mReceiver_bold, mReceiver_regular;
        private final TextView mDate, mDate_num;
        private final TextView mDueDate, mDueDate_num;
        private final TextView mAddress;
        private final ImageView mStatusImg;
//        Typeface mTypeface_regular;
//        Typeface mTypeface_bold;


        public ViewHolder(View item) {
            super(item);
            mAddress = (TextView) item.findViewById(R.id.sendcheck_address);
            mReceiver_bold = (TextView) item.findViewById(R.id.sendcheck_receiver_bold);
            mReceiver_regular = (TextView) item.findViewById(R.id.sendcheck_receiver_regular);
            mDate = (TextView) item.findViewById(R.id.sendcheck_date);
            mDate_num = (TextView) item.findViewById(R.id.sendcheck_date_num);
            mDueDate = (TextView) item.findViewById(R.id.sendcheck_due_date);
            mDueDate_num = (TextView) item.findViewById(R.id.sendcheck_due_date_num);
            mStatusImg = (ImageView) item.findViewById(R.id.sendcheck_status_img);
        }
    }

}
