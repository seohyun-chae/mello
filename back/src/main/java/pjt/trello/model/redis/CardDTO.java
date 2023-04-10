package pjt.trello.model.redis;

import lombok.Data;

@Data
public class CardDTO {
    private String cardCode;
    private String cardTitle;

    private double cardIdx;

    private String listCode;

}
