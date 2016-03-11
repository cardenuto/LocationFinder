package info.anth.locationfinder;

import android.provider.BaseColumns;

public final class FeedReaderContractLocations {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FeedReaderContractLocations() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntryLocations implements BaseColumns {
        public static final String TABLE_NAME = "locations";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_ADDRESS = "address";
    }
}
