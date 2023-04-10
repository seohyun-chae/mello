package pjt.trello.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pjt.trello.model.AttachVO;
import pjt.trello.service.AttachService;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/attach")
public class AttachController {
    @Autowired
    private AttachService attachService;
    @Value("${spring.mvc.servlet.multipart.location}")
    private String uploadPath;

    @PostMapping("/upload")
    public void uploadAttach(String cardCode, MultipartFile[] files){

        log.info("--------------cardCode:{}", cardCode);
        log.info("--------------files:{}", files[0]);

        List<AttachVO> attachList = new ArrayList<>();

        for(MultipartFile file: files) {
            // 파일 확장자 구하기
            String prefix = file.getOriginalFilename().
                    substring(file.getOriginalFilename().lastIndexOf(".")+1,
                            file.getOriginalFilename().length());

            // 서버에 저장할 파일명+확장자 (예: 99cxdf980.jpg)
            String attachName = UUID.randomUUID().toString() +"."+ prefix;

            // 파일 경로 (예: C:/TrelloUpload/99cxdf980.jpg)
            String pathName = uploadPath + attachName;
            File dest = new File(pathName);

            // 파일 업로드
            // 폴더 유무 체크, 업로드 중간에 오류날 시 체크하기
            try {
                file.transferTo(dest);
            } catch (IllegalStateException | IOException e){
                log.error(e.getMessage());
            }

            // 업로드한 파일 정보 저장
            AttachVO attachVO = new AttachVO();
            attachVO.setAttachLoc(pathName);
            attachVO.setOrgName(file.getOriginalFilename());
            attachVO.setAttachName(attachName);
            attachVO.setCardCode(cardCode);

            attachList.add(attachVO);
        } //end of for
        log.info("--------------attachList:{}", attachList);
        attachService.uploadAttach(attachList);
    }

    @GetMapping("/list")
    public ResponseEntity<List<AttachVO>> getAttachList(@RequestParam String cardCode) {
        log.info("--------------cardCode:{}", cardCode);
        List<AttachVO> attList = attachService.getAttachList(cardCode);

        return ResponseEntity.ok(attList);
    }

    @GetMapping("/att/download")
    public void downloadAttach(@RequestParam String attachName, HttpServletResponse response) throws IOException {
        AttachVO attach = attachService.selectedFileInfo(attachName);
        if(ObjectUtils.isEmpty(attach) == false) {
            String fileName = attach.getAttachName();
            log.info("--------------fileName:{}", fileName);

            byte[] files = FileUtils.readFileToByteArray(new File(attach.getAttachLoc()));

            response.setContentType("application/octet-stream");
            response.setContentLength(files.length);
            response.setHeader("Content-Disposition","attachment; fileName=\""+ URLEncoder.encode(fileName,"UTF-8")+"\";");

            response.getOutputStream().write(files);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }

    }

    @PatchMapping("/delete")
    public void deleteAttach(@RequestBody AttachVO attachVO){
        String attachName =attachVO.getAttachName();
        log.info("--------------attachName:{}", attachName);
        attachService.deleteAttach(attachName);
    }

}
