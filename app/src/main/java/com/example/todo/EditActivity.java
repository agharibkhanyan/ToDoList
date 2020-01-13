package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText etItem;
    Button btnSave;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etItem = findViewById(R.id.etItem);
        btnSave = findViewById(R.id.btnSave);

        //set title
        getSupportActionBar().setTitle("Edit item!");

        etItem.setText(getIntent().getStringExtra(MainActivity.keyItemText));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create and intent which will contain the results
                Intent intent = new Intent();

                //pass the data (results of editing)
                intent.putExtra(MainActivity.keyItemText,etItem.getText().toString());
                intent.putExtra(MainActivity.keyItemPosition,getIntent().getExtras().getInt(MainActivity.keyItemPosition));

                //set the result of intent
                setResult(RESULT_OK,intent);

                //finish activity and close
                finish();
            }
        });

    }
}
