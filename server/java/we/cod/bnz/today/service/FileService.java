package we.cod.bnz.today.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import we.cod.bnz.today.common.exception.ResourceNotFoundException;
import we.cod.bnz.today.dto.response.file.ResFileDownloadDTO;
import we.cod.bnz.today.dto.response.file.ResFileUploadDTO;
import we.cod.bnz.today.dto.response.file.ResThumbnailDTO;
import we.cod.bnz.today.entity.FileEntity;
import we.cod.bnz.today.entity.Thumbnail;
import we.cod.bnz.today.entity.Today;
import we.cod.bnz.today.repository.FileRepository;
import we.cod.bnz.today.repository.ThumbnailRepository;
import we.cod.bnz.today.repository.TodayRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FileService {

    private final TodayRepository todayRepository;
    private final FileRepository fileRepository;
    private final ThumbnailRepository thumbnailRepository;

    @Value("${project.folderPath}")
    private String FOLDER_PATH;

    public List<ResFileUploadDTO> upload(Long todayId, List<MultipartFile> multipartFiles) throws IOException {
        // 게시글 찾기
        Today today = todayRepository.findById(todayId).orElseThrow(
                () -> new ResourceNotFoundException("Today", "Tdoay Id", String.valueOf(todayId))
        );
        List<FileEntity> fileEntity = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            // get origin file name
            String fileName = multipartFile.getOriginalFilename();

            // random name generation of image while uploading to store in folder
            String randomId = UUID.randomUUID().toString();

            // create save File name : ex) POST_boardID_randomID.확장자
            String filePath =
                    "POST_" + today.getId() + "_" + randomId.concat(fileName.substring(fileName.indexOf(".")));

            // File.separator : OS에 따른 구분자
            String fileResourcePath = FOLDER_PATH + File.separator + filePath;

            // create folder if not created
            File f = new File(FOLDER_PATH);
            if (!f.exists()) {
                f.mkdir();
            }

            // file copy in folder
            Files.copy(multipartFile.getInputStream(), Paths.get(fileResourcePath));

            // create File Entity & 연관관게 매핑
            FileEntity saveFile = FileEntity.builder()
                    .originFileName(multipartFile.getOriginalFilename())
                    .filePath(filePath)
                    .fileType(multipartFile.getContentType())
                    .build();
            saveFile.setMappingToday(today);
            // File Entity 저장 및 DTO로 변환 전송

            fileEntity.add(fileRepository.save(saveFile));
        }
        List<ResFileUploadDTO> dtos = fileEntity.stream()
                .map(ResFileUploadDTO::fromEntity)
                .collect(Collectors.toList());

        return dtos;
    }

    public ResFileDownloadDTO download(Long fileId) throws IOException {
        FileEntity file = fileRepository.findById(fileId).orElseThrow(
                () -> new FileNotFoundException()
        );
        String filePath = FOLDER_PATH + file.getFilePath();
        String contentType = determineContentType(file.getFileType());
        byte[] content = Files.readAllBytes(new File(filePath).toPath());
        return ResFileDownloadDTO.fromFileResource(file, contentType, content);
    }

    public void delete(Long fileId) {
        FileEntity file = fileRepository.findById(fileId).orElseThrow(
                () -> new ResourceNotFoundException("File", "File Id", String.valueOf(fileId))
        );

        // local 파일을 삭제
        String filePath = FOLDER_PATH + File.separator + file.getFilePath();
        File physicalFile = new File(filePath);
        if (physicalFile.exists()) {
            physicalFile.delete();
        }
        fileRepository.delete(file);
    }

    private String determineContentType(String contentType) {
        // ContentType에 따라 MediaType 결정
        switch (contentType) {
            case "image/png":
                return MediaType.IMAGE_PNG_VALUE;
            case "image/jpeg":
                return MediaType.IMAGE_JPEG_VALUE;
            case "text/plain":
                return MediaType.TEXT_PLAIN_VALUE;
            default:
                return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }
    public ResThumbnailDTO uploadThumbnail(Long todayId, MultipartFile thumbnail) throws IOException {
        // 게시글 찾기
        Today today = todayRepository.findById(todayId).orElseThrow(
                () -> new ResourceNotFoundException("Today", "Today Id", String.valueOf(todayId))
        );

        // get origin file name
        String fileName = thumbnail.getOriginalFilename();

        // random name generation of image while uploading to store in folder
        String randomId = UUID.randomUUID().toString();

        // create save File name : ex) POST_boardID_randomID.확장자
        String filePath = "POST_" + today.getId() + "_" + randomId.concat(fileName.substring(fileName.indexOf(".")));

        // File.separator : OS에 따른 구분자
        String fileResourcePath = FOLDER_PATH + File.separator + filePath;

        // create folder if not created
        File f = new File(FOLDER_PATH);
        if (!f.exists()) {
            f.mkdir();
        }
        // 썸네일 경로를 보드 엔티티에 저장
        today.setThumbnailPath(filePath);

        // 보드 엔티티 업데이트
        todayRepository.save(today);

        // file copy in folder
        Files.copy(thumbnail.getInputStream(), Paths.get(fileResourcePath));

        // create Thumbnail Entity & 연관관게 매핑
        Thumbnail saveThumbnail = Thumbnail.builder()
                .thumbnailOriginFileName(thumbnail.getOriginalFilename())
                .thumbnailFilePath(filePath)
                .thumbnailFileType(thumbnail.getContentType())
//                .today(today)
                .build();

        // Thumbnail Entity 저장 및 DTO로 변환 전송
        Thumbnail savedThumbnail = thumbnailRepository.save(saveThumbnail);

        return ResThumbnailDTO.fromEntity(savedThumbnail);
    }
}
