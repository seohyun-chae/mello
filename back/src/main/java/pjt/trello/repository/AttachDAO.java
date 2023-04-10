package pjt.trello.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pjt.trello.model.AttachVO;

import java.util.List;

@Mapper
public interface AttachDAO {
    public void uploadAttach(List<AttachVO> attachVO);

    public List<AttachVO> getAttachList(String cardCode);

    public AttachVO selectedFileInfo(@Param("attachName")String attachName);

    public void deleteAttach(String attachName);
}
