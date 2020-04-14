package de.netempire;

import de.netempire.classes.Fork;
import de.netempire.classes.Philosopher;
import de.netempire.logger.MyLogger;
import de.netempire.logger.ResultLogger;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class PhilosophersDesk {

    private static String report;
    static Fork fork1 = new Fork();
    static Fork fork2 = new Fork();
    static Fork fork3 = new Fork();
    static Fork fork4 = new Fork();
    static Fork fork5 = new Fork();
    static Philosopher platon = new Philosopher("Platon", 1, fork1, fork2);
    static Philosopher aristoteles = new Philosopher("Aristoteles",2, fork2, fork3);
    static Philosopher herder = new Philosopher("Herder", 3,fork3, fork4);
    static Philosopher fichte = new Philosopher("Fichte", 4,fork4, fork5);
    static Philosopher schlegel = new Philosopher("Schlegel",5, fork5, fork1);
    static Thread platonThread = new Thread(platon);
    static Thread aristotelesThread = new Thread(aristoteles);
    static Thread schlegelThread = new Thread(schlegel);
    static Thread fichteThread = new Thread(fichte);
    static Thread herderThread = new Thread(herder);
    static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    static Thread controllerThread = new Thread();
    static Philosopher[] philosophers = new Philosopher[5];
    static Runnable controller;
    static Date start = Calendar.getInstance().getTime();

    public static void main(String[] args) {
        PhilosophersDesk.startProcess();
    }

    private static void startProcess() {
        initialize();
        start();
    }

    public static void initialize(){
        platon.setEatingTime(750);
        aristoteles.setEatingTime(1000);
        herder.setEatingTime(300);
        fichte.setEatingTime(1500);
        schlegel.setEatingTime(500);

        philosophers = new Philosopher[]{platon, aristoteles, herder, fichte, schlegel};

        controller = () -> {
            if(!platonThread.isAlive() && !herderThread.isAlive() && !aristotelesThread.isAlive() && !fichteThread.isAlive() && !schlegelThread.isAlive()){
                platon.stop();
                herder.stop();
                platon.stop();
                aristoteles.stop();
                schlegel.stop();
                executor.shutdown();
                System.out.println("Der Abend wird beendet.");
                ResultLogger.log("Die Philosophen haben " + computeDuration(start, Calendar.getInstance().getTime()) + " Sekunden zusammen am Tisch gesessen.");
            }
            if (philosophers[0].state.equals("hungry") && philosophers[1].state.equals("hungry") && philosophers[2].state.equals("hungry") && philosophers[3].state.equals("hungry") && philosophers[4].state.equals("hungry") ) {
                System.out.println("Es haben alle Philosophen hunger!");
                try {
                    MyLogger.printOut(getReport() + " legt seine Gabeln wieder auf den Tisch.");
                    Optional<Philosopher> lastPhiloso = Arrays.stream(philosophers).filter(p -> p.name.equals(getReport())).findFirst();
                    int idLastPhiloso = lastPhiloso.map(philosopher -> philosopher.id - 1).orElse(0);
                    if (Arrays.asList(philosophers).get(idLastPhiloso).right.isTaken()) {
                        Arrays.asList(philosophers).get(idLastPhiloso).right.put();
                        while (philosophers[0].state.equals("hungry") && philosophers[1].state.equals("hungry") && philosophers[2].state.equals("hungry") && philosophers[3].state.equals("hungry") && philosophers[4].state.equals("hungry")) {
                            sleep(100);
                        }
                        while (Arrays.asList(philosophers).get(idLastPhiloso).right.getId() != -1) {
                            sleep(10);
                        }
                        Arrays.asList(philosophers).get(idLastPhiloso).right.get();
                    } else {
                        Arrays.asList(philosophers).get(idLastPhiloso).left.put();
                        while (philosophers[0].state.equals("hungry") && philosophers[1].state.equals("hungry") && philosophers[2].state.equals("hungry") && philosophers[3].state.equals("hungry") && philosophers[4].state.equals("hungry")) {
                            sleep(100);
                        }
                        while (Arrays.asList(philosophers).get(idLastPhiloso).left.getId() != -1) {
                            sleep(10);
                        }
                        Arrays.asList(philosophers).get(idLastPhiloso).left.get();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static void start(){
        platonThread.start();
        aristotelesThread.start();
        schlegelThread.start();
        fichteThread.start();
        herderThread.start();
        executor.scheduleAtFixedRate(controller, 0, 4, TimeUnit.SECONDS);
    }

    public static int computeDuration(Date to, Date from) {
        long difference = from.getTime() - to.getTime();
        return (int) (difference/1000);
    }

    public static String getReport() {
        return report;
    }

    public static void setReport(String report) {
        PhilosophersDesk.report = report;
    }
}