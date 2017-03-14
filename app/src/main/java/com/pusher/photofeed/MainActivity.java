package com.pusher.photofeed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionStateChange;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Pusher pusher = new Pusher("50ed18dd967b455393ed");
    private PhotoAdapter adapter;
    private RecyclerView.LayoutManager lManager;
    private static final String CHANNEL_NAME = "pics";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the RecyclerView
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler_view);

        // Use LinearLayout as the layout manager
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        // Set the custom adapter
        List<Photo> photoList = new ArrayList<>();
        adapter = new PhotoAdapter(this, photoList);
        recycler.setAdapter(adapter);

        // Subscribe to a channel
        Channel channel = pusher.subscribe(CHANNEL_NAME);

        // Action to be executed when an event is received
        SubscriptionEventListener eventListener = new SubscriptionEventListener() {
            @Override
            public void onEvent(String channel, final String event, final String data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Received event with data: " + data);
                        Gson gson = new Gson();
                        Photo photo = gson.fromJson(data, Photo.class);
                        adapter.addPhoto(photo);
                        ((LinearLayoutManager)lManager).scrollToPositionWithOffset(0, 0);
                    }

                });
            }
        };

        // Bind to listen for events sent to the channel
        channel.bind("new-listing", eventListener);

        // connect to the Pusher API
        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                System.out.println("State changed to " + change.getCurrentState() +
                        " from " + change.getPreviousState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                System.out.println("There was a problem connecting!");
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pusher.disconnect();
    }
}
