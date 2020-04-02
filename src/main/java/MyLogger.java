public class MyLogger {

    private MyLogger() {
        throw new IllegalStateException("Logger class");
    }

    public static synchronized void printOut(String printText){
        System.out.println(printText);
    }
}