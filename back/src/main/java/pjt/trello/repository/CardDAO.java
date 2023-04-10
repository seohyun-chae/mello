package pjt.trello.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import pjt.trello.model.*;

import java.util.List;

@Mapper
public interface CardDAO {
    /* 카드 상세 정보 */
    public List<CardVO> getCardInfo (String cardCode);

    /* 카드 정보 수정 */
    public void updateCard (CardVO cardVO);

    /* 카드 검색 */
    public List<SearchDTO> searchCard(@Param("userId") String userId, @Param("searchWord") String searchWord);
}
