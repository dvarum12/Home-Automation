package HOME;
/**
 * Created by Kentora on 20-03-14.
 */
class ForkReturnCheck {
    private int forks = 0;
    private int returns = 0;

    public void addFork(){
        forks++;
    }

    public void addReturn(){
        returns++;
    }

    public int getReturn(){
        return returns;
    }

    public boolean closed(){
        return forks == returns;
    }
}

