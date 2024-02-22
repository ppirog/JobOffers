package joboffers.domain.loginandregister;

class NotFoundInDatabaseException extends RuntimeException{
    public NotFoundInDatabaseException(String message) {
        super(message);
    }
}
