package pjt.trello.model;

import lombok.Data;

@Data
public class BoardDetailDTO {
    /** 보드 */
    private int brdSeq;
    private String brdCode;
    private String brdName;

    /** 리스트 */
    private String listCode;
    private String listName;
    private int listIdx;

    /** 카드 */
    private String cardCode;
    private String cardTitle;
    private int cardIdx;
    private String imgCover;

}
