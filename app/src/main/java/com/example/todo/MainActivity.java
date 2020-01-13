package com.example.todo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    public static final String keyItemText ="item_text";
    public static final String keyItemPosition = "item_position";
    public static final int Edit_Text_Code = 20;

    List<String> items;

    Button btnADD;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnADD = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                //Delete the item from the model
                items.remove(position);
                //Notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(),"It was removed!",Toast.LENGTH_SHORT).show();
                saveItems();

            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                //create a new activity
                Intent i = new Intent(MainActivity.this,EditActivity.class);

                //pass the data being edited
                i.putExtra(keyItemText,items.get(position));
                i.putExtra(keyItemPosition,position);

                //display the activity
                startActivityForResult(i,Edit_Text_Code);
            }
        };
        //final ItemsAdapter itemsAdapter = new ItemsAdapter(items);
        itemsAdapter = new ItemsAdapter(items,onLongClickListener,onClickListener);

        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = etItem.getText().toString();
                //ADd item to the model
                items.add(todoItem);
                //notify adapter that an item is inserted
                itemsAdapter.notifyItemInserted(items.size()-1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(),"Get to it!",Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });

       // btnADD
    }

    //handle result of edit ectivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK && requestCode==Edit_Text_Code){
            //retrieve the updated text value
            String itemText = data.getStringExtra(keyItemText);
            //extract the original position of the edited item from the position key
            int position = data.getExtras().getInt(keyItemPosition);

            //update the model at the right position with the new item text
            items.set(position,itemText);
            //notify the adapter
            itemsAdapter.notifyItemChanged(position);
            //persist changes
            saveItems();
            Toast.makeText(getApplicationContext(),"Updated sucessfully",Toast.LENGTH_SHORT).show();
        }
        else{
            Log.w("MainActivity","Unknown call to onActivityResult");
        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    //load functions by reading every item from data file
    private void loadItems() {
        try{
            items = new ArrayList<>(org.apache.commons.io.FileUtils.readLines(getDataFile(),Charset.defaultCharset()));
        }catch (IOException e){
            Log.e("MainActivity","Error reading items",e);
            items = new ArrayList<>();
        }
    }

    //save items by writing items to file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(),items);
        }catch(IOException e)
        {
            Log.e("MainActivity","Error writing items",e);
        }

    }



}
