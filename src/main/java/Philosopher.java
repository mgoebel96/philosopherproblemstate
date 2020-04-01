import java.util.Random;

import static java.lang.Thread.sleep;

public class Philosopher implements Runnable {

    String name;
    int id;
    Fork right, left;
    boolean rightF, leftF;
    private volatile boolean exit = false;
    String state;

    Random random = new Random();

    public Philosopher(String name, int id, Fork right, Fork left){
        this.name = name;
        this.id = id;
        this.right = right;
        this.left = left;
    }

    public void run() {
        int i = 100;
        while( i > 0 ) {
            try {
                // Philosopher is thinking
                Logger.printOut (name + " philosphiert.");
                state = "wait";
                sleep((int) (random.nextDouble()*1000));
                Logger.printOut (name + " hat Hunger.");
                PhilosophersDesk.report = name;
                // Philosopher is hungry
                state = "hungry";
                // taking right
                right.get();
                right.setId(id);
                // turn left (critical moment)
                sleep((int) (random.nextDouble()*1000));
                // taking left
                left.get();
                left.setId(id);
                setLeftF(true);
                while(!hasLeftFork() && !hasRightFork()) {
                    sleep(100);
                }
                state = "eating";
                Logger.printOut(name + " hat zwei Gabeln. Er kann essen.");
                // holding two forks -> can eat now
                sleep((int) (random.nextDouble() * 1000));
            } catch (InterruptedException e) {
                Logger.printOut (e.getMessage());
            }
            right.setId(-1);
            left.setId(-1);
            right.put();
            left.put();
        }
    }

    public boolean hasRightFork() {
        return rightF;
    }

    public void setRightF(boolean rightF) {
        this.rightF = rightF;
    }

    public boolean hasLeftFork() {
        return leftF;
    }

    public void setLeftF(boolean leftF) {
        this.leftF = leftF;
    }

    public void stop(){
        exit = true;
    }
}