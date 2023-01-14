package Commands;

import util.Environment;

public class Info implements ICommand{
    @Override
    public void execute(Environment environment, String message) {
        environment.getPrintStream().println("" +
                "My Function is: f(x,y) = sinh (2x+y)/100, [4,90]x[-51,89].\n" +
                "At first, you need to create polygon. You can use 'add_one_by_one' to do it.\n" +
                "Then, you can modify, check  it or clear it. 'remove_by_id', 'clear', 'show'.\n" +
                "After that, you can enter 'integrate' to see the result of the integral sums and errors for some partition of the bar.\n" +
                "Now we've also added an opportunity to integrate with 'Monte Carlo' method: 'integrate_monte_carlo'." +
                "Also, you can print graph of dependency error from N. 'monte_carlo_error_graph'\n");
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
