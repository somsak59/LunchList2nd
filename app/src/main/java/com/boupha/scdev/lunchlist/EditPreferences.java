package com.boupha.scdev.lunchlist;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by scdev on 18/4/2560.
 */

public class EditPreferences extends PreferenceActivity {
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
