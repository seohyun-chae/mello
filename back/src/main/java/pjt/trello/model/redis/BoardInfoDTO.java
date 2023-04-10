package pjt.trello.model.redis;

import lombok.Data;
import pjt.trello.model.redis.LstDTO;

import java.util.List;

@Data
public class BoardInfoDTO {
    private String brdCode;
    private List<LstDTO> list;

}
