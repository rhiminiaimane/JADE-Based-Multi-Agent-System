import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.*;

public class Main {
    public static void main(String[] args) {
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        p.setParameter(Profile.MAIN_PORT, "1100");
        p.setParameter(Profile.GUI, "true");
        ContainerController cc = rt.createMainContainer(p);

        try {
            // Créer le Sniffer et surveiller les agents
            AgentController sniffer = cc.createNewAgent("sniffer1", "jade.tools.sniffer.Sniffer",
                    new String[] { "PatientAgent;MedecinAgent;SystemAgent" });
            sniffer.start();

            // Attendre que le Sniffer soit prêt (par sécurité)
            Thread.sleep(5000); // Délai de 2 secondes pour laisser le Sniffer s'initialiser

            // Créer et démarrer les agents
            AgentController patient = cc.createNewAgent("PatientAgent", "PatientAgent", null);
            AgentController medecin = cc.createNewAgent("MedecinAgent", "MedecinAgent", null);
            AgentController system = cc.createNewAgent("SystemAgent", "SystemAgent", null);

            patient.start();
            medecin.start();
            system.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}