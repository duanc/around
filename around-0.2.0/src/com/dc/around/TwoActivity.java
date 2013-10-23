package com.dc.around;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.dc.around.Manage.Manage;
import com.dc.around.model.Category;
import com.dc.around.model.Information;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class TwoActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    TextView textView;
    ListView listView;
    ImageButton backButton;
    List<Information> informationList;
    List<Category> categoryList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twoepage);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String code = intent.getStringExtra("code");
        listView = (ListView) findViewById(R.id.secListView);
        textView = (TextView) findViewById(R.id.title);


        textView.setText(name);


        backButton = (ImageButton) findViewById(R.id.twoback);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                       TwoActivity.this.finish();
            }
        });


        Manage manage = new Manage();


        try {

            categoryList = manage.getCategoryList(TwoActivity.this, code);
            Log.e("categoryList", "categoryList:" + categoryList.size());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return categoryList.size();  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Object getItem(int position) {
                return categoryList.get(position);  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public long getItemId(int position) {
                return position;  //To change body of implemented methods use File | Settings | File Templates.
            }


            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                LayoutInflater layoutInflater = getLayoutInflater();
                convertView = layoutInflater.inflate(R.layout.categorylist, parent, false);
//                if (convertView == null) {
//                    LayoutInflater layoutInflater = getLayoutInflater();
//                    convertView = layoutInflater.inflate(R.layout.categorylist, parent, false);
//                }

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(TwoActivity.this, PoiActivity.class);
                        intent.putExtra("name", categoryList.get(position).getName());
                        intent.putExtra("code", categoryList.get(position).getCode());
                        startActivity(intent);

                    }
                });
                TextView nameTextView = (TextView) convertView.findViewById(R.id.textlist);
                Button nextButton = (Button) convertView.findViewById(R.id.btnlist);


                nameTextView.setText(categoryList.get(position).getName());
                if (!categoryList.get(position).isIschildren()) {

                    nextButton.setVisibility(View.INVISIBLE);
                } else {
                    nextButton.setId(position);//Integer.parseInt(categoryList.get(position).getCode()));//会报错
                    Log.e("nextButton position", "position  " + categoryList.get(position).getCode());
                    nextButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //页面跳转  下级菜单
                            Intent intent = new Intent(TwoActivity.this, TwoActivity.class);
                            intent.putExtra("name", categoryList.get(view.getId()).getName());
                            intent.putExtra("code", categoryList.get(view.getId()).getCode());
                            startActivity(intent);


                        }
                    });
                }


                return convertView;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };

        listView.setAdapter(baseAdapter);


    }

}

