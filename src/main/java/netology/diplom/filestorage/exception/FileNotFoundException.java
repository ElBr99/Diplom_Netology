package netology.diplom.filestorage.exception;

public class FileNotFoundException extends RuntimeException {

    private long id;

    public FileNotFoundException(String msg, long id) {
        super(msg);
        this.id = id;
    }

    public FileNotFoundException(String msg) {
        super(msg);
    }

    public long getId() {
        return id;
    }
}
