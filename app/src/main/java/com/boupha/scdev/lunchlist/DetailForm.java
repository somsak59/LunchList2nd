package com.boupha.scdev.lunchlist;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.net.URL;

/**
 * Created by scdev on 17/4/2560.
 */

public class DetailForm extends AppCompatActivity {
    EditText name = null, address = null, notes = null, url = null;
    RadioGroup types = null;
    RestaurantHelper helper = null;
    String restaurantId = null;
    Button button = null;
    Dialog dialog;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.detail_form);

        helper = new RestaurantHelper(this);

        name = (EditText)findViewById(R.id.name);
        address = (EditText)findViewById(R.id.addr);
        types = (RadioGroup)findViewById(R.id.types);
        notes = (EditText)findViewById(R.id.notes);
        url = (EditText)findViewById(R.id.web);

        button  = (Button)findViewById(R.id.save);
        button.setOnClickListener(onSave);

        restaurantId = getIntent().getStringExtra(LunchList.ID_EXTRA);
        if (restaurantId != null){
            load();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);

        state.putString("name", name.getText().toString());
        state.putString("address", address.getText().toString());
        state.putString("notes", notes.getText().toString());
        state.putString("url", url.getText().toString());
        state.putInt("type", types.getCheckedRadioButtonId());
    }

    @Override
    public void onRestoreInstanceState(Bundle state){
        super.onRestoreInstanceState(state);
        name.setText(state.getString("name"));
        address.setText(state.getString("address"));
        notes.setText(state.getString("notes"));
        url.setText(state.getString("url"));
        types.check(state.getInt("type"));
    }

    private View.OnClickListener onSave = new View.OnClickListener(){
        public void onClick(View v){
            String type = null;
            switch (types.getCheckedRadioButtonId()){
                case R.id.sit_down:
                    type = "sit_down";
                    break;
                case R.id.take_out:
                    type = "take_out";
                    break;
                case R.id.delivery:
                    type = "delivery";
                    break;
            }

            if (restaurantId == null){
                helper.insert(name.getText().toString(),
                        address.getText().toString(),type,
                        notes.getText().toString(),url.getText().toString());
            }else {
                helper.update(restaurantId, name.getText().toString(),
                        address.getText().toString(),type,
                        notes.getText().toString(), url.getText().toString());
            }
            finish();
        }
    };

    @Override
    public void onDestroy(){
        super.onDestroy();
        helper.close();
    }

    private void load(){
        Cursor c = helper.getById(restaurantId);

        c.moveToFirst();
        name.setText(helper.getName(c));
        address.setText(helper.getAddress(c));
        notes.setText(helper.getNotes(c));
        url.setText(helper.getUrl(c));

        if (helper.getType(c).equals("sit_down")){
            types.check(R.id.sit_down);
        }else if (helper.getType(c).equals("take_out")){
            types.check(R.id.take_out);
        }else {
            types.check(R.id.delivery);
        }
        c.close();
    }
    public boolean onCreateOptionsMenu(Menu menu){
        new MenuInflater(this).inflate(R.menu.rs_option, menu);
        return (super.onCreateOptionsMenu(menu));
    }
    public boolean onOptionsItemSelected(MenuItem item){

        String urls = null;
        if (item.getItemId() == R.id.visit){
            Cursor c = helper.getById(restaurantId);
            c.moveToFirst();
            urls = helper.getUrl(c);
            if (!urls.startsWith("http://") && (!urls.startsWith("https://"))){
                String a = "http://"+urls;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(a)));
            }else {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urls)));
            }
            c.close();
        }else {
            dialog  = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(Dia(this,"Are you sure to delete ?"));
            dialog.show();
        }
        return (super.onOptionsItemSelected(item));
    }
    private View Dia(Context context, String message){
        final View row = LayoutInflater.from(context).inflate(R.layout.dialogshow, null);
        TextView message1 = (TextView)row.findViewById(R.id.message);
        message1.setText(message);

        Button ok = (Button)row.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c = helper.deleteById(restaurantId);
                c.moveToFirst();
                c.close();
                finish();
            }
        });
        Button cn = (Button)row.findViewById(R.id.cancel);
        cn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        return row;

    }

}
