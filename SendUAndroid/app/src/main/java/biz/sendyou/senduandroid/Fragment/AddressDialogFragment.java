package biz.sendyou.senduandroid.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.rey.material.widget.Button;

import biz.sendyou.senduandroid.Fragment.InteractInterface.AddressDialogInteract;
import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.datatype.Address;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by parkjaesung on 2016. 7. 31..
 */
public class AddressDialogFragment extends DialogFragment {

    private Realm realm;
    private RealmConfiguration realmConfig;
    private RealmResults<Address> addressRealmResults;

    //constructor
    public AddressDialogFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        realmConfig = new RealmConfiguration.Builder(getContext()).build();
        realm = Realm.getInstance(realmConfig);

        View view = inflater.inflate(R.layout.fragment_address_dialog, container);

        final AppCompatEditText nameEditText = (AppCompatEditText) view.findViewById(R.id.address_dialog_name_edittext);
        final AppCompatEditText addressEditText = (AppCompatEditText) view.findViewById(R.id.address_dialog_address_edittext);

        Button saveButton = (Button)view.findViewById(R.id.address_dialog_save_button);
        Button cancelButton = (Button)view.findViewById(R.id.address_dialog_cancel_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AddressDialogInteract)getFragmentManager().findFragmentById(R.id.mainFrameLayout)).addressSaveButtonClick(nameEditText.getText().toString(), addressEditText.getText().toString());
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return  view;
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

    }

    @Override
    public void onPause() {
        Fragment mFragment = getFragmentManager().findFragmentByTag("AddressDialogFragment");
        FragmentTransaction FragTsaction = getFragmentManager().beginTransaction();
        FragTsaction.remove(mFragment);
        super.onPause();
    }

}
