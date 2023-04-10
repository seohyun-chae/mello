package pjt.trello.model;

import lombok.Data;

//import javax.validation.constraints.NotNull;

/**
 * tb_brd 테이블 모델
 */
@Data
public class BoardVO {
    /** 보드 시퀀스 - 자동 부여 */
    private int brdSeq;

    /** 보드 코드 */
    private String brdCode;

    /** 보드 이름 */
    private String brdName;

}
