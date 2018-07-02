import com.mongodb.*;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;



public class viewsmain extends Connecttion{
    private static int pageTotalTime;

    public static Inventory inventory = new Inventory();


//    querry by Object querry --> return List video
     public static List queryDocuments(DBObject obj){
        Video video;
        List<Video> getVideo = new LinkedList<Video>();

        DBCursor cursor = collection.find(obj);
        List<DBObject> videoss = cursor.toArray();

        for (int i = 0; i<videoss.size(); i++){
            LinkedList list = new LinkedList();
            BasicDBList dat = (BasicDBList) videoss.get(i).get("data");
            for (int j = 0; j<dat.size(); j++){
                DBObject data1 =  (DBObject) dat.get(String.valueOf(j));
                list.add( new Data((Long)data1.get("date_time"), (Integer) data1.get("long_time"), (Integer) data1.get("views")));
            }
            video = new Video( (String) videoss.get(i).get("video_id"), (Integer) videoss.get(i).get("views"),list);
            getVideo.add(video);
        }
        return getVideo;
    }

    //querry longtime of video in 24 hour by video id, time start, time stop --> return list data
    public static LinkedList queryDocumentsByID(String id, String timeStart, String timeStop){
        BasicDBObject obj = new BasicDBObject("video_id", id);
        DBCursor cursor = collection.find(obj);
        DBObject videos = cursor.one();
        LinkedList list = new LinkedList();
            BasicDBList dat = (BasicDBList) videos.get("data");
            for (int j = 0; j<dat.size(); j++) {
                DBObject data1 = (DBObject) dat.get(String.valueOf(j));
                if ((Long) data1.get("date_time") >= convertDateLong(timeStart))
                    if ((Long) data1.get("date_time") <= convertDateLong(timeStop)) {
                        list.add(new Data((Long) data1.get("date_time"), (Integer) data1.get("long_time"), (Integer) data1.get("views")));
                    } else break;
            }

        return list;
    }



    //pull data in Array Document
    public static void pullItemsInArray(String video_id, Data datapull){
        collection.update(new BasicDBObject("video_id", video_id),
                new BasicDBObject("$pull", new BasicDBObject("data",
                        new BasicDBObject("date_time", datapull.getDate_time())
                                .append("long_time", datapull.getLong_time())
                                .append("views", datapull.getViews()))));
        revertupdateTotal(datapull.getDate_time(),datapull);
    }

    //delete Documents;
    public static void deletedDocuments(String key, String value){
        collection.remove(new BasicDBObject(key,value));
    }

    //update one field in Document
    public static void updateDocuments(String key, String value, BasicDBObject obj){
        collection.update(new BasicDBObject(key, value), new BasicDBObject("$set", obj));
    }





    // Cập nhật giảm thời gian xem.
    public static void revertupdateTotal(long date_time, Data data){
        BasicDBList list = new BasicDBList();
        list.add(new BasicDBObject("date_time",new BasicDBObject("$gte", date_time)));
        list.add(new BasicDBObject("date_time", new BasicDBObject("$lte",date_time+3600000)));
        BasicDBObject obj = new BasicDBObject("$and", list);
        DBCursor cursor = collection2.find(obj);
        DBObject datas = cursor.one();
        System.out.println(datas);
        if(datas != null) {
            collection2.update(new BasicDBObject("date_time", datas.get("date_time")), new BasicDBObject("$inc", new BasicDBObject("long_time", -data.getLong_time())
                    .append("total_views",-data.getViews())));
        }
    }

    // query total time view in hour
    public static List queryTotalTime(int skip , String timeStart, String timeStop){
        Data video;
        List<Data> getVideo = new LinkedList<Data>();
        BasicDBObject sortDK = new BasicDBObject("date_time", 1);
        DBCursor cursor = collection2.find().skip(skip).sort(sortDK).limit(10);
        System.out.println(cursor);
        List<DBObject> videoss = cursor.toArray();

        for (int i = 0; i<videoss.size(); i++){
            video = new Data( (Long) videoss.get(i).get("date_time"), (Integer) videoss.get(i).get("total_views"),(Integer) videoss.get(i).get("long_time"));
            getVideo.add(video);
            }
            pageTotalTime+=10;
        return getVideo;
    }

    public static void main(String args[]){
        new Connecttion();

//        ThreadRandom trD = new ThreadRandom();
//        Thread thread1 = new Thread(trD);
//        thread1.start();
//
//        ThreadPushInDB tPIDB = new ThreadPushInDB();
//        Thread thread2 = new Thread(tPIDB);
//        thread2.start();
//


    }
}
