package info.anth.locationfinder;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import org.w3c.dom.DOMImplementation;

import java.util.List;

/**
 * An activity representing a list of Locations. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link LocationDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class LocationListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    DBHelper locationDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Added new value", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                addLocation(view);

                //LocationContent.addItem(new LocationContent.LocationItem(null,"a test", 41.17, -73.86, "my address"));
            }
        });

        View recyclerView = findViewById(R.id.location_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.location_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        Log.i("ajc", "before Fetch");
        new FetchDatabase().execute();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(LocationContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<LocationContent.LocationItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<LocationContent.LocationItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.location_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mNameView.setText(mValues.get(position).location_name);
            holder.mLongitudeView.setText(String.valueOf(mValues.get(position).longitude));
            holder.mLatitudeView.setText(String.valueOf(mValues.get(position).latitude));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(LocationDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        LocationDetailFragment fragment = new LocationDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.location_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, LocationDetailActivity.class);
                        intent.putExtra(LocationDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            //public final TextView mContentView;
            public final TextView mNameView;
            public final TextView mLongitudeView;
            public final TextView mLatitudeView;
            public LocationContent.LocationItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mNameView = (TextView) view.findViewById(R.id.location_name);
                mLongitudeView = (TextView) view.findViewById(R.id.longitude);
                mLatitudeView = (TextView) view.findViewById(R.id.latitude);
            }

            @Override
            public String toString() {
                //return super.toString() + " '" + mContentView.getText() + "'";
                return super.toString() + " '" + mNameView.getText() + "'";
            }
        }
    }

    private void addLocation(View view) {
        //locationDB.insertLocation("a test", 41.17, -73.86, "my address");
        Context context = view.getContext();
        Intent intent = new Intent(context, AddLocation.class);
        //intent.putExtra(LocationDetailFragment.ARG_ITEM_ID, holder.mItem.id);

        context.startActivity(intent);
    }

    private class FetchDatabase extends AsyncTask<Void, Void, Cursor> {
        private final String LOG_TAG = FetchDatabase.class.getSimpleName();

        @Override
        protected Cursor doInBackground(Void... Void) {
            //Log.i("ajc", "in doInBackground");
            /*
            if (params.length == 0) {
               return null;
            }
            */

            //Context context = Activity.getContext();
            locationDB = new DBHelper(getApplicationContext());

            return locationDB.getAllLocations();

        }

        @Override
        protected void onPostExecute(Cursor result) {
            //Log.i("ajc", "in onPost");
            if (result != null){
                //Log.i("ajc", "in onPost !null");
                LocationContent.clear();
                result.moveToFirst();
                while(!result.isAfterLast()){
                    //Log.i("ajc", "ColumnIndex: " + String.valueOf(result.getColumnIndex(FeedReaderContractLocations.FeedEntryLocations._ID)));
                    //Log.i("ajc", "String: " + result.getString(0));
                    String id = result.getString(result.getColumnIndex(FeedReaderContractLocations.FeedEntryLocations._ID));
                    String name = result.getString(result.getColumnIndex(FeedReaderContractLocations.FeedEntryLocations.COLUMN_NAME));
                    Double longitude = result.getDouble(result.getColumnIndex(FeedReaderContractLocations.FeedEntryLocations.COLUMN_LONGITUDE));
                    Double latitude = result.getDouble(result.getColumnIndex(FeedReaderContractLocations.FeedEntryLocations.COLUMN_LATITUDE));
                    String address = result.getString(result.getColumnIndex(FeedReaderContractLocations.FeedEntryLocations.COLUMN_ADDRESS));

                    LocationContent.addItem(new LocationContent.LocationItem(id, name, longitude, latitude, address));

                    result.moveToNext();
                }

            }
        }
    }

}
