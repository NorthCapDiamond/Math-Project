package Managers;
import Data.*;

import java.util.LinkedList;

public class CollectionManager {
    LinkedList<Dot> dots;


    public CollectionManager(){
        dots = new LinkedList<Dot>();
    }

    public CollectionManager(LinkedList<Dot> dots) {
        this.dots = dots;
    }

    public void add(Dot dot){
        dots.add(dot);
    }

    public LinkedList<Dot> getDots(){
        return this.dots;
    }

    public void removeAllElements(){
        dots.clear();
    }

    public Dot getHead(){
        return dots.getFirst();
    }

    public int length(){
        try {
            return dots.size();
        }catch (NullPointerException exception) {
            return 0;
        }
    }

    public Dot findById(int id){
        for(Dot dot: dots){
            if(dot.getId()==id){
                return dot;
            }
        }
        return null;
    }

    public boolean existsById(int id){
        return findById(id) != null;
    }

    public void removeById(int id){
        if (existsById(id)) {
            dots.remove(findById(id));
        }
    }

    public void removeLast(){
        this.dots.remove(findById(dots.size()));
    }

    public void replace(int id, double x, double y){
        Dot currentDot = findById(id);
        if(currentDot!=null){
            currentDot.setXY(x,y);
        }
    }

    public double distanceBetweenDots(Dot a, Dot b){
        return Math.sqrt(Math.pow(b.getX()-a.getX(),2)+Math.pow(b.getY()-a.getY(),2));
    }

    public boolean triangleInequality(Dot a, Dot b, Dot c){
        if(distanceBetweenDots(a,b) >= distanceBetweenDots(a,c)+distanceBetweenDots(c,b)){
            return false;
        }if(distanceBetweenDots(c,b) >= distanceBetweenDots(a,b)+distanceBetweenDots(c,a)){
            return false;
        }if(distanceBetweenDots(a,c) >= distanceBetweenDots(a,b)+distanceBetweenDots(c,b)){
            return false;
        }
        return true;
    }

    @Override
    public String toString(){
        String answer = "";
        answer+=("Displaying the elements of a collection...\n");
        for (Dot dot : dots) {
            answer+=(String.format("Element with id: %d",dot.getId())+"\n");
            answer+=dot.toString()+"\n";
        }
        return answer;
    }
}
