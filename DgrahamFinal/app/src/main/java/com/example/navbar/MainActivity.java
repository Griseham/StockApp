package com.example.navbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    public static JSONObject savedResponse;
    private RequestQueue mQueue;
    MainActivity context;

    ListView listView;
    SearchView searchView;











    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        context = this;





        mQueue = Volley.newRequestQueue(this);










        //Navigation drawer code

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null){
        navigationView.setCheckedItem(R.id.nav_homee);}







    }


    public void Logout(View v){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent (getApplicationContext(), SignIn.class));
        finish();
    }




    //When items on the navbar are selected, the page changes
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){

            case R.id.nav_homee:
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);

                break;
            case R.id.nav_myCoins:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyCoins()).commit();
                break;
            case R.id.nav_myStocks:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyStocks()).commit();
                break;





        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();


        }


    }


    //parsing button uses this function to call the parsing function

    public void onGoClick(View v){

        jsonParse();
    }






    //sends a request with certain headers, API key and saves the response
    private void jsonParse(){

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }

        String url = "https://investing-cryptocurrency-markets.p.rapidapi.com/coins/list?edition_currency_id=12&lang_ID=1&time_utc_offset=28800&sort=PERC1D_DN&page=1";

        System.out.println("Using URL " + url);

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                savedResponse = response;




                //When I get a response, travel down to the desired array within the JSON response and loop through
                //the array and input that information in the right parameters
                searchView = (SearchView) findViewById(R.id.searchView);

                listView = (ListView) findViewById(R.id.listView);


                ArrayList<String> arrayList = new ArrayList<>();


                JSONObject stockData = savedResponse ;

                JSONObject jsonObj = null;
                JSONObject ja_data2 = null;


                //loops through the first array

                try {
                    JSONArray ja_data = stockData.getJSONArray("data");
                    JSONArray jsonArray = null;


                    for (int i  = 0; i < ja_data.length(); i++){
                        jsonObj = ja_data.getJSONObject(i);


                        ja_data2 = jsonObj.getJSONObject("screen_data");












                    }


                    //loops through the second array

                    jsonArray = ja_data2.getJSONArray("crypto_data");

                    for (int i = 0; i < jsonArray.length() ; i++) {

                        JSONObject crypto = jsonArray.getJSONObject(i);
                        String name = crypto.getString("name");
                        arrayList.add(name);


                    }








                } catch (JSONException e) {
                }

                //saved the information from the array into the listview

                ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(arrayAdapter);

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit (String s) {
                        searchView.clearFocus();








                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {

                        arrayAdapter.getFilter().filter(s);

                        return false;
                    }

                });






                //home code end









            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Something didn't work");


            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-rapidapi-host", "investing-cryptocurrency-markets.p.rapidapi.com");
                params.put("useQueryString", "true");
                params.put("x-rapidapi-key", "a690623a80msh16ec45431d33cc1p1b9723jsne05e3146ea39");

                return params;
            };

        };
        mQueue.add(request);





















    }




}