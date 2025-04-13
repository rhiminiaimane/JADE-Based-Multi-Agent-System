import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class PatientAgent extends Agent {
    protected void setup() {
        System.out.println("PatientAgent " + getAID().getName() + " est prêt.");
        // Ajouter un délai avant d'envoyer la demande
        try {
            Thread.sleep(5000); // Attendre 5 secondes pour laisser le Sniffer démarrer
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Envoyer une demande au SystemAgent
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("SystemAgent", AID.ISLOCALNAME));
        msg.setContent("Demande rendez-vous: Cardiologie, Lundi 10h-12h");
        send(msg);

        // Recevoir la réponse
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage reply = receive();
                if (reply != null) {
                    if (reply.getPerformative() == ACLMessage.INFORM) {
                        System.out.println("Réponse reçue: " + reply.getContent());
                        // Simuler le choix d'un créneau
                        ACLMessage confirmation = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                        confirmation.addReceiver(new AID("SystemAgent", AID.ISLOCALNAME));
                        confirmation.setContent("Choix: Lundi 11h");
                        send(confirmation);
                    }
                } else {
                    block();
                }
            }
        });
    }
}