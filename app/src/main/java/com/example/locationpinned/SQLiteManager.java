package com.example.locationpinned;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLiteManager extends SQLiteOpenHelper {

    private static SQLiteManager sqLiteManager;
    private static final String DATABASE_NAME = "LocationsDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Location";
    private static final String COUNTER = "Counter";
    private static final String ID_FIELD = "id";
    private static final String LAT_FIELD = "latitude";
    private static final String LONG_FIELD = "longitude";
    private static final String ADD_FIELD = "address";
    private static final String DELETED_FIELD = "deleted";

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    public SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteManager instanceOfDatabase(Context context) {
        if (sqLiteManager == null)
            sqLiteManager = new SQLiteManager(context);

        return sqLiteManager;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sql;
        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ID_FIELD)
                .append(" INT, ")
                .append(LAT_FIELD)
                .append(" REAL, ")
                .append(LONG_FIELD)
                .append(" REAL, ")
                .append(ADD_FIELD)
                .append(" TEXT, ")
                .append(DELETED_FIELD)
                .append(" TEXT)");

        sqLiteDatabase.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void addLocationToDatabase(Location location) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, location.getId());
        contentValues.put(LAT_FIELD, location.getLatitude());
        contentValues.put(LONG_FIELD, location.getLongitude());
        contentValues.put(ADD_FIELD, location.getAddress());
        contentValues.put(DELETED_FIELD, getStringFromDate(location.getDeleted()));

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public void populateLocationListArray() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {
            if (result.getCount() != 0) {
                while (result.moveToNext()) {
                    int id = result.getInt(1);
                    Double latitude = result.getDouble(2);
                    Double longitude = result.getDouble(3);
                    String add = result.getString(4);
                    String stringDeleted = result.getString(5);
                    Date deleted = getDateFromString(stringDeleted);
                    Location location = new Location(id, latitude, longitude, add, deleted);
                    location.locationArrayList.add(location);
                }
            }
        }
    }

    public void updateLocationInDB(Location location) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, location.getId());
        contentValues.put(LAT_FIELD, location.getLatitude());
        contentValues.put(LONG_FIELD, location.getLongitude());
        contentValues.put(ADD_FIELD, location.getAddress());
        contentValues.put(DELETED_FIELD, getStringFromDate(location.getDeleted()));

        sqLiteDatabase.update(TABLE_NAME, contentValues, ID_FIELD + " =? ", new String[]{String.valueOf(location.getId())});
    }

    private static String getStringFromDate(Date date) {
        if (date == null)
            return null;
        return dateFormat.format(date);
    }

    private Date getDateFromString(String string) {
        try {
            return dateFormat.parse(string);
        } catch (ParseException | NullPointerException e) {
            return null;
        }
    }
}
