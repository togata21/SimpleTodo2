package com.example.togata.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //obtain reference to ListView from layout
        lvItems = (ListView) findViewById(R.id.lvItems);

        //initialize item list
        //items = new ArrayList<>();
        readItems();

        //initialize adapter using items list
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

        //wire adapter to view
        lvItems.setAdapter(itemsAdapter);

        //listen for long clicks to remove items
        setupListViewListener();

    }

    public void onAddItem(View v){
        //get text added in layout
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);

        //add text to arraylist of items
        items.add(etNewItem.getText().toString());

        //clear edit text secton
        etNewItem.setText("");

        //notification of finished addition
        Toast.makeText(getApplicationContext(), "Item added to the list", Toast.LENGTH_SHORT).show();
        writeItems();

    }

    private void setupListViewListener(){
        //after a long click on an item, the item will be deleted
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                String deletedWord = items.get(position);
                items.remove(position); //remove item that was long clicked
                itemsAdapter.notifyDataSetChanged(); //notify adapter that dataset changed
                //Toast.makeText(getApplicationContext(), (deletedWord+" is complete"), Toast.LENGTH_SHORT).show();
                Log.i("MainActivity", "Remvoed item + postition");
                return true;

            }
        });


    }

    //get stored (previous) actions
    private File getDataFile(){
        return new File(getFilesDir(), "todo.txt");
    }

    //read files into the system
    private void readItems() {
        try{
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading file", e);
            items = new ArrayList<>(); //loads empty list
        }

    }

    //write files to list view
    private void writeItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing to file", e);
        }
    }
}
