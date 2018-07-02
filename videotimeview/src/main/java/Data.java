public class Data {
    private long date_time;
    private int long_time;
    private int views;

    public Data (long date, int time, int views){
        date_time = date;
        long_time = time;
        this.views = views;
    }

    public long getDate_time(){
        return date_time;
    }

    public int getLong_time(){
        return long_time;
    }

    public int getViews(){ return  views;}
}
