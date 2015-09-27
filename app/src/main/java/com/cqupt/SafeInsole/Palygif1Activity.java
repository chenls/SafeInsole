package com.cqupt.SafeInsole;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.cqupt.SafeInsole.R.color;

public class Palygif1Activity extends TabActivity {
    TabHost tabHost;

    private TextView main_tab_unread_tv;

    private RelativeLayout main_tab_addExam, main_tab_myExam, main_tab_message,
            main_tab_settings, main_tab_help;

    private LinearLayout main_layout_addExam, main_layout_myExam,
            main_layout_message, main_layout_settings, main_layout_help;

    private ImageView img_tab_addExam, img_tab_myExam, img_tab_message,
            img_tab_settings, img_tab_help;

    private TextView text_tab_addExam, text_tab_myExam, text_tab_message,
            text_tab_settings, text_tab_help;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initTab();
        init();
    }

    private void init() {

        // 点击底部
        main_tab_addExam = (RelativeLayout) findViewById(R.id.main_tab_addExam);

        main_tab_myExam = (RelativeLayout) findViewById(R.id.main_tab_myExam);

        main_tab_message = (RelativeLayout) findViewById(R.id.main_tab_message);

        main_tab_settings = (RelativeLayout) findViewById(R.id.main_tab_settings);

        main_tab_help = (RelativeLayout) findViewById(R.id.main_tab_help);

        // 底部图片跟换
        img_tab_addExam = (ImageView) findViewById(R.id.img_tab_addExam);

        img_tab_myExam = (ImageView) findViewById(R.id.img_tab_myExam);

        img_tab_message = (ImageView) findViewById(R.id.img_tab_message);

        img_tab_settings = (ImageView) findViewById(R.id.img_tab_settings);

        img_tab_help = (ImageView) findViewById(R.id.img_tab_help);

        // 底部文字
        text_tab_addExam = (TextView) findViewById(R.id.tab_addExam_text);

        text_tab_myExam = (TextView) findViewById(R.id.img_myExam_text);

        text_tab_message = (TextView) findViewById(R.id.img_message_text);

        text_tab_settings = (TextView) findViewById(R.id.img_settings_text);

        text_tab_help = (TextView) findViewById(R.id.img_help_text);

        main_layout_addExam = (LinearLayout) findViewById(R.id.main_layout_addExam);

        main_layout_myExam = (LinearLayout) findViewById(R.id.main_layout_myExam);

        main_layout_message = (LinearLayout) findViewById(R.id.main_layout_message);

        main_layout_settings = (LinearLayout) findViewById(R.id.main_layout_settings);

        main_layout_help = (LinearLayout) findViewById(R.id.main_layout_help);
        // 图片点亮
        img_tab_addExam.setImageResource(R.drawable.label_one_one);
        img_tab_myExam.setImageResource(R.drawable.label_2);
        img_tab_message.setImageResource(R.drawable.label_3);
        img_tab_settings.setImageResource(R.drawable.label4);
        img_tab_help.setImageResource(R.drawable.label5);
        // 背景点亮
        main_layout_addExam.setBackgroundResource(R.drawable.labelbg);
        main_layout_myExam.setBackgroundResource(0);
        main_layout_message.setBackgroundResource(0);
        main_layout_settings.setBackgroundResource(0);
        main_layout_help.setBackgroundResource(0);
        // 文字点亮
        text_tab_addExam.setTextColor(color.red);
        text_tab_myExam.setTextColor(color.blue);
        text_tab_message.setTextColor(color.blue);
        text_tab_settings.setTextColor(color.blue);
        text_tab_help.setTextColor(color.blue);
        // main_tab_addExam.setCompoundDrawablesWithIntrinsicBounds(null,
        // getResources().getDrawable(R.drawable.label_one_one), null, null);

        // main_tab_unread_tv.setVisibility(View.VISIBLE);

        // main_tab_unread_tv.setText("3");

        main_tab_addExam.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {

                tabHost.setCurrentTabByTag("first");

                // 图片点亮
                img_tab_addExam.setImageResource(R.drawable.label_one_one);
                img_tab_myExam.setImageResource(R.drawable.label_2);
                img_tab_message.setImageResource(R.drawable.label_3);
                img_tab_settings.setImageResource(R.drawable.label4);
                img_tab_help.setImageResource(R.drawable.label5);
                // 背景点亮
                main_layout_addExam.setBackgroundResource(R.drawable.labelbg);
                main_layout_myExam.setBackgroundResource(0);
                main_layout_message.setBackgroundResource(0);
                main_layout_settings.setBackgroundResource(0);
                main_layout_help.setBackgroundResource(0);
                // 文字点亮
                text_tab_addExam.setTextColor(color.red);
                text_tab_myExam.setTextColor(color.blue);
                text_tab_message.setTextColor(color.blue);
                text_tab_settings.setTextColor(color.blue);
                text_tab_help.setTextColor(color.blue);
            }

        });

        main_tab_myExam.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {

                tabHost.setCurrentTabByTag("second");

                // 图片点亮
                img_tab_addExam.setImageResource(R.drawable.label_1);
                img_tab_myExam.setImageResource(R.drawable.label_two_one);
                img_tab_message.setImageResource(R.drawable.label_3);
                img_tab_settings.setImageResource(R.drawable.label4);
                img_tab_help.setImageResource(R.drawable.label5);
                // 背景点亮
                main_layout_addExam.setBackgroundResource(0);
                main_layout_myExam.setBackgroundResource(R.drawable.labelbg);
                main_layout_message.setBackgroundResource(0);
                main_layout_settings.setBackgroundResource(0);
                main_layout_help.setBackgroundResource(0);
                // 文字点亮
                text_tab_addExam.setTextColor(color.blue);
                text_tab_myExam.setTextColor(color.red);
                text_tab_message.setTextColor(color.blue);
                text_tab_settings.setTextColor(color.blue);
                text_tab_help.setTextColor(color.blue);

            }

        });

        main_tab_message.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                // TODO Auto-generated method stub

                tabHost.setCurrentTabByTag("third");

                // 图片点亮
                img_tab_addExam.setImageResource(R.drawable.label_1);
                img_tab_myExam.setImageResource(R.drawable.label_2);
                img_tab_message.setImageResource(R.drawable.label_three_one);
                img_tab_settings.setImageResource(R.drawable.label4);
                img_tab_help.setImageResource(R.drawable.label5);
                // 背景点亮
                main_layout_addExam.setBackgroundResource(0);
                main_layout_myExam.setBackgroundResource(0);
                main_layout_message.setBackgroundResource(R.drawable.labelbg);
                main_layout_settings.setBackgroundResource(0);
                main_layout_help.setBackgroundResource(0);
                // 文字点亮
                text_tab_addExam.setTextColor(color.blue);
                text_tab_myExam.setTextColor(color.blue);
                text_tab_message.setTextColor(color.red);
                text_tab_settings.setTextColor(color.blue);
                text_tab_help.setTextColor(color.blue);

            }

        });

        main_tab_settings.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                // TODO Auto-generated method stub

                tabHost.setCurrentTabByTag("four");

                // 图片点亮
                img_tab_addExam.setImageResource(R.drawable.label_1);
                img_tab_myExam.setImageResource(R.drawable.label_2);
                img_tab_message.setImageResource(R.drawable.label_3);
                img_tab_settings.setImageResource(R.drawable.label_four_one);
                img_tab_help.setImageResource(R.drawable.label5);
                // 背景点亮
                main_layout_addExam.setBackgroundResource(0);
                main_layout_myExam.setBackgroundResource(0);
                main_layout_message.setBackgroundResource(0);
                main_layout_settings.setBackgroundResource(R.drawable.labelbg);
                main_layout_help.setBackgroundResource(0);
                // 文字点亮
                text_tab_addExam.setTextColor(color.blue);
                text_tab_myExam.setTextColor(color.blue);
                text_tab_message.setTextColor(color.blue);
                text_tab_settings.setTextColor(color.red);
                text_tab_help.setTextColor(color.blue);
            }

        });

        main_tab_help.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                // TODO Auto-generated method stub

                tabHost.setCurrentTabByTag("five");

                // 图片点亮
                img_tab_addExam.setImageResource(R.drawable.label_1);
                img_tab_myExam.setImageResource(R.drawable.label_2);
                img_tab_message.setImageResource(R.drawable.label_3);
                img_tab_settings.setImageResource(R.drawable.label4);
                img_tab_help.setImageResource(R.drawable.label_five_one);
                // 背景点亮
                main_layout_addExam.setBackgroundResource(0);
                main_layout_myExam.setBackgroundResource(0);
                main_layout_message.setBackgroundResource(0);
                main_layout_settings.setBackgroundResource(0);
                main_layout_help.setBackgroundResource(R.drawable.labelbg);
                // 文字点亮
                text_tab_addExam.setTextColor(color.blue);
                text_tab_myExam.setTextColor(color.blue);
                text_tab_message.setTextColor(color.blue);
                text_tab_settings.setTextColor(color.blue);
                text_tab_help.setTextColor(color.red);
            }

        });
    }

    private void initTab() {

        tabHost = getTabHost();

        tabHost.addTab(tabHost.newTabSpec("first").setIndicator("first")
                .setContent(

                        new Intent(this, MainActivity.class)));

        tabHost.addTab(tabHost.newTabSpec("second").setIndicator("second")
                .setContent(

                        new Intent(this, SafeActivity.class)));

        tabHost.addTab(tabHost.newTabSpec("third").setIndicator("third")
                .setContent(

                        new Intent(this, FallActivity.class)));

        tabHost.addTab(tabHost.newTabSpec("four").setIndicator("four")
                .setContent(

                        new Intent(this, SetActivity.class)));

        tabHost.addTab(tabHost.newTabSpec("five").setIndicator("five")
                .setContent(

                        new Intent(this, HelpActivity.class)));

    }

}