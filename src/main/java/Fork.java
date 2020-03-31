class Fork {

    boolean taken;
    int id;

    synchronized void put() {
        // Fork is placed back on the table. -> status: not taken
        taken = false;
        notify();
    }

    synchronized void get() throws InterruptedException {
        // Fork is taken from the table. -> status: taken
        while (taken) {
            wait(); // wait until the fork is back on the table.
        }
        taken = true;
    }

    public boolean isTaken() {
        return taken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}