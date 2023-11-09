package com.example.locationpinned;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button newLocation;
    private Button populate;
    private ListView locationListView;
    private List<Location> locations = new ArrayList<>();

    private SearchView search;

    protected void onCreate(Bundle savedInstanceState) {

        //Load home_screen.xml once application runs
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        locationListView = findViewById(R.id.listView);

        setLocationAdapter();
        loadFromDBToMemory();

        //OnClick functionality to allow notes from the list to be edited
        locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                Location selectedLocation = (Location) locationListView.getItemAtPosition(position);
                Intent editLocationIntent = new Intent(getApplicationContext(), LocationDetails.class);
                editLocationIntent.putExtra(Location.LOCATION_EDIT_EXTRA, selectedLocation.getId());
                startActivity(editLocationIntent);
            }
        });


        newLocation = findViewById(R.id.newLocationButton);

        //OnClick listener to navigate to Location Details Page
        newLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newLocation(view);
            }
        });

        populate = findViewById(R.id.dataButton);

        //OnClick listener to navigate to Location Details Page
        populate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readLocationData();
            }
        });
    }

    //Function that creates the intent to switch between pages
    private void newLocation(View view){
        Intent intent = new Intent(this, LocationDetails.class);
        startActivity(intent);
    }

    private void readLocationData(){
        InputStream is = getResources().openRawResource(R.raw.locations);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        String line = "";

        try{
            reader.readLine();
            int id = 1;

            while ((line = reader.readLine()) != null){
                String[] tokens = line.split(",");

                Location sample = new Location();

                Double x = Double.parseDouble(tokens[0]);
                Double y = Double.parseDouble(tokens[1]);
                String address = getAddress(x,y);

                sample.setId(id);
                sample.setLatitude(x);
                sample.setLongitude(y);
                sample.setAddress(address);
                locations.add(sample);

                saveLocation(x,y, address);

                //Log.d("MyActivity", "Just created: " + sample);
                id++;
            }
        } catch(IOException e){
            Log.wtf("MyActivity", "Error reading data file on line " + line, e);
            e.printStackTrace();
        }

        recreate();
    }
    private void setLocationAdapter() {
        LocationAdapter locationAdapater = new LocationAdapter(getApplicationContext(), Location.nonDeletedNotes());
        locationListView.setAdapter(locationAdapater);

        search = findViewById(R.id.searchView);

        //TextListener to take user input for when text is altered
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //Filter for the user's input when input is entered
            @Override
            public boolean onQueryTextSubmit(String s) {
                locationAdapater.getFilter().filter(s.toString());
                //locationListView.setAdapter(locationAdapater);
                return false;
            }

            //Filter for the input when input is altered
            @Override
            public boolean onQueryTextChange(String s) {;
                locationAdapater.getFilter().filter(s);
                return false;
            }
        });
    }

    //Initialize SQLite manager to pull data and add it to the list
    private void loadFromDBToMemory() {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populateLocationListArray();
    }

    private void saveLocation(Double latitude, Double longitude, String address){
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        int id = Location.locationArrayList.size();
        Location newLocation = new Location(id, latitude, longitude, address);
        Location.locationArrayList.add(newLocation);
        sqLiteManager.addLocationToDatabase(newLocation);
    }

    private String getAddress(Double latitude, Double longitude){
        String address = "";

        if(Geocoder.isPresent()){
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            try {
                List<Address> addresses;
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if(!addresses.isEmpty()){
                    address = addresses.get(0).getAddressLine(0);
                } else{
                    address = "No Address Found";
                }
            } catch(IOException e){
                //e.printStackTrace();
                address = "No Address Found";
            }
        }

        return address;
    }
}