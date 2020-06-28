package com.example.booklist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BookListActivity extends AppCompatActivity {
    TextView textView;
    Button readButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        textView = (TextView)  findViewById(R.id.descView);
        textView.setText(getIntent().getStringExtra("desc"));
        textView.setMovementMethod(new ScrollingMovementMethod());

        readButton = (Button) findViewById(R.id.button);
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getIntent().getStringExtra("readerLink")));
                startActivity(intent);
            }
        });
    }


}
