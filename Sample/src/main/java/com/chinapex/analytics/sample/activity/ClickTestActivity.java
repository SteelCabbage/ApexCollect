package com.chinapex.analytics.sample.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.chinapex.analytics.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Seven
 * @date : 2018/11/28
 */
public class ClickTestActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView mListView;
    private GridView mGridView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_test);

        initView();
        initData();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.start:
                Toast.makeText(this, "开始游戏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.over:
                Toast.makeText(this, "结束游戏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting1:
                Toast.makeText(this, "声音設置", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting2:
                Toast.makeText(this, "背景設置", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);

    }


    private void initData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("" + i);
        }

        mListView.setAdapter(new ArrayAdapter<>(this, R.layout.list_view_item, R.id.list_view_item_text, list));
        mGridView.setAdapter(new ArrayAdapter<>(this, R.layout.list_view_item, R.id.list_view_item_text, list));
    }

    private void initView() {
        mListView = findViewById(R.id.list_view);
        mGridView = findViewById(R.id.grid_view);
        ImageButton imageButton = findViewById(R.id.image_button);

        imageButton.setOnClickListener(this);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ClickTestActivity.this, "mGridView item is clicked !!!  position: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ClickTestActivity.this, "mListView item is clicked !!!  position: " + position, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_button:
                Toast.makeText(this, "onClick ImageButton is clicked !!!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public void xmlOnClick(View view) {
        switch (view.getId()) {
            case R.id.clickable_text:
                Toast.makeText(this, "xmlOnClick Text is clicked !!!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.clickable_image_view:
                Toast.makeText(this, "xmlOnClick ImageView is clicked !!!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

}
