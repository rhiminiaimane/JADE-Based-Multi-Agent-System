import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;

public class MedecinAgent extends Agent {
    protected void setup() {
        System.out.println("Médecin en service");

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null && msg.getContent().startsWith("PROPOSITION_RDV")) {
                    String disponibilites = msg.getContent().split(":")[1];
                    System.out.println("Disponibilités reçues: " + disponibilites);

                    // Simuler la validation d’un créneau (tu peux parser le JSON ici)
                    String rdvChoisi = "jeudi 15h"; // Par exemple

                    ACLMessage confirm = new ACLMessage(ACLMessage.CONFIRM);
                    confirm.addReceiver(new AID("system", AID.ISLOCALNAME));
                    confirm.setContent(rdvChoisi);
                    send(confirm);
                } else
                    block();
            }
        });
    }
}