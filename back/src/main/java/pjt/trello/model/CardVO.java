package pjt.trello.model;

import lombok.Data;

/**
 * tb_card 테이블 모델
 */
@Data
public class CardVO {
    /** 카드 시퀀스 - 자동 부여 */
    private int cardSeq;

    /** 카드 코드 - 자동 부여 */
    private String cardCode;

    /** 카드 인덱스 */
    private int cardIdx;

    /** 카드 제목 */
    private String cardTitle;

    /** 카드 상세 */
    private String cardInfo;

    /** 리스트 코드 */
    private String listCode;
}
