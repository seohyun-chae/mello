package pjt.trello.model;

import lombok.Data;
//import javax.validation.constraints.NotNull;

/**
 * tb_list 테이블 모델
 */
@Data
public class LstVO {
    /** 리스트 시퀀스 - 자동 부여 */
    private int listSeq;

    /** 리스트 코드 - 자동 부여 */
    private String listCode;

    /** 리스트 이름 */
    private String listName;

    /** 리스트 인덱스 */
    private int listIdx;

    /** 보드 코드 */
    private String brdCode;

}
