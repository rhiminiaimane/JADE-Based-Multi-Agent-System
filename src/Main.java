import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.ContainerController;

public class Main {
    public static void main(String[] args) {
        jade.core.Runtime rt = jade.core.Runtime.instance();
        Profile p = new ProfileImpl();
        ContainerController cc = rt.createMainContainer(p);

        try {
            cc.createNewAgent("patient", "agents.PatientAgent", null).start();
            cc.createNewAgent("system", "agents.SystemAgent", null).start();
            cc.createNewAgent("medecin", "agents.MedecinAgent", null).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}