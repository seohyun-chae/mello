package pjt.trello.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pjt.trello.model.*;
import pjt.trello.model.redis.BoardInfoDTO;
import pjt.trello.model.redis.CardDTO;
import pjt.trello.model.redis.LstDTO;
import pjt.trello.service.BoardService;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/b")
public class BoardController {
    @Autowired
    private BoardService boardService;

    /**
     * 보드 목록 조회
     */
    @GetMapping("/boardList")
    public ResponseEntity<List<BoardVO>> getBoardList(@RequestParam String userId) {
        log.info("--------------userId:{}", userId);
        List<BoardVO> boardList = boardService.getBrdList(userId);

        return ResponseEntity.ok(boardList);
    }

    // 레디스
    @GetMapping("/boardListR")
    public ResponseEntity<Set<String>> getBoardListR(@RequestParam String userId) {
        log.info("--------------userId:{}", userId);
        Set<String> boardList = boardService.getBrdListR(userId);

        return ResponseEntity.ok(boardList);
    }

    /**
     * 보드 추가
     */
    @PostMapping("/board")
    public ResponseEntity<Void> addBrd(@RequestBody BoardDTO boardDTO) {
        log.info("--------------user ID :{}", boardDTO.getUserId());
        log.info("--------------new board name:{}", boardDTO.getBrdName());

        boardService.addBrd(boardDTO);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    // 레디스
    @PostMapping("/boardR")
    public ResponseEntity<Void> addBrdR(@RequestBody BoardDTO boardDTO){
        log.info("--------------user ID :{}", boardDTO.getUserId());
        log.info("--------------new board name:{}", boardDTO.getBrdName());

        boardService.addBrdR(boardDTO);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     * 보드 삭제
     */
    @DeleteMapping("/board")
    public ResponseEntity<Void> deleteBoard(@RequestParam String brdCode) {
        log.info("--------------boardCode:{}", brdCode);
        boardService.deleteBoard(brdCode);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    // 레디스
    @PostMapping("/deleteBoard")
    public ResponseEntity<Void> deleteBoardR(@RequestBody BoardDTO boardDTO) {

        log.info("--------------delete board Code:{}", boardDTO.getBrdCode());
        log.info("--------------board Name:{}", boardDTO.getBrdName());
        log.info("--------------userId:{}", boardDTO.getUserId());
        boardService.deleteBoardR(boardDTO);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     * 보드 상세 데이터 (리스트, 카드) 조회
     */

    @GetMapping("/board")
    public ResponseEntity<List<BoardDetailDTO>> getBoard(@RequestParam String brdCode) {
        log.info("--------------brdCode:{}", brdCode);
        List<BoardDetailDTO> board = boardService.getBrdInfo(brdCode);

        return ResponseEntity.ok(board);
    }

    //레디스
    @GetMapping("/boardR")
    public ResponseEntity<BoardInfoDTO> getBoardInfo(@RequestParam String brdCode){
        log.info("--------------brdCode:{}", brdCode);
        BoardInfoDTO board = boardService.getBrdInfoR(brdCode);

        return ResponseEntity.ok(board);
    }

    /**
     * 리스트, 카드 추가
     */
    @PostMapping("/list")
    public ResponseEntity<Void> insertList(@RequestBody LstVO lstVO) {
        log.info("--------------listName:{}", lstVO.getListName());
        boardService.insertList(lstVO);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/card")
    public ResponseEntity<Void> insertCard(@RequestBody CardVO cardVO) {
        log.info("--------------cardTitle:{}", cardVO.getCardTitle());
        boardService.insertCard(cardVO);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    //레디스
    @PostMapping("/AddCard")
    public ResponseEntity<Void> insertCardR(@RequestBody CardDTO cardDTO){
        log.info("--------------cardTitle:{}", cardDTO.getCardTitle());
        boardService.insertCardR(cardDTO);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/AddList")
    public ResponseEntity<Void> insertListR(@RequestBody LstDTO lstDTO){
        log.info("--------------listName:{}", lstDTO.getListName());
        boardService.insertListR(lstDTO);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     * 리스트, 카드 순서 인덱스 수정
     */
    @PatchMapping("/list")
    public ResponseEntity<Void> updateListSort(@RequestBody SortDTO sortDto) {
        log.info("--------------listCode:{}", sortDto.getListCode());
        boardService.updateListSort(sortDto);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PatchMapping("/card")
    public ResponseEntity<Void> updateCardSort(@RequestBody SortDTO sortDto) {
        log.info("--------------ListCode:{}", sortDto.getListCode());
        log.info("--------------CardCode:{}", sortDto.getCardCode());
        boardService.updateCardSort(sortDto);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    //레디스
    @PostMapping("/sortList")
    public ResponseEntity<Void> updateListSort(@RequestBody BoardInfoDTO boardInfoDTO){
        log.info("--------------ListCode:{}", boardInfoDTO.getList().get(0).getListCode());
        boardService.updateListSortR(boardInfoDTO);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     * 리스트 이름 수정
     */
    @PatchMapping("/listName")
    public ResponseEntity<Void> renameList(@RequestBody SortDTO sortDto) {
        log.info("--------------ListCode:{}", sortDto.getListCode());
        boardService.renameList(sortDto);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     * 리스트, 카드 삭제
     */
    @DeleteMapping("/list")
    public ResponseEntity<Void> deleteList(@RequestParam String listCode) {
        log.info("--------------listCode:{}", listCode);
        boardService.deleteList(listCode);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/deleteList")
    public ResponseEntity<Void> deleteListR(@RequestBody LstDTO lstDTO){
        log.info("--------------cardCode:{}", lstDTO.getListCode());
        boardService.deleteListR(lstDTO);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @DeleteMapping("/card")
    public ResponseEntity<Void> deleteCard(@RequestParam String cardCode) {
        log.info("--------------cardCode:{}", cardCode);
        boardService.deleteCard(cardCode);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/deleteCard")
    public ResponseEntity<Void> deleteCardR(@RequestBody CardDTO cardDTO){
        log.info("--------------cardCode:{}", cardDTO.getCardCode());
        boardService.deleteCardR(cardDTO);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
