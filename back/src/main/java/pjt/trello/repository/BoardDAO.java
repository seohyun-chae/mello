package pjt.trello.repository;

import org.apache.ibatis.annotations.Mapper;
import pjt.trello.model.*;

import java.util.List;

@Mapper
public interface BoardDAO {
    /* 메인화면 - 보드 목록 */
    public List<BoardVO> getBrdList(String userId);

    /* 메인화면 - 보드 추가 */
    public void addBrd(BoardDTO boardDTO);

    /* 메인화면 - 보드 삭제 */
    public void deleteBoard(String boardCode);

    /* 보드 상세 정보 */
    public List<BoardDetailDTO> getBrdInfo(String brdCode);
//    public List<BoardDetailDTO> selectList(String brdCode, String code) {
//        return getSqlSession().selectList("boardMapper.selectBrdInfo", brdCode);
//    }
    /* 보드에 리스트 추가 */
    public void insertList(LstVO lstVO);

    /* 리스트에 카드 추가 */
    public void insertCard(CardVO cardVO);

    /* 리스트 순서 업데이트 */
    public void updateListSort(SortDTO sortDTO);

    /* 카드 정렬 업데이트 */
    public void updateCardSort(SortDTO sortDTO);

    /* 리스트 이름 변경 */
    public void renameList(SortDTO sortDTO);

    /* 리스트 삭제 */
    public void deleteList(String listCode);

    /* 카드 삭제 */
    public void deleteCard(String cardCode);
}
