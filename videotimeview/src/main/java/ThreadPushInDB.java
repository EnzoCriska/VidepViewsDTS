import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.LinkedList;

import static java.lang.Thread.sleep;

public class ThreadPushInDB extends Connecttion implements Runnable{


    public ThreadPushInDB(){
        new Connecttion();
    }
    @Override
    public void run() {
        while (true){
            if (viewsmain.inventory.getSize()> 0){
                Video video = viewsmain.inventory.getRecord();
                System.out.println("process video " + video.getVideo_id());
                if (queryById(video.getVideo_id()) != null)
                {
                    System.out.println("pushing " + video.getVideo_id());
                    pushToDocuments(video.getVideo_id(), (Data) video.getData().getFirst());

                }else
                    insertDocuments(viewsmain.inventory.getRecord());
                System.out.println("insert " + video.getVideo_id());

            }else {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public void insertDocuments(Video video){
        Data dt = (Data) video.getData().getFirst();
        DBObject list = new BasicDBList();
        ((BasicDBList) list).add(new BasicDBObject("date_time", dt.getDate_time())
                .append("long_time", dt.getLong_time()).append("views", dt.getViews()));
        DBObject object = new BasicDBObject("video_id",video.getVideo_id())
                .append("views", video.getViews())
                .append("data",list);
        collection.insert(object);
    }

    public Video queryById(String str){
        Video video;
        DBObject obj = new BasicDBObject("video_id", str);
        DBCursor cursor = collection.find(obj);
        DBObject vd = cursor.one();
        if (vd == null){
            return null;
        }else{
            System.out.println(vd);
            LinkedList list = new LinkedList();
            BasicDBList data = (BasicDBList) vd.get("data");
            for (int i = 0; i < data.size(); i++){
                DBObject data1 = (DBObject) data.get(String.valueOf(i));
                list.add( new Data((Long) data1.get("date_time"), (Integer) data1.get("long_time"), (Integer) data1.get("views")));
            }
            video = new Video((String) vd.get("video_id"), (Integer) vd.get("views"), list);
            return  video;
        }
    }

    //push data to Documents
    public void pushToDocuments(String video_id, Data dataUpdate){
        collection.update(new BasicDBObject("video_id", video_id),
                new BasicDBObject("$push",
                        new BasicDBObject("data",
                                new BasicDBObject("date_time", dataUpdate.getDate_time())
                                        .append("long_time", dataUpdate.getLong_time())
                                        .append("views", dataUpdate.getViews()))));
        updateTotal(dataUpdate.getDate_time(), dataUpdate);
    }

    // Cập nhật tăng tông thời gian xem
    public  void updateTotal(long date_time, Data data){
        BasicDBList list = new BasicDBList();
        list.add(new BasicDBObject("date_time",new BasicDBObject("$gte", date_time)));
        list.add(new BasicDBObject("date_time", new BasicDBObject("$lte",date_time+3600000)));
        BasicDBObject obj = new BasicDBObject("$and", list);
        DBCursor cursor = collection2.find(obj);
        DBObject datas = cursor.one();
        System.out.println(datas);
        if(datas != null) {
            collection2.update(new BasicDBObject("date_time", datas.get("date_time")), new BasicDBObject("$inc", new BasicDBObject("long_time", data.getLong_time())
                    .append("total_views",data.getViews())));

        } else {
            DBObject object = new BasicDBObject("date_time", date_time).append("total_views", data.getViews()).append("long_time",data.getLong_time());
            collection2.insert(object);
        }
    }
}
