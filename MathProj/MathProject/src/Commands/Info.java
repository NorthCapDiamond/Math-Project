package Commands;

import util.Environment;

public class Info implements ICommand{
    @Override
    public void execute(Environment environment, String message) {
        environment.getPrintStream().println("" +
                "My Function is: f(x,y) = sinh (2x+y)/100, [4,90]x[-51,89].\n" +
                "At first, you need to create polygon. You can use 'add_one_by_one' to do it\n" +
                "Then, you can modify, check  it or clear it. 'remove_by_id', 'clear', 'show'\n" +
                "After that, you can enter 'integrate' to see the result of the integral sums for some partition of the bar.\n");
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "info : returns main information about this work";
    }
}
