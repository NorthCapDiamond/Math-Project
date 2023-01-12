package Commands;

import util.Environment;

public interface ICommand {
    void execute(Environment environment, String message);
    String getName();
    String getDescription();
}
