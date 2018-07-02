import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Connecttion {
    MongoClient mongoClient;
    DB databases;
    static DBCollection collection;
    static DBCollection collection2 ;

    public Connecttion(){
        try {
            mongoClient = new MongoClient();
            databases = mongoClient.getDB("DTS_video");
            collection = databases.getCollection("video");
            collection2 = databases.getCollection("total");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    //Convert String date to long
    public static long convertDateLong(String str){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();
        return millis;
    }

    //convert long to String date
    public static String convertLongDate(long datelong){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date(datelong);
        String str = sdf.format(date);
        return str;
    }
}
