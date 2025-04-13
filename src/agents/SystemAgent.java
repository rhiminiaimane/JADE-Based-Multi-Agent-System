import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;

public class SystemAgent extends Agent {
    protected void setup() {
        System.out.println("SystemAgent actif");

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    if (msg.getContent().startsWith("DEMANDE_RDV")) {
                        String[] parts = msg.getContent().split(":");
                        String specialite = parts[1];
                        System.out.println("Specialité demandée: " + specialite);
                        String disponibilites = parts[2];

                        // Transmettre la demande au MedecinAgent concerné
                        ACLMessage medMsg = new ACLMessage(ACLMessage.REQUEST);
                        medMsg.addReceiver(new AID("medecin", AID.ISLOCALNAME));
                        medMsg.setContent("PROPOSITION_RDV:" + disponibilites);
                        send(medMsg);
                    } else if (msg.getPerformative() == ACLMessage.CONFIRM) {
                        // Transmettre confirmation au patient
                        ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                        reply.addReceiver(new AID("patient", AID.ISLOCALNAME));
                        reply.setContent("RDV_CONFIRMÉ:" + msg.getContent());
                        send(reply);
                    }
                } else
                    block();
            }
        });
    }
}