package biz.sendyou.senduandroid.Fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import biz.sendyou.senduandroid.R;

/**
 * Created by Chan_Woo_Kim on 2016-08-11.
 */
public class SettingPreferenceFragment extends PreferenceFragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.xml_preference);
    }
}
