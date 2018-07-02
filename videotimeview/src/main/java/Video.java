import java.util.LinkedList;

public class Video {
    private String video_id;
    private int views;
    private LinkedList<Data> data = new LinkedList<Data>();

    public Video(String id, int views, LinkedList data){
        video_id = id;
        this.views = views;
        this.data = data;
    }

    public void addData(Data datas){
        data.add(datas);
    }

    public String getVideo_id(){
        return video_id;
    }

    public int getViews(){
        return views;
    }

    public LinkedList getData(){
        return data;
    }
}
