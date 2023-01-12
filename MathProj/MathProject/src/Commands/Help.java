package Commands;

import util.Environment;

public class Help implements ICommand{
    @Override
    public void execute(Environment environment, String message) {
        environment.getPrintStream().println("\nhelp : Display help for available commands.\n" +
                "info : returns main information about this work" +
                "add_one_by_one : add a new elements to the collection. ( you need to write dots one by one )\n" +
                "integrate : Calculates the integral sums for some partition of the bar" +
                "clean : clear the collection.\n" +
                "exit : exit the program. (without saving to file)\n" +
                "remove_by_id id : Remove an element from the collection by its id.\n" +
                "show : print to standard output all elements of the collection in string representation.\n" +
                "rick : NOOOOOOOOOOO!\n");
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "help : Display help for available commands.";
    }
}
