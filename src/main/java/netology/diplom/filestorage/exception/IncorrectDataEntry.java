package netology.diplom.filestorage.exception;

public class IncorrectDataEntry extends RuntimeException {

    private long id;

    public IncorrectDataEntry(String msg, long id) {
        super(msg);
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
