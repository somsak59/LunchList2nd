package com.boupha.scdev.lunchlist;


import android.app.Dialog;
import android.app.ListActivity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

public class LunchList extends ListActivity {
    RestaurantHelper helper;
    EditText name = null;
    EditText address =null;
    RadioGroup types = null;
    EditText notes = null;
    EditText url = null;
    Restaurant current = null;
    Cursor model = null;
    RestaurantAdapter adapter = null;
    public final static String ID_EXTRA = "apt.tutorial._ID";
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
         prefs = PreferenceManager.getDefaultSharedPreferences(this);

        helper = new RestaurantHelper(this);

         name = (EditText)findViewById(R.id.name);
         address = (EditText)findViewById(R.id.addr);
         types = (RadioGroup)findViewById(R.id.types);
        notes = (EditText)findViewById(R.id.notes);
        url = (EditText)findViewById(R.id.web);

        initList();
        prefs.registerOnSharedPreferenceChangeListener(prefListener);

    }

    private void initList(){
        if (model != null){
            stopManagingCursor(model);
            model.close();
        }
        model = helper.getAll(prefs.getString("sort_order", "name"));
        startManagingCursor(model);
        adapter = new RestaurantAdapter(model);
        setListAdapter(adapter);

    }

    private SharedPreferences.OnSharedPreferenceChangeListener prefListener=
            new SharedPreferences.OnSharedPreferenceChangeListener(){
                public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key){
                    if (key.equals("sort_order")){
                        initList();
                    }
                }
            };

    @Override
    public void onDestroy(){
        super.onDestroy();
        helper.close();
    }

    private View.OnClickListener onSave = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            String type = null;
            switch(types.getCheckedRadioButtonId()) {
                case R.id.sit_down:
                    type="sit_down";
                    break;
                case R.id.take_out:
                    type="take_out";
                    break;
                case R.id.delivery:
                    type="delivery";
                    break;
            }
            helper.insert(name.getText().toString(),
                    address.getText().toString(), type, notes.getText().toString(), url.getText().toString());
            model.requery();
        }
    };
    //==========Custom Adapter ===========================
    class RestaurantAdapter extends CursorAdapter{
        RestaurantAdapter(Cursor c){
            super(LunchList.this, c);
        }
        @Override
        public void bindView(View row, Context ctxt, Cursor c){
            RestaurantHolder holder = (RestaurantHolder)row.getTag();
            holder.populateForm(c, helper);
        }

        @Override
        public View newView(Context ctxt, Cursor c, ViewGroup parent){
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.row,parent,false);
            RestaurantHolder holder = new RestaurantHolder(row);
            row.setTag(holder);
            return row;
        }

    }

    static class RestaurantHolder{
        private TextView name = null, address = null;
        private ImageView icon = null;
        private View rows = null;

        RestaurantHolder(View row){
            this.rows = row;

            name = (TextView)row.findViewById(R.id.title);
            address = (TextView)row.findViewById(R.id.address);
            icon = (ImageView)row.findViewById(R.id.icon);
        }
        void populateForm(Cursor c,RestaurantHelper helper){
            name.setText(helper.getName(c));
            address.setText(helper.getAddress(c));
            address.setTextColor(Color.parseColor("#EC9640"));

            if (helper.getType(c).equals("sit_down")){
                icon.setImageResource(R.drawable.s);
                name.setTextColor(Color.parseColor("#F60404"));
            }else if (helper.getType(c).equals("take_out")){
                icon.setImageResource(R.drawable.t);
                name.setTextColor(Color.parseColor("#1100FF"));
            }else {
                icon.setImageResource(R.drawable.d);
                name.setTextColor(Color.parseColor("#22B922"));
            }
        }
    }
    public void onListItemClick(ListView list, View view, int position, long id){
        Intent i = new Intent(LunchList.this, DetailForm.class);
        i.putExtra(ID_EXTRA, String.valueOf(id));
        startActivity(i);
    }



    public boolean onCreateOptionsMenu(Menu menu){
        new MenuInflater(this).inflate(R.menu.option, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.add){
            startActivity(new Intent(LunchList.this, DetailForm.class));
            return true;
        }else if (item.getItemId() == R.id.prefs){
            startActivity(new Intent(this, EditPreferences.class));
            return true;
        }
        return (super.onOptionsItemSelected(item));
    }


}
