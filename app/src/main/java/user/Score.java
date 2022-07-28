package user;

public class Score {
    private String serial_code;
    private String date;
    private int orientation;
    private int memory;
    private int attention;
    private int spacetime;
    private int execution;
    private int language;
    private int total;

    public Score() {}

    public Score(String serial_code, String date, int orientation, int memory, int attention, int spacetime, int execution, int language, int total) {
        this.serial_code = serial_code;
        this.date = date;
        this.orientation = orientation;
        this.memory = memory;
        this.attention = attention;
        this.spacetime = spacetime;
        this.execution = execution;
        this.language = language;
        this.total = total;
    }

    public String getSerial_code() {
        return serial_code;
    }

    public void setSerial_code(String serial_code) {
        this.serial_code = serial_code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public int getSpacetime() {
        return spacetime;
    }

    public void setSpacetime(int spacetime) {
        this.spacetime = spacetime;
    }

    public int getExecution() {
        return execution;
    }

    public void setExecution(int execution) {
        this.execution = execution;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}