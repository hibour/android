package com.dsquare.hibour.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.ChatingAdapter;

import java.util.ArrayList;
import java.util.List;


public class Chat extends AppCompatActivity implements View.OnClickListener {
    private ImageView backIcon;
    private RecyclerView chatRecycler;
    private List<String[]> chatList = new ArrayList<>();
    private ChatingAdapter ChatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chating);
        initializeViews();
        initializeEventListeners();
    }

    private void initializeViews() {

        backIcon = (ImageView)findViewById(R.id.chat_back_icon);
        chatRecycler = (RecyclerView)findViewById(R.id.chating_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatRecycler.setLayoutManager(layoutManager);
        chatRecycler.setHasFixedSize(true);
        for (int i = 0; i < 10; i++) {
            String[] data = new String[4];
            data[0] = "Hai machha how r u?";
            data[1] = "2 Jan 2015";
            data[2] = "Fine rey How About u?";
            data[3] = "3 Jan 2015";
            chatList.add(data);
        }
        ChatAdapter = new ChatingAdapter(this, chatList);
        chatRecycler.setAdapter(ChatAdapter);

    }
    private void initializeEventListeners(){
        backIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.chat_back_icon:
                this.finish();
                break;
        }
    }
}
