package biz.sendyou.senduandroid.Fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        holder.mAddress.setText(mItems.get(position).getAddress());
        holder.mReceiver.setText(mItems.get(position).getReceiver());
        holder.mDate.setText(mItems.get(position).getDate());
        holder.mDueDate.setText(mItems.get(position).getDueDate());
        holder.mStatusImg.setImageResource(mItems.get(position).getStatus_img());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView mReceiver;
        private final TextView mDate;
        private final TextView mDueDate;
        private final TextView mAddress;
        private final ImageView mStatusImg;

        public ViewHolder(View item) {
            super(item);
            mAddress = (TextView) item.findViewById(R.id.sendcheck_address);
            mReceiver = (TextView) item.findViewById(R.id.sendcheck_receiver);
            mDate = (TextView) item.findViewById(R.id.sendcheck_date);
            mDueDate = (TextView) item.findViewById(R.id.sendcheck_due_date);
            mStatusImg = (ImageView) item.findViewById(R.id.sendcheck_status_img);
        }
    }
}
