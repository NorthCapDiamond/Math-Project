package Commands;

import Data.Dot;
import Managers.CollectionManager;
import util.Environment;

import java.util.LinkedList;

public class Integrate implements ICommand{


    public boolean isSelfIntersection(double[] equation1, double[] equation2){
        int equationType1 = (int) Math.round(equation1[4]);
        int equationType2 = (int) Math.round(equation2[4]);
        // let's look on some main situations:


        //y1 = parameter, y2 = parameter or x1 = parameter or x2 = parameter
        if(equation1.length==equation2.length && equation1.length == 5){

            if (equationType1 == equationType2 && equationType1==1){
                return (equation1[0]==equation2[0]);
            }
            if (equationType1 == equationType2 && equationType1==0){
                return (equation1[1]==equation2[1]);
            }

            // else they are cross like and there is an intersection if the point belongs to both lines;

            if (equationType1 == 1 && equationType2 == 0){
                double x = equation1[0];
                double y = equation2[1];
                double maxX1 = Double.max(equation1[0], equation1[2]);
                double maxX2 = Double.max(equation2[0], equation2[2]);
                double maxY1 = Double.max(equation1[1], equation1[3]);
                double maxY2 = Double.max(equation2[1], equation2[3]);

                double minX1 = Double.min(equation1[0], equation1[2]);
                double minX2 = Double.min(equation2[0], equation2[2]);
                double minY1 = Double.min(equation1[1], equation1[3]);
                double minY2 = Double.min(equation2[1], equation2[3]);
                if ((minX1==minX2 || minY1==minY2)&&(minY1<=y && y<=maxY1)&&(minX2<=x && x<=maxX2)){
                    return false;
                }
                return (y<=maxY2 && y>=minY2) && (minX1<=x && x<=maxX1);

            }

            if (equationType1 == 0 && equationType2 == 1){
                double x = equation2[0];
                double y = equation1[1];
                double maxX1 = Double.max(equation1[0], equation1[2]);
                double maxX2 = Double.max(equation2[0], equation2[2]);
                double maxY1 = Double.max(equation1[1], equation1[3]);
                double maxY2 = Double.max(equation2[1], equation2[3]);

                double minX1 = Double.min(equation1[0], equation1[2]);
                double minX2 = Double.min(equation2[0], equation2[2]);
                double minY1 = Double.min(equation1[1], equation1[3]);
                double minY2 = Double.min(equation2[1], equation2[3]);
                if ((minX1==minX2 || minY1==minY2)&&(minY2<=y && y<=maxY2)&&(minX1<=x && x<=maxX1)){
                    return false;
                }
                return (minY2<=y && y<=maxY2)&&(minX1<=x && x<=maxX1);
            }
        }

        //x1 or x2 = parameter, but one of them is y = kx + b
        if (equationType1==1 && equation1.length==5 && equation2.length==6){
            double y = equation2[4]*equation1[0]+equation2[5];
            return (y>Double.min(equation2[1],equation2[3]) && y< Double.max(equation2[1],equation2[3]));
        }

        //y1 or y2 = parameter, but one of them is y = kx + b
        if (equationType1==0 && equation1.length==5 && equation2.length==6){
            double x = (equation1[1]-equation2[5])/(equation2[4]);
            return (x<Double.max(equation2[0],equation2[2])&&x>Double.min(equation2[0],equation2[2]));
        }
        //x1 or x2 = parameter, but one of them is y = kx + b
        if (equationType2==1 && equation2.length==5 && equation1.length==6){
            double y = equation1[4]*equation2[0]+equation1[5];
            return (y < Double.max(equation1[1], equation1[3]) && y > Double.min(equation1[1], equation1[3]));
        }

        //y1 or y2 = parameter, but one of them is y = kx + b
        if (equationType2==0 && equation2.length==5 && equation1.length==6){
            double x = (equation2[1]-equation1[5])/(equation1[4]);
            return (x < Double.max(equation1[0], equation1[2]) && x > Double.min(equation2[0], equation2[2]));
        }


        //last situation, y1 = k1x+b1, y2 = k2x + b2
        // solutions of y1 = y2 => k1x+b1 = k2x+b2 | or x1 = x2 => (y-b1)/k1 = (y-b2)/k2 are:

        //one contains other
        if(equation1[4]==equation2[4] && equation1[5]==equation2[5]){
            double maxX1 = Double.max(equation1[0],equation1[2]);
            double maxX2 = Double.max(equation2[0],equation2[2]);

            double minX1 = Double.min(equation1[0],equation1[2]);
            double minX2 = Double.min(equation2[0],equation2[2]);

            if(minX1>=maxX2 || minX2>=maxX1){
                return false;
            }
            else {
                return true;
            }
        }


        //else
        double x = (equation2[5]-equation1[5])/(equation1[4]-equation2[4]);
        double y = (equation1[5]*equation2[4]-equation2[5]*equation1[4])/(equation2[4]-equation1[4]);

        return (x>Double.min(equation1[0],equation1[2]) && x<Double.max(equation1[0], equation1[2]) && x>Double.min(equation2[0],equation2[2]) && x<Double.max(equation2[0], equation2[2]) && y>Double.min(equation1[1],equation1[3]) && y<Double.max(equation1[1], equation1[3]) && y>Double.min(equation2[1],equation2[3]) && y<Double.max(equation2[1], equation2[3]));
    }

    public double[] equations(Dot dotA, Dot dotB){
        double x1 = dotA.getX();
        double x2 = dotB.getX();
        double y1 = dotA.getY();
        double y2 = dotB.getY();
        // y = kx + b
        // let's look on some main situations:

        // if x1 = x2 => equation does not depend on y => (x = parameter)
        if(x1 == x2){
            return new double[]{x1, y1, x2, y2, 1}; //here we return x = parameter; 1 means the equality x
        }

        // if y1 = y2 => equation does not depend on x => (y = parameter)
        if(y1 == y2){
            return new double[]{x1, y1, x2, y2, 0}; //here we return y = parameter; 0 means the equality x
        }

        // if x1!=x2 and y1!=y2 => we can find coefficients "k" and "b" from "y = kx + b"
        double k = (y2 - y1)/(x2 - x1);
        double b = y1 - k*x1;
        return new double[]{x1, y1, x2, y2, k, b}; //here we return the equation y = kx+b

    }

    public double square(double deltaX, double deltaY){
        return deltaX*deltaY;
    }
    public double sinhXY(double x, double y){
        //sinh x = (e^x - e^(-x))/2
        //my function is: sinh t, where t=(2x+y)/100 => (e^t-e^(-t))/2
        return ((Math.pow(Math.E,(2*x+y)/100)-Math.pow(Math.E,-1*(2*x+y)/100))/2);
    }
    public double firstPartialDxAndDy(double x,double y, double xo, double yo){
        return  ((Math.pow(Math.E, -xo/50 -yo/100))/(100) + (Math.pow(Math.E,xo/50 + yo/100))/(100))*(x-xo) +
                ((Math.pow(Math.E,-xo/50 - yo/100))/(200) + (Math.pow(Math.E,xo/50 + yo/100))/(200))*(y-yo);
    }
    public double secondPartialDx2AndDxDyAndDy2(double x, double y, double xo, double yo){
        return (1/2) * (
                (-(Math.pow(Math.E,-xo/50 -yo/100))/5000 + (Math.pow(Math.E,xo/50 +yo/100))/5000)*(Math.pow(x-xo,2))+
              2*(-(Math.pow(Math.E,-xo/50 -yo/100))/10000 +(Math.pow(Math.E, xo/50 + yo/100))/10000)*(x-xo)*(y-yo)+
                (-(Math.pow(Math.E,-xo/50 -yo/100))/20000 +(Math.pow(Math.E, xo/50 + yo/100))/20000)*(Math.pow(y-yo,2))
        );
    }


    //Ray tracing method
    //Accounting for the number of intersections
    public boolean dotInPolygon(double x, double y, CollectionManager collectionManager){
        // I want to create equation for y = a and x from dot.x to maxX, for dot and then count, how many times will it cross my polygon.
        // If it's n%2==1 => in polygon, else not.
        // my x in range [-51; 89]. that means, my range will be [dot.x;89]
        Dot dot = new Dot(x,y,-1);
        int counterOfIntersections = 0;
        Dot edge = new Dot(89,dot.getY(),-2);
        double[] ray = equations(dot, edge);

        for (int i = 1; i < collectionManager.length() ; i++) {
            if (isSelfIntersection(ray, equations(collectionManager.findById(i),collectionManager.findById(i+1))) || isSelfIntersection(ray,equations(collectionManager.findById(1),collectionManager.findById(collectionManager.length())))){
                if(equations(collectionManager.findById(i),collectionManager.findById(i+1)).length==5 && equations(collectionManager.findById(i),collectionManager.findById(i+1))[4]==0){
                    return rayOnTheEdge(dot.getX(), dot.getY(), collectionManager);
                }
                counterOfIntersections++;
            }
        }
        return counterOfIntersections % 2 == 1;
    }

    public boolean rayOnTheEdge(double x, double y, CollectionManager collectionManager){
        double[] ray = equations(new Dot(x,y,-1), new Dot(89,y,-1));
        for (int i = 1; i < collectionManager.length(); i++) {
            double [] newEquation = equations(collectionManager.findById(i),collectionManager.findById(i+1));
            double maxY1 = Double.max(ray[1], ray[3]);
            double maxY2 = Double.max(newEquation[1], newEquation[3]);
            if(newEquation.length==5 && newEquation[4]==0 && maxY1 == maxY2){
                double maxX1 = Double.max(ray[0], ray[2]);
                double maxX2 = Double.max(newEquation[0], newEquation[2]);

                double minX1 = Double.min(ray[0], ray[2]);
                double minX2 = Double.min(newEquation[0], newEquation[2]);
                double minY1 = Double.min(ray[1], ray[3]);
                double minY2 = Double.min(newEquation[1], newEquation[3]);
                return (minX2<=minX1);
            }
        }
        double [] newEquation = equations(collectionManager.findById(1),collectionManager.findById(collectionManager.length()));
        if(newEquation.length==5 && newEquation[4]==0){
            double maxX1 = Double.max(ray[0], ray[2]);
            double maxX2 = Double.max(newEquation[0], newEquation[2]);
            double maxY1 = Double.max(ray[1], ray[3]);
            double maxY2 = Double.max(newEquation[1], newEquation[3]);

            double minX1 = Double.min(ray[0], ray[2]);
            double minX2 = Double.min(newEquation[0], newEquation[2]);
            double minY1 = Double.min(ray[1], ray[3]);
            double minY2 = Double.min(newEquation[1], newEquation[3]);
            return (minX2<=minX1 && maxY1 == maxY2);

        }
        return false;
    }

    public int characteristicFunction(double x, double y, CollectionManager collectionManager){
        if(dotInPolygon(x,y, collectionManager)){
            return 1;
        }
        return 0;
    }



    @Override
    public void execute(Environment environment, String message) {
        if (environment.getCollectionManager().length()<3){
            environment.getPrintStream().println("You need to fill the collection first!");
            return;
        }
        double xo = -1000;
        double yo = -1000;
        double answer = 0;
        double deltaX = 1;
        double deltaY = 1;
        double s = square(deltaX,deltaY);
        for (double x = 4; x <= 90 ; x+=deltaX) {
            for (double y = -51; y <= 89 ; y+=deltaY) {
                if(xo==-1000 && yo==-1000){
                    if (dotInPolygon(x,y,environment.getCollectionManager())){
                        xo = x;
                        yo = y;
                    }
                }
                answer+=sinhXY(x,y)*characteristicFunction(x, y, environment.getCollectionManager())*s;
            }
        }
        environment.getPrintStream().printf("The integral sums for some partition of the bar equals : %f\n",answer);
        environment.getPrintStream().println("Now we need to understand the error");

        double error = 0;
        for (double x = 4; x <= 90; x+=deltaX) {
            for (double y = -51; y <= 89; y+=deltaY) {
                error+= (sinhXY(xo,yo) + firstPartialDxAndDy(x,y,xo,yo) + secondPartialDx2AndDxDyAndDy2(x,y,xo,yo))*characteristicFunction(x,y,environment.getCollectionManager());
            }

        }
        for (int i = 1; i < environment.getCollectionManager().length(); i++) {
            error+=1/2*deltaX*deltaY*(environment.getCollectionManager().distanceBetweenDots(environment.getCollectionManager().findById(i),environment.getCollectionManager().findById(i+1)));

        }
        error+=1/2*deltaX*deltaY*(environment.getCollectionManager().distanceBetweenDots(environment.getCollectionManager().findById(1),environment.getCollectionManager().findById(environment.getCollectionManager().length())));

        environment.getPrintStream().printf("|Error - Answer| = %f", Math.abs(error-answer));
    }

    @Override
    public String getName() {
        return "integrate";
    }

    @Override
    public String getDescription() {
        return "integrate : Calculates the integral sums for some partition of the bar";
    }
}
