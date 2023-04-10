package pjt.trello.model;

import lombok.Data;

/**
 * tb_attach 모델
 */
@Data
public class AttachVO {
    /** 시퀀스 - 자동 부여 */
    private int attachSeq;
    /** 파일명 */
    private String attachName;
    /** 원래 파일명 */
    private String orgName;

    /** 파일 경로 */
    private String attachLoc;
    /** 파일 생성 날짜 */
    private String creDt;
    /** 카드 코드 */
    private String cardCode;
}
