import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class MedecinAgent extends Agent {
    protected void setup() {
        System.out.println("MedecinAgent " + getAID().getName() + " est prêt.");
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null && msg.getPerformative() == ACLMessage.REQUEST) {
                    System.out.println("Demande reçue: " + msg.getContent());
                    // Répondre avec les créneaux disponibles
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.PROPOSE);
                    reply.setContent("Créneaux disponibles: Lundi 11h, Mercredi 14h");
                    send(reply);
                } else {
                    block();
                }
            }
        });
    }
}