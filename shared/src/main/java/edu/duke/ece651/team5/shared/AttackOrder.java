package edu.duke.ece651.team5.shared;

public class AttackOrder extends BasicOrder {
    public AttackOrder(String source, String destination, int number, Unit type, String playerName) {
        super(source, destination, number, type, playerName);
    }


    @Override
    public void execute(RISKMap map) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }
}
