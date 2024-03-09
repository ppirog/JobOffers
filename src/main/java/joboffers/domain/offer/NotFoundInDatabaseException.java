package joboffers.domain.offer;

public class NotFoundInDatabaseException extends RuntimeException {
    public NotFoundInDatabaseException(String message) {
        super(message);
    }
}
