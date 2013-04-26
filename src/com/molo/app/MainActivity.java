/**
 *
 * Copyright 2012 Molo, Inc. All rights reserved.
 * P2PActivity.java
 *
 */
package com.molo.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 *@author kane (kanegong@molo.cn)
 *@date 2012-5-24
 */

public class MainActivity extends Activity implements OnClickListener
{
    Receiver receiver;
    private String name = ""; 
    
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p2p);
        name = getIntent().getStringExtra("name");
        IntentFilter filter = new IntentFilter("p2p");
        receiver = new Receiver();
        this.registerReceiver(receiver, filter);
        
        Button button1 =(Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        Button button2 =(Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
    }

    class Receiver extends BroadcastReceiver
    {

        public Receiver()
        {
        }
        
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String msg = intent.getStringExtra("msg");
            System.out.println("Get msg in Main Activity: " + msg);
            final AlertDialog ad = new AlertDialog.Builder(MainActivity.this).setMessage(msg).show();
            android.os.Handler hander = new android.os.Handler();
            // 设定定时器并在设定时间后使对话框关闭
            hander.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    ad.dismiss();
                }
            }, 2 * 1000);
        }
    }
    
    @Override
    public void onBackPressed()
    {
        this.finish();
        super.onBackPressed();
    }
    
    @Override
    protected void onPause()
    {
        this.unregisterReceiver(receiver);
        receiver = null;
        super.onPause();
    }
    
    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.button1)
        {
            EditText text1 = (EditText) findViewById(R.id.editText1);
            final String remote_name = text1.getText().toString();
            if (remote_name != null && remote_name.length() > 0 && remote_name.indexOf("@") != -1)
            {
                new Thread(new Runnable()
                {
                    public void run()
                    {
                        System.out.println("createP2P");
                        NativeMethod.createChannel(MainActivity.this.getName(), remote_name);
                    }
                }).start();
            }
            else
            {
                Toast.makeText(MainActivity.this, "Please input right user's ID.", Toast.LENGTH_SHORT).show();
            }
        }
        else if (v.getId() == R.id.button2)
        {
            EditText text2 = (EditText) findViewById(R.id.editText2);
            final String data = text2.getText().toString();
            if (data != null && data.length() > 0)
            {
                new Thread(new Runnable()
                {
                    public void run()
                    {
                        System.out.println("sendData");
                        NativeMethod.sendData("", data);
                    }
                }).start();
            }
            else
            {
                Toast.makeText(MainActivity.this, "Please input something to chat.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    };
    
}
