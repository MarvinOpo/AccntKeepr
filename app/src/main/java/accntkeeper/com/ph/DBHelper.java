package accntkeeper.com.ph;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper{
    Context context;
    final static String DBNAME = "db_guia";
    final static String USERS = "tbl_user";
    final static String ACCOUNTS = "tbl_accounts";

    public DBHelper(Context context) {
        super(context, DBNAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "Create table "+USERS+"(id integer primary key, name varchar(50)";
        String sql1 = "Create table "+ACCOUNTS+"(id integer primary key, name varchar(50) site_name varchar(50), " +
                "user_name varchar(50), password varchar(50), category varchar(50))";
        db.execSQL(sql);
        db.execSQL(sql1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE "+USERS+"IF EXISTS";
        String sql1 = "DROP TABLE "+ACCOUNTS+"IF EXISTS";
        db.execSQL(sql);
        db.execSQL(sql1);
        onCreate(db);
    }

    public void addUser(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        db.insert(USERS, null, cv);
        db.close();
    }

    public boolean nameTaken(String name){
        boolean ok = false;
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "Select * from "+USERS+" where name ='"+name+"'";
        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()) ok = true;
        c.close();
        return ok;
    }

    public void addAccoount(Accounts acc){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("category", acc.category);
        cv.put("site_name", acc.site_name);
        cv.put("user_name", acc.user_name);
        cv.put("password", acc.password);
        db.insert(ACCOUNTS, null, cv);
        db.close();
    }

    public ArrayList<Accounts> getAccountsByCategory(String name, String category){
        ArrayList<Accounts> accs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "Select * from "+ACCOUNTS+" where name ='"+name+"' and category = '"+category+"'";
        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()){
            while(!c.isAfterLast()) {
                String site_name = c.getString(c.getColumnIndex("site_name"));
                String user_name = c.getString(c.getColumnIndex("user_name"));
                String password = c.getString(c.getColumnIndex("password"));
                accs.add(new Accounts(name, category, site_name, user_name, password));

                c.moveToNext();
            }
        }
        c.close();
        return accs;
    }
}
