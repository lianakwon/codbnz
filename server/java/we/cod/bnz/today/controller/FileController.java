package we.cod.bnz.today.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import we.cod.bnz.today.dto.response.file.ResFileDownloadDTO;
import we.cod.bnz.today.dto.response.file.ResFileUploadDTO;
import we.cod.bnz.today.dto.response.file.ResThumbnailDTO;
import we.cod.bnz.today.service.FileService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/today/{todayId}/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<List<ResFileUploadDTO>> upload (
            @PathVariable Long todayId,
            @RequestParam("file") List<MultipartFile> files) throws IOException {
        List<ResFileUploadDTO> saveFile = fileService.upload(todayId, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveFile);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download (
            @RequestParam("fileId") Long fileId) throws IOException {
        ResFileDownloadDTO downloadDto = fileService.download(fileId);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(downloadDto.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + downloadDto.getFilename() + "\"")
                .body(new ByteArrayResource(downloadDto.getContent()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Long> delete (
            @RequestParam("fileId") Long fileId) {
        fileService.delete(fileId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/thumbnail/upload")

    public ResponseEntity<ResThumbnailDTO> uploadThumbnail(
            @PathVariable Long todayId,
            @RequestParam("thumbnail") MultipartFile thumbnail) throws IOException {
        ResThumbnailDTO saveThumbnail = fileService.uploadThumbnail(todayId, thumbnail);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveThumbnail);
    }
}
