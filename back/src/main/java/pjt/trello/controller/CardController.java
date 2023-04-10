package pjt.trello.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pjt.trello.model.CardVO;
import pjt.trello.model.SearchDTO;
import pjt.trello.service.CardService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/c")
public class CardController {
    @Autowired
    private CardService cardService;

    /**
     * 카드 데이터 조회
     */
    @GetMapping("/{cardCode}")
    public ResponseEntity<List<CardVO>> getCard(@PathVariable String cardCode) {
        log.info("--------------cardCode:{}", cardCode);
        List<CardVO> card = cardService.getCardInfo(cardCode);

        return ResponseEntity.ok(card);
    }

    /**
     * 카드 정보 수정
     */
    @PatchMapping("/modify")
    public ResponseEntity<Void> updateCard(@RequestBody CardVO cardVO) {
        log.info("--------------cardCode:{}", cardVO.getCardCode());
        cardService.updateCard(cardVO);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     * 검색창 - 카드 조회
     */
    @GetMapping("/search")
    public ResponseEntity<List<SearchDTO>> searchCard(@RequestParam String userId, String searchWord) {
        log.info("--------------userId:{}", userId);
        List<SearchDTO> result = cardService.searchCard(userId, searchWord);

        return ResponseEntity.ok(result);
    }

}
