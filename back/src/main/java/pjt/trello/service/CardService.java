package pjt.trello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.*;
import pjt.trello.model.*;
import pjt.trello.repository.BoardDAO;
import pjt.trello.repository.CardDAO;

import java.util.List;

@Service
public class CardService {
    @Autowired
    private CardDAO cardDAO;

    public List<CardVO> getCardInfo(String cardCode) { return cardDAO.getCardInfo(cardCode); }

    @Transactional
    public void updateCard(CardVO cardVO) { cardDAO.updateCard(cardVO); }

    public List<SearchDTO> searchCard(String userId, String searchWord) {  return cardDAO.searchCard(userId, searchWord); }

}
