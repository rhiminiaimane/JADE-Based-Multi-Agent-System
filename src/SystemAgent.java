import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;

public class SystemAgent extends Agent {
    protected void setup() {
        System.out.println("SystemAgent " + getAID().getName() + " est prêt.");
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    // Gérer la demande initiale du PatientAgent
                    if (msg.getPerformative() == ACLMessage.REQUEST) {
                        System.out.println("Demande reçue de " + msg.getSender().getName() + ": " + msg.getContent());
                        SequentialBehaviour seq = new SequentialBehaviour();

                        // Étape 1 : Transférer la demande au MedecinAgent
                        seq.addSubBehaviour(new OneShotBehaviour() {
                            public void action() {
                                ACLMessage forward = new ACLMessage(ACLMessage.REQUEST);
                                forward.addReceiver(new AID("MedecinAgent", AID.ISLOCALNAME));
                                forward.setContent(msg.getContent());
                                forward.setConversationId(msg.getConversationId());
                                send(forward);
                            }
                        });

                        // Étape 2 : Attendre la réponse du MedecinAgent
                        seq.addSubBehaviour(new OneShotBehaviour() {
                            public void action() {
                                ACLMessage reply = blockingReceive(5000);
                                if (reply != null && reply.getPerformative() == ACLMessage.PROPOSE) {
                                    System.out.println("Réponse du médecin: " + reply.getContent());
                                    ACLMessage response = new ACLMessage(ACLMessage.INFORM);
                                    response.addReceiver(msg.getSender());
                                    response.setContent(reply.getContent());
                                    response.setConversationId(msg.getConversationId());
                                    send(response);
                                }
                            }
                        });

                        myAgent.addBehaviour(seq);
                    }
                    // Gérer la confirmation du PatientAgent
                    else if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                        System.out.println(
                                "Confirmation reçue de " + msg.getSender().getName() + ": " + msg.getContent());
                        SequentialBehaviour confirmSeq = new SequentialBehaviour();

                        // Étape 1 : Informer le MedecinAgent du créneau choisi
                        confirmSeq.addSubBehaviour(new OneShotBehaviour() {
                            public void action() {
                                ACLMessage confirmMsg = new ACLMessage(ACLMessage.INFORM);
                                confirmMsg.addReceiver(new AID("MedecinAgent", AID.ISLOCALNAME));
                                confirmMsg.setContent("Créneau confirmé: " + msg.getContent());
                                confirmMsg.setConversationId(msg.getConversationId());
                                send(confirmMsg);
                            }
                        });

                        // Étape 2 : Confirmer au PatientAgent
                        confirmSeq.addSubBehaviour(new OneShotBehaviour() {
                            public void action() {
                                ACLMessage finalConfirm = new ACLMessage(ACLMessage.CONFIRM);
                                finalConfirm.addReceiver(msg.getSender());
                                finalConfirm.setContent("Rendez-vous confirmé: " + msg.getContent());
                                finalConfirm.setConversationId(msg.getConversationId());
                                send(finalConfirm);
                            }
                        });

                        myAgent.addBehaviour(confirmSeq);
                    }
                } else {
                    block();
                }
            }
        });
    }
}