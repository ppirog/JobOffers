package joboffers.domain.offer;

class NotFoundInDatabaseException extends RuntimeException {
    public NotFoundInDatabaseException(String message) {
        super(message);
    }
}
