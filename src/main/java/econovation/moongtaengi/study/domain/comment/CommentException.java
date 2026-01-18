package econovation.moongtaengi.study.domain.comment;

import econovation.moongtaengi.global.exception.BusinessException;

public class CommentException extends BusinessException {

  public CommentException(CommentErrorCode errorCode) {
    super(errorCode);
  }

  public CommentException(CommentErrorCode errorCode, Object... args) {
    super(errorCode, args);
  }
}
