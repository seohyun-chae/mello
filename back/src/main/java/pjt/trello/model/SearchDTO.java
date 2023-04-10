package pjt.trello.model;

import lombok.Data;

@Data
public class SearchDTO {
    private String brdCode;
    private String brdName;
    private String listCode;
    private String listName;
    private String cardCode;
    private String cardTitle;
    private String userId;
}
