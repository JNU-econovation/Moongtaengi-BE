package econovation.moongtaengi.global.exception;

public class AlreadyRegisteredException extends BusinessException {

    public AlreadyRegisteredException() {
        super(ErrorCode.ALREADY_REGISTERED);
    }
}
