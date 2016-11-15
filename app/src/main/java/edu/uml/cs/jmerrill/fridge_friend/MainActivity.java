package edu.uml.cs.jmerrill.fridge_friend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView obj;
    DBHelper proddb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        proddb = new DBHelper(this);
        //ArrayList array_list = proddb.getAllProducts();
/*
        if(proddb.insertContact(name.getText().toString(), phone.getText().toString(),
                email.getText().toString(), street.getText().toString(),
                place.getText().toString())){
            Toast.makeText(getApplicationContext(), "done",
                    Toast.LENGTH_SHORT).show();
*/
    }

}
