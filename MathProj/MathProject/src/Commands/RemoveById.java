package Commands;

import Data.Dot;
import Managers.CollectionManager;
import util.Environment;
import util.IntersectionsException;

public class RemoveById implements ICommand{
    private int id;

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
                return (x>Double.min(equation1[0],equation1[2]) && x<Double.max(equation1[0], equation1[2]) && x>Double.min(equation2[0],equation2[2]) && x<Double.max(equation2[0], equation2[2]) && y>Double.min(equation1[1],equation1[3]) && y<Double.max(equation1[1], equation1[3]) && y>Double.min(equation2[1],equation2[3]) && y<Double.max(equation2[1], equation2[3]));
            }

            if (equationType1 == 0 && equationType2 == 1){
                double x = equation2[0];
                double y = equation1[1];
                return (x>Double.min(equation1[0],equation1[2]) && x<Double.max(equation1[0], equation1[2]) && x>Double.min(equation2[0],equation2[2]) && x<Double.max(equation2[0], equation2[2]) && y>Double.min(equation1[1],equation1[3]) && y<Double.max(equation1[1], equation1[3]) && y>Double.min(equation2[1],equation2[3]) && y<Double.max(equation2[1], equation2[3]));
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

    @Override
    public void execute(Environment environment, String message) {
        CollectionManager collectionManager = environment.getCollectionManager();


        try {
            id = Integer.parseInt(message);
        }catch (Exception e){
            environment.getPrintStream().println("You entered a number other than Integer");
            return;
        }

        try {
            if(!collectionManager.existsById(id)){
                throw new NullPointerException();
            }
            collectionManager.removeById(id);
            environment.getPrintStream().println("Element with ID: "+String.format("%d",id)+" was removed.");




            //we need to update all indexes

            for (int i = 1; i <= collectionManager.length(); i++) {
                if (!collectionManager.existsById(i)){
                    collectionManager.findById(i+1).setId(i);
                }
            }
            environment.getPrintStream().println("indexes are fixed");

            environment.getPrintStream().println("Checking for intersections and amount of dots...");


            if (collectionManager.length()<3){
                environment.getPrintStream().println("Not enough dots for creating polygon... Collection is cleared");
                collectionManager.removeAllElements();
                return;
            }
            Dot newDot = collectionManager.findById(collectionManager.length());
            double[] newEquation1 = new double[]{0,0,0,0,0};
            double[] newEquation2 = new double[]{0,0,0,0,0};
            if(collectionManager.length()>0) {
                newEquation1 = equations(collectionManager.findById(collectionManager.length()), newDot);
                newEquation2 = equations(collectionManager.findById(1),newDot);
            }
            if(collectionManager.length()>=3) {
                for (int k = 1; k <= collectionManager.length() - 1; k++) {
                    if ((isSelfIntersection(equations(collectionManager.findById(k),collectionManager.findById(k+1)),newEquation1) || isSelfIntersection(equations(collectionManager.findById(k),collectionManager.findById(k+1)),newEquation2)) && (k==collectionManager.length()-1)){
                        throw new IntersectionsException();
                    }
                    if (isSelfIntersection(equations(collectionManager.findById(k),collectionManager.findById(k+1)),newEquation1)  && (k == collectionManager.length()-1)){
                        throw new IntersectionsException();
                    }
                }
            }

            environment.getPrintStream().println("Command finished successfully!");

        }catch (NullPointerException exception){
            environment.getPrintStream().println("No such element in your collection");
            return;
        } catch (IntersectionsException e) {
            environment.getPrintStream().println("You've created intersections... Collection cleared ");
            collectionManager.removeAllElements();
            return;
        }
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }

    @Override
    public String getDescription() {
        return "remove_by_id id : Remove an element from the collection by its id.";
    }
}



