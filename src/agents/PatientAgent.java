import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.AID;

public class PatientAgent extends Agent {
    protected void setup() {
        System.out.println("Patient prêt : " + getLocalName());

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("system", AID.ISLOCALNAME));
        msg.setContent("DEMANDE_RDV:cardiologie:mercredi 10h, jeudi 15h");
        send(msg);
    }
}