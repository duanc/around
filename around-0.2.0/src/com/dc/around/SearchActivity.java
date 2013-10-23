package com.dc.around;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-16
 * Time: 上午9:54
 * To change this template use File | Settings | File Templates.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.dc.around.Manage.Manage;
import com.dc.around.model.Information;
import com.dc.around.tools.Constant;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class SearchActivity extends Activity {
    List<Information> informationList;
    ImageButton backButton;
    TextView textView;
    ListView listView;
    String name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        listView = (ListView) findViewById(R.id.serchListView);

        backButton = (ImageButton) findViewById(R.id.searchback);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SearchActivity.this.finish();
            }
        });


        backButton = (ImageButton) findViewById(R.id.searchid);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView = (TextView) findViewById(R.id.searchedittext);
                name = textView.getText().toString();
                getasyncTask().execute(null);

            }
        });



    }
    public AsyncTask getasyncTask(){
        final Manage manage =  new Manage();
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object... objects) {
                try {
                    informationList = manage.getInformationList(name, Constant.locData.longitude +"",  Constant.locData.latitude+"", "5000" + "", "1");
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (JSONException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                if(informationList==null){
                    Toast.makeText(SearchActivity.this,"没有找到您要的信息", Toast.LENGTH_LONG).show();
                    return;
                }
                listView.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return informationList.size();  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public Object getItem(int position) {
                        return informationList.get(position);  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;  //To change body of implemented methods use File | Settings | File Templates.
                    }


                    @Override
                    public View getView(final int position, View convertView, ViewGroup parent) {

                        LayoutInflater layoutInflater = getLayoutInflater();
                        convertView = layoutInflater.inflate(R.layout.informationlist, parent, false);

//                if (convertView == null) {
//                    LayoutInflater layoutInflater = getLayoutInflater();
//                    convertView = layoutInflater.inflate(R.layout.categorylist, parent, false);
//                }

                        convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(SearchActivity.this, PoiderailsActivity.class);
                                Constant.information =  informationList.get(position);
                                startActivity(intent);

                            }
                        });
                        TextView nameTextView = (TextView) convertView.findViewById(R.id.textname);
                        TextView addressTextView = (TextView) convertView.findViewById(R.id.textaddress);
                        TextView distanceTextView = (TextView) convertView.findViewById(R.id.distance);


                        nameTextView.setText(informationList.get(position).getName());
                        addressTextView.setText(informationList.get(position).getAddress());
                        distanceTextView.setText(informationList.get(position).getDistance() + "米");


                        return convertView;  //To change body of implemented methods use File | Settings | File Templates.
                    }
                });


            }


        };
        return asyncTask;
    }
}

