package Commands;

import Managers.CollectionManager;
import util.Environment;

public class Clean implements ICommand{
    @Override
    public void execute(Environment environment, String message) {
        CollectionManager collectionManager = environment.getCollectionManager();
        collectionManager.removeAllElements();
        environment.getPrintStream().println("Collection cleaned!");
    }

    @Override
    public String getName() {
        return "clean";
    }

    @Override
    public String getDescription() {
        return "clean : clear the collection";
    }
}
