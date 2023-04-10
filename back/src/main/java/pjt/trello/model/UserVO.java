package pjt.trello.model;
import lombok.Data;

/**
 * tb_user 테이블 모델
 */
@Data
public class UserVO {
    private int userSeq;
    private String userId;
    private String password;
    private String creDt;
}
