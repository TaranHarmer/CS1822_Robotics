public class State {
    private States payload;
    /*
     * States include S for Searching F for Found L for Lifted H for Home
     */
    
    public State() {
        this.payload = States.S;
    }

    public States getState() {
        return payload;
    }

    public void setState() {
        switch (payload) {
        case S:
            payload = States.F; //state found
            break;
        case F:
            payload = States.L;
            break;
        case L:
            payload = States.H;
            break;
        case H:
            payload = States.S; //state searching
        }
    }
    
}