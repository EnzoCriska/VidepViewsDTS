import java.util.LinkedList;


/**
     *
     * @author Enzo
     */
    public class Inventory {
        private int MaxSize = 500000;
        private boolean full = false;
        private LinkedList<Video> list = new LinkedList<Video>();

        public synchronized void writeRecord(Video str){
            list.add(str);
            if (list.size() >= MaxSize) full = true;
        }

        public synchronized Video getRecord(){
            return list.poll();
        }

        public synchronized boolean checkEmpty(){
            if (list.isEmpty()) return true;
            else return false;
        }

        public  synchronized int getSize(){
            return list.size();
        }

        public synchronized  boolean isFull(){
            return full;
        }

        public synchronized LinkedList copyList(int size){
            LinkedList<Video> listCopy = new LinkedList<Video>();
            if (list.size() >= size){
                for (int i = 0; i<size; i++){
                    listCopy.add(list.poll());
                }
            }
            return listCopy;
        }
    }
