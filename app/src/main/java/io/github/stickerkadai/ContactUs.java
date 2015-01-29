package io.github.stickerkadai;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


public class ContactUs extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        String[] contacts = new String[]{"Mail to us", "Facebook", "Twitter", "Github"};

        ListAdapter conatctsListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts);

        ListView contactsListView =(ListView) findViewById(R.id.contactListView);

        contactsListView.setAdapter(conatctsListAdapter);

        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                String uri;
                switch (position) {
                    case 0:
                        intent = new Intent(Intent.ACTION_VIEW);
                        Uri data = Uri.parse("mailto:stickerkadai@gmail.com");
                        intent.setData(data);
                        startActivity(intent);
                        break;
                    case 1:
                        uri = "http://facebook.com/stickerkadai";
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(intent);
                        break;
                    case 2:
                        uri = "http://twitter.com/stickerkadai";
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(intent);
                        break;
                    case 3:
                        uri = "http://github.com/stickerkadai";
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(intent);
                        break;
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_us, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
