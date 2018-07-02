import java.util.LinkedList;
import java.util.Random;

import static java.lang.Thread.sleep;

public class ThreadRandom extends Connecttion implements Runnable{
    Random rd = new Random();
    @Override
    public void run(){
        while (true){
            if(!viewsmain.inventory.isFull()){
                Video videoRd = randomVideo();
                viewsmain.inventory.writeRecord(videoRd);

            } else {
                try {
                    System.out.println("Kho is full");
                    sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

        public Video randomVideo(){
            Video video;
            int ngay, gio, view;
            String str1="";
            String str = "2018/05/";
            ngay = 1+ rd.nextInt(30);
            gio = 1 + rd.nextInt(24);
            str = str + ngay + " " + gio + ":00:00";
            view = rd.nextInt(100);
            Data data = new Data(convertDateLong(str), rd.nextInt(200), view);
            LinkedList listData = new LinkedList();
            listData.add(data);

            for (int i =0; i<5; i++ ){
                str1 = str1 + (char)(97+rd.nextInt(25));
            }
            video = new Video(str1, view, listData);

            return video;
        }
    }
