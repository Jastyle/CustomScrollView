package com.scroller.jastyle.customscrollview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView test1_lv, test2_lv, test3_lv;
    private List<String> datas = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();

    }

    private void initView() {
        test1_lv = (ListView) findViewById(R.id.test1_lv);
        test2_lv = (ListView) findViewById(R.id.test2_lv);
        test3_lv = (ListView) findViewById(R.id.test3_lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.name, datas);
        test1_lv.setAdapter(adapter);
        test2_lv.setAdapter(adapter);
        test3_lv.setAdapter(adapter);
        test1_lv.setOnItemClickListener( new onItemclick());
        test2_lv.setOnItemClickListener( new onItemclick());
        test3_lv.setOnItemClickListener( new onItemclick());
    }
    private void initData() {
        for (int i = 0; i < 50; i++) {
            datas.add("name "+i);
        }
    }

    class onItemclick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(MainActivity.this, "name "+datas.get(position), Toast.LENGTH_SHORT).show();
        }
    }
}
