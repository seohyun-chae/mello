package pjt.trello.model.redis;

import lombok.Data;

import java.util.List;

@Data
public class LstDTO {
    private String listCode;
    private String listName;

    private double listIdx;
    private List<CardDTO> card;

    private String brdCode;
}
