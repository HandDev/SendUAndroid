package biz.sendyou.senduandroid.Fragment;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.datatype.Address;

import java.util.List;

public class AddressBookRecyclerViewAdapter extends RecyclerView.Adapter<AddressBookRecyclerViewAdapter.ViewHolder> {

    private final List<Address> mValues;
    private final AddressBookFragment.OnListFragmentInteractionListener mListener;

    private String LOGTAG = "AddressBookRecyclerAdapter";

    public AddressBookRecyclerViewAdapter(List<Address> items, AddressBookFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.i(LOGTAG, "onBindViewHolder");
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(holder.mItem.getName());
        Log.i(LOGTAG, "name : "+ holder.mItem.getName());
        holder.mAddressView.setText(holder.mItem.getAddress());
        Log.i(LOGTAG, "addresss : "+ holder.mItem.getAddress());
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
        public final TextView mNameView;
        public final TextView mAddressView;
        public Address mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.address_list_name);
            mAddressView = (TextView) view.findViewById(R.id.address_list_address);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mAddressView.getText() + "'";
        }
    }

    public void refreshList() {
        this.notifyDataSetChanged();

        Log.i(LOGTAG, "refreshList");
    }

}
