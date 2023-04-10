package pjt.trello.model;

import lombok.Data;

@Data
public class SortDTO {
    private String brdCode;
    private String listCode;

    private String listName;
    private int listIdx;
    private String cardCode;
    private int cardIdx;
}
