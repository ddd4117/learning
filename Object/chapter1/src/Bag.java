public class Bag {
    private Long amount;
    private Invitation invitation;
    private Ticket ticket;

    public Long hold(Ticket ticket){
        if(hasInvitaion()){
            setTicket(ticket);
            return 0L;
        } else{
            setTicket(ticket);
            minusAmount(ticket.getFee());
            return ticket.getFee();
        }
    }
    private boolean hasInvitaion(){
        return invitation != null;
    }

    public boolean hasTicket(){
        return ticket != null;
    }

    private void setTicket(Ticket ticket){
        this.ticket = ticket;
    }

    private void minusAmount(Long amount){
        this.amount -= amount;
    }

    public void plusAmount(Long amount){
        this.amount += amount;
    }

    public Bag(Long amount) {
        this(null, amount);
    }

    public Bag(Invitation invitation, Long amount) {
        this.amount = amount;
        this.invitation = invitation;
    }
}
