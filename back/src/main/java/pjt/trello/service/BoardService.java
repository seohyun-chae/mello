package pjt.trello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.*;
import pjt.trello.model.*;
import pjt.trello.model.redis.BoardInfoDTO;
import pjt.trello.model.redis.CardDTO;
import pjt.trello.model.redis.LstDTO;
import pjt.trello.repository.BoardDAO;

import java.util.*;

@Service
public class BoardService {
    @Autowired
    private BoardDAO boardDAO;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 보드 목록 조회
     */
    public List<BoardVO> getBrdList(String userId) { return boardDAO.getBrdList(userId); }

    // 레디스
    public Set<String> getBrdListR(String userId){
        SetOperations<String, String> setOperations = stringRedisTemplate.opsForSet();
        String key = "user:"+userId+":brds";
        Set<String> brds = setOperations.members(key);

        System.out.println(brds);
        return brds;

//        HashOperations<String, Object,Object> hashOperations = stringRedisTemplate.opsForHash();
//        String key = "brd*"+userId+"";
//        Map<Object, Object> brds = hashOperations.entries(key);
//
//        System.out.println(brds.get("brd_name"));
    }

    /**
     * 보드 추가
     */
    @Transactional
    public void addBrd(BoardDTO boardDTO){
        /* 보드 코드 자동 부여 */
        int length = 8; // 길이 설정
        boolean useLetters = true; // 문자 사용 여부
        boolean useNumbers = true; // 숫자 사용 여부
        String newCode = RandomStringUtils.random(length, useLetters, useNumbers);
        boardDTO.setBrdCode(newCode);

        boardDAO.addBrd(boardDTO);
    }

    public void addBrdR(BoardDTO boardDTO){
        String brdName = boardDTO.getBrdName();
        String userId = boardDTO.getUserId();
        /* 보드 코드 자동 부여 */
        int length = 8; // 길이 설정
        boolean useLetters = true; // 문자 사용 여부
        boolean useNumbers = true; // 숫자 사용 여부
        String newCode = RandomStringUtils.random(length, useLetters, useNumbers);

        String key = "user:"+userId+":brds";
        String mem = newCode+":"+brdName;

//        HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();
//        Map<String, Object> map = new HashMap<>();
//        map.put("brd_name",brdName);
//        map.put("del_yn","n");
//
//        hashOperations.putAll(key,map);
        SetOperations<String, String> setOperations = stringRedisTemplate.opsForSet();
        setOperations.add(key,mem);
    }

    /**
     * 보드 삭제
     */
    @Transactional
    public void deleteBoard(String boardCode) { boardDAO.deleteBoard(boardCode); }

    public void deleteBoardR(BoardDTO boardDTO){
        String key = "user:"+boardDTO.getUserId()+":brds";
        String mem = boardDTO.getBrdCode()+":"+boardDTO.getBrdName();
        System.out.println(mem);
        SetOperations<String, String> setOperations = stringRedisTemplate.opsForSet();
        Long i = setOperations.remove(key,mem);
        System.out.println(i);
        //i가 0이 아니면 성공
        //보드 코드 받아서 리스트, 카드도 삭제
    }

    /**
     * 보드 데이터(카드, 리스트) 조회
     */
    public List<BoardDetailDTO> getBrdInfo(String brdCode){ return boardDAO.getBrdInfo(brdCode); }

    //레디스
    public BoardInfoDTO getBrdInfoR(String brdCode){
        BoardInfoDTO boardInfoDTO = new BoardInfoDTO();
        boardInfoDTO.setBrdCode(brdCode);

        List<LstDTO> lst = new ArrayList<>();

        ZSetOperations<String, String> ZsetOperations = stringRedisTemplate.opsForZSet();
        String key = "brd:"+brdCode+":lists";
        Set<String> listSet = ZsetOperations.range(key,0,-1); // 해당 보드의 리스트 데이터
        ArrayList<String> lists = new ArrayList<>(listSet); // set 데이터를 ArrayList로 변환

        for(int i = 0; i < lists.size(); i++){
            LstDTO tmpLst = new LstDTO();
            List<CardDTO> card = new ArrayList<>();

            //LstDTO 데이터 세팅
            String tmp = lists.get(i); // listCode:listName
            String tmpListCode = tmp.substring(0,tmp.indexOf(':'));
            String tmpListName = tmp.substring(tmp.indexOf(':')+1);
            tmpLst.setListCode(tmpListCode);
            tmpLst.setListName(tmpListName);
            tmpLst.setBrdCode(brdCode);
            System.out.println(i+"-------------------------");
            System.out.println(i+"번째 리스트 코드="+tmpListCode);
            System.out.println(i+"번째 리스트 이름="+tmpListName);

            // CardDTO 데이터 세팅
            String cardKey = "list:"+tmpListCode+":cards";
            Set<String> cardSet = ZsetOperations.range(cardKey,0,-1); // 해당 리스트의 카드 데이터
            ArrayList<String> cards = new ArrayList<>(cardSet); // set 데이터를 ArrayList로 변환
//            System.out.println(i+"번째 리스트의 카드= "+cards);
            if(cards.isEmpty()){
                tmpLst.setCard(null);
            }else{
                for(int j = 0; j < cards.size(); j++){
                    CardDTO tmpCrd = new CardDTO();
                    String tmpCard = cards.get(j); // cardCode:cardName
                    String tmpCdCode = tmpCard.substring(0,tmpCard.indexOf(':'));
                    String tmpCdName = tmpCard.substring(tmpCard.indexOf(':')+1);
                    tmpCrd.setCardCode(tmpCdCode);
                    tmpCrd.setCardTitle(tmpCdName);
                    tmpCrd.setListCode(tmpListCode);
                    card.add(j,tmpCrd);
                }
            }
            tmpLst.setCard(card);
            lst.add(i, tmpLst);
        }
        boardInfoDTO.setList(lst);
        return boardInfoDTO;
    }

    /**
     * 리스트 추가
     */
    @Transactional
    public void insertList(LstVO lstVO) {
        /* 리스트 코드 자동 부여 */
        int length = 8; // 길이 설정
        boolean useLetters = true; // 문자 사용 여부
        boolean useNumbers = true; // 숫자 사용 여부
        String newLstCode = RandomStringUtils.random(length, useLetters, useNumbers);
        lstVO.setListCode(newLstCode);

        boardDAO.insertList(lstVO);
    }

    public void insertListR(LstDTO lstDTO){
        /* 리스트 코드 자동 부여 */
        int length = 8; // 길이 설정
        boolean useLetters = true; // 문자 사용 여부
        boolean useNumbers = true; // 숫자 사용 여부
        String newLstCode = RandomStringUtils.random(length, useLetters, useNumbers);

        ZSetOperations<String, String> ZsetOperations = stringRedisTemplate.opsForZSet();
        String key = "brd:"+lstDTO.getBrdCode()+":lists";
        String value = newLstCode+":"+lstDTO.getListName();

        System.out.println(key+" 리스트 = "+value);
        ZsetOperations.add(key, value, lstDTO.getListIdx());
    }

    /**
     * 리스트 정렬 변경
     */
    @Transactional
    public void updateListSort(SortDTO sortDTO) { boardDAO.updateListSort(sortDTO); }

    public void updateListSortR(BoardInfoDTO boardInfoDTO){
        ZSetOperations<String, String> ZsetOperations = stringRedisTemplate.opsForZSet();
        String key = "brd:"+boardInfoDTO.getBrdCode()+":lists";

        for(int i = 0; i<boardInfoDTO.getList().size(); i++){
            String listCode = boardInfoDTO.getList().get(i).getListCode();
            String listName = boardInfoDTO.getList().get(i).getListName();
            String value = listCode+":"+listName;
            System.out.println(i+"번째 add 값 = "+value);
            double j = i;
            ZsetOperations.add(key,value,j);
            //null 반환되면 처리
        }
        System.out.println("결과 확인 = "+ZsetOperations.range(key,0,-1));

    }

    /**
     * 리스트 이름 변경
     */
    @Transactional
    public void renameList(SortDTO sortDTO) { boardDAO.renameList(sortDTO); }

    /**
     * 리스트 삭제
     */
    @Transactional
    public void deleteList(String listCode) { boardDAO.deleteList(listCode); }

    public void deleteListR(LstDTO lstDTO){
        ZSetOperations<String, String> ZsetOperations = stringRedisTemplate.opsForZSet();
        String lKey = "list:"+lstDTO.getListCode()+":cards";
        if(lstDTO.getCard() != null){
            ZsetOperations.removeRange(lKey,0,-1);
        }

        String key = "brd:"+lstDTO.getBrdCode()+":lists";
        String value = lstDTO.getListCode()+":"+lstDTO.getListName();
        System.out.println("삭제할 리스트 = "+value);
        ZsetOperations.remove(key, value);

    }

    /**
     * 카드 추가
     */
    @Transactional
    public void insertCard(CardVO cardVO) {
        /* 카드 코드 자동 부여 */
        int length = 8; // 길이 설정
        boolean useLetters = true; // 문자 사용 여부
        boolean useNumbers = true; // 숫자 사용 여부
        String newCrdCode = RandomStringUtils.random(length, useLetters, useNumbers);

        cardVO.setCardCode(newCrdCode);

        boardDAO.insertCard(cardVO);
    }
    // 레디스
    public void insertCardR(CardDTO cardDTO){
        /* 카드 코드 자동 부여 */
        int length = 8; // 길이 설정
        boolean useLetters = true; // 문자 사용 여부
        boolean useNumbers = true; // 숫자 사용 여부
        String newCrdCode = RandomStringUtils.random(length, useLetters, useNumbers);

        ZSetOperations<String, String> ZsetOperations = stringRedisTemplate.opsForZSet();
        String key = "list:"+cardDTO.getListCode()+":cards";
        String value = newCrdCode+":"+cardDTO.getCardTitle();

        System.out.println(key+"카드="+value);
        ZsetOperations.add(key, value, cardDTO.getCardIdx());
        //반환값 null이면 예외처리

    }

    /**
     * 카드 정렬 변경
     */
    @Transactional
    public void updateCardSort(SortDTO sortDTO) { boardDAO.updateCardSort(sortDTO); }

    /**
     * 카드 삭제
     */
    @Transactional
    public void deleteCard(String cardCode) { boardDAO.deleteCard(cardCode); }

    public void deleteCardR(CardDTO cardDTO){
        ZSetOperations<String, String> ZsetOperations = stringRedisTemplate.opsForZSet();
        String key = "list:"+cardDTO.getListCode()+":cards";
        String value = cardDTO.getCardCode()+":"+cardDTO.getCardTitle();

        ZsetOperations.remove(key, value);
    }

}
