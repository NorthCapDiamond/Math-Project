package Data;

public class Dot {
    private double x;
    private double y;
    private int id;
    public Dot(double x, double y, int id){
        this.x = x;
        this.y = y;
        this.id = id;
    }
    public Dot(int id){
        this.id = id;
    }

    public void setXY(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double[] getXY(){
        return new double[]{x,y};
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }


    public boolean dotEquals(Dot dot) {
        if (this.x == dot.getX() && this.y == dot.getY()){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("X: %f\nY: %f",x, y);
    }
}
