package pjt.trello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pjt.trello.model.AttachVO;
import pjt.trello.repository.AttachDAO;

import java.util.List;

@Service
public class AttachService {
    @Autowired
    private AttachDAO attachDAO;

    public void uploadAttach(List<AttachVO> attachVO) { attachDAO.uploadAttach(attachVO); }

    public List<AttachVO> getAttachList(String cardCode) { return attachDAO.getAttachList(cardCode); }

    public AttachVO selectedFileInfo(String attachName){
        return attachDAO.selectedFileInfo(attachName);
    }

    public void deleteAttach(String attachName){ attachDAO.deleteAttach(attachName); }
}
