import controller.RequestController;
import model.DataStore;
import model.RequestService;
import view.ConsoleView;

public class Main {
    public static void main(String[] args) {
        try {
            DataStore dataStore = new DataStore();
            dataStore.loadSeedData();
            RequestService requestService = new RequestService(dataStore);
            RequestController controller = new RequestController(requestService);
            new ConsoleView().run(controller);
        } catch (Exception exception) {
            System.out.println("Cannot start program: " + exception.getMessage());
        }
    }
}
