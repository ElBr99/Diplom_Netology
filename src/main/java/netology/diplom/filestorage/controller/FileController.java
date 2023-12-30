package netology.diplom.filestorage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import netology.diplom.filestorage.dto.FileDto;
import netology.diplom.filestorage.dto.UpdateRequestDto;
import netology.diplom.filestorage.entity.File;
import netology.diplom.filestorage.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/file")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Void> addFile(@NotNull @RequestParam("file") MultipartFile file, @RequestParam("filename") String fileName) {
        log.info("Файл для загрузки на сервер: {}", fileName);
        fileService.addFile(file, fileName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/file")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<byte[]> getFile(@RequestParam("filename") String fileName) {
        log.info("Запрос файла для скачивания с сервера: {}", fileName);
        File file = fileService.getFile(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFileName() + "\"")
                .body(file.getFileData());
    }

    @PutMapping("/file")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Void> renameFile(@RequestParam("filename") String fileName, @Valid @RequestBody UpdateRequestDto body) {
        log.info("Запрос файла на сервере для переименования: {}", fileName);
        fileService.renameFile(fileName, body);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/file")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Void> deleteFile(@RequestParam("filename") String fileName) {
        log.info("Запрос файла для удаления на сервере: {}", fileName);
        fileService.deleteFile(fileName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<List<FileDto>> getAllFiles(@Min(1) @RequestParam int limit) {
        log.info("Запрос для получения списка файлов. Лимит: {}", limit);
        var fileDtoList = fileService.getAllFiles(limit)
                .stream()
                .map(FileController::mapFile)
                .collect(Collectors.toList());

        return new ResponseEntity<>(fileDtoList, HttpStatus.OK);
    }

    private static FileDto mapFile(File file) {
        return FileDto.builder()
                .filename(file.getFileName())
                .size(file.getSize())
                .build();
    }
}
