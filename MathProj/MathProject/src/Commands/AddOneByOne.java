package Commands;

import Data.Dot;
import Managers.CollectionManager;
import util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class AddOneByOne implements ICommand{

    public boolean forcedQuit(int i, PrintStream printStream){
        if (i==2){
            printStream.println("The command is forcibly closed. Try again.");
            return true;
        }
        return false;
    }

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
        BufferedReader bufferedReader = environment.getBufferedReader();
        int amountOfDots = 0;
        int sizeOfCollectionBefore = collectionManager.length();

        for (int i = 0; i < 3; i++) {
            environment.getPrintStream().println("How many dots do you want to add?");
            try{
                amountOfDots = Integer.parseInt(bufferedReader.readLine());
                break;
            }catch (IOException e) {
                if(forcedQuit(i, environment.getPrintStream())){
                    return;
                }
                environment.getPrintStream().printf("Incorrect input, you have %d more attempts\n", 2-i);
            }
            catch (NumberFormatException e){
                if(forcedQuit(i, environment.getPrintStream())){
                    return;
                }
                environment.getPrintStream().printf("you need to write Integer in amount of dots. You have %d attempts\n", 2-i);
            }
            catch (Exception e){
                if(forcedQuit(i, environment.getPrintStream())){
                    return;
                }
            }
        }

        Dot newDot = new Dot(collectionManager.length()+1);
        environment.getPrintStream().println("Now you need to type double values for the dot like this: 'x,y' without '. 4<=x<=90 <-51=y<=89 '");
        for (int i = 0; i < 3; i++) {
            try {
                for (int j = 0; j < amountOfDots; j++) {
                    if(collectionManager.length()-sizeOfCollectionBefore==amountOfDots){
                        environment.getPrintStream().println("Command finished successfully!");
                        return;
                    }
                    String userLine = bufferedReader.readLine();
                    if (userLine.split(",").length!=2){
                        throw new IncorrectNumberOfArgumentsException();
                    }

                    double[] XY = new double[]{Double.parseDouble(userLine.split(",")[0]), Double.parseDouble(userLine.split(",")[1])};
                    double x = Double.parseDouble(userLine.split(",")[0]);
                    double y = Double.parseDouble(userLine.split(",")[1]);
                    if((x<4 || x>90) || (y<-51 || y>89)){
                        throw new ValidationException();
                    }
                    // now we need to check if line between this Dot and the last one from collection does not create intersections
                    newDot.setXY(XY[0],XY[1]);
                    double[] newEquation1 = new double[]{0,0,0,0,0};
                    double[] newEquation2 = new double[]{0,0,0,0,0};
                    if(collectionManager.length()>0) {
                        newEquation1 = equations(collectionManager.findById(collectionManager.length()), newDot);
                        newEquation2 = equations(collectionManager.findById(1),newDot);
                    }
                    if(collectionManager.length()>=2) {
                        if(collectionManager.length()==2 && collectionManager.length()-sizeOfCollectionBefore==amountOfDots-1){
                            if(!(collectionManager.triangleInequality(collectionManager.findById(1),collectionManager.findById(2),newDot))){
                                throw  new IntersectionsException();
                            }
                        }
                        for (int k = 1; k <= collectionManager.length() - 1; k++) {
                                if ((isSelfIntersection(equations(collectionManager.findById(k),collectionManager.findById(k+1)),newEquation1) || isSelfIntersection(equations(collectionManager.findById(k),collectionManager.findById(k+1)),newEquation2)) && (collectionManager.length()-sizeOfCollectionBefore==amountOfDots-1)){
                                    throw new IntersectionsException();
                                }
                                if (isSelfIntersection(equations(collectionManager.findById(k),collectionManager.findById(k+1)),newEquation1)  && (collectionManager.length()-sizeOfCollectionBefore!=amountOfDots-1)){
                                    throw new IntersectionsException();
                                }
                        }
                    }
                    for (Dot dot:collectionManager.getDots()) {
                        if (newDot.dotEquals(dot)){
                            throw new DuplicateException();
                        }
                    }
                    collectionManager.add(newDot);
                    newDot = new Dot(collectionManager.length()+1);
                }
                environment.getPrintStream().println("Command finished successfully!\n");
                return;

            }
            catch (DuplicateException e){
                environment.getPrintStream().printf("WARNING! this dot already exists. You have %d attempts\n",2-i);
                if(forcedQuit(i, environment.getPrintStream())){
                    return;
                }
            }
            catch (ValidationException e){
                environment.getPrintStream().printf("WARNING! 4<=x<=90 <-51=y<=89.You have %d attempts\n",2-i);
                if(forcedQuit(i, environment.getPrintStream())){
                    return;
                }
            }
            catch (NumberFormatException e ){
                environment.getPrintStream().printf("you need to write Integer. You have %d attempts\n", 2-i);
                if(forcedQuit(i, environment.getPrintStream())){
                    return;
                }
            }catch (IntersectionsException e){
                environment.getPrintStream().printf("this dot creates intersections. Try again please! You have %d more attempts\n", 2-i);
                if(forcedQuit(i, environment.getPrintStream())){
                    return;
                }
            }
            catch (IncorrectNumberOfArgumentsException e){
                environment.getPrintStream().printf("you need to type double values like this: 'x,y' without ', you have %d more attempts\n", 2-i);
                if(forcedQuit(i, environment.getPrintStream())){
                    return;
                }
            } catch (IOException e) {
                environment.getPrintStream().printf("IOException... Incorrect input!\n", 2-i);
                if(forcedQuit(i, environment.getPrintStream())){
                    return;
                }
            }

        }
    }


    @Override
    public String getName() {
        return "add_one_by_one";
    }

    @Override
    public String getDescription() {
        return "add_one_by_one : add a new elements to the collection. ( you need to write dots one by one )";
    }
}
