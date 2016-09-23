package biz.sendyou.senduandroid.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import biz.sendyou.senduandroid.Fragment.InteractInterface.AddressDialogInteract;
import biz.sendyou.senduandroid.Fragment.dummy.DummyContent;
import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.datatype.Address;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class AddressBookFragment extends Fragment implements AddressDialogInteract {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private AddressBookRecyclerViewAdapter mViewAdapter;
    private AddressDialogFragment addressDialogFragment;

    private Realm realm;
    private RealmConfiguration realmConfig;
    private RealmResults<Address> addressRealmResults;
    private List<Address> items;
    private RecyclerView recyclerView;

    private String LOGTAG = "AddressBookFragment";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AddressBookFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static AddressBookFragment newInstance(int columnCount) {
        AddressBookFragment fragment = new AddressBookFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("AddressBookFragment", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_address_list, container, false);

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.address_list);
        items = new ArrayList<>();

        //Create Realm Configuration
        realmConfig = new RealmConfiguration.Builder(getContext()).build();
        realm = Realm.getInstance(realmConfig);

        addressRealmResults = realm.where(Address.class).findAll();

        for (Object addressRealmResult : addressRealmResults) {
            Log.i(LOGTAG, "addItems");
            items.add((Address)addressRealmResult);
        }

            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mViewAdapter = new AddressBookRecyclerViewAdapter(items, mListener);
            recyclerView.setAdapter(mViewAdapter);

            Log.i(LOGTAG, "setAdapter");

        final FloatingActionButton addFloatingActionButton = (FloatingActionButton)view.findViewById(R.id.address_fab);
        addFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                addressDialogFragment = new AddressDialogFragment();
                addressDialogFragment.show(fm, "Add Address");
            }
        });

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void addressSaveButtonClick(String name, String address) {
        addAddress(name, address);
        mViewAdapter.refreshList();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Address item);

    }

    public void addAddress(final String inputName, final String inputAddress){

        realm.executeTransaction(new Realm.Transaction(){

            @Override
            public void execute(Realm realm) {
                Address address = realm.createObject(Address.class);

                address.setAddress(inputAddress);
                address.setName(inputName);
            }
        });

        Log.i("AddressBookFragment", "saved name : " + inputName + "saved Address : " + inputAddress);

        resetAdapter();
    }

    private void resetAdapter(){

        Log.i(LOGTAG, "resetAdapter");
        items.clear();

        addressRealmResults = realm.where(Address.class).findAll();
        for (Object addressRealmResult : addressRealmResults) {
            Log.i(LOGTAG, "addItems");
            items.add((Address)addressRealmResult);
        }

        mViewAdapter = new AddressBookRecyclerViewAdapter(items, mListener);
        recyclerView.setAdapter(mViewAdapter);
    }

    @Override
    public void onPause() {
        Fragment mFragment = getFragmentManager().findFragmentByTag("AddressBookFragment");
        FragmentTransaction FragTsaction = getFragmentManager().beginTransaction();
        FragTsaction.remove(mFragment);
    }
}
