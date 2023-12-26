package netology.diplom.filestorage.exception;

public class InternalServerError extends RuntimeException {

    private long id;

    public InternalServerError(String msg, long id) {
        super(msg);
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
