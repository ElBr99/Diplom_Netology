package netology.diplom.filestorage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import netology.diplom.filestorage.dto.FileDto;
import netology.diplom.filestorage.entity.File;
import netology.diplom.filestorage.exception.FileNotFoundException;
import netology.diplom.filestorage.dto.UpdateRequestDto;
import netology.diplom.filestorage.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    @Transactional
    public void addFile(MultipartFile file, String fileName) {
        String hash = null;
        byte[] fileData = null;
        try {
            hash = checksum(file);
            fileData = file.getBytes();
        } catch (NoSuchAlgorithmException | IOException e) {
            log.error(e.getMessage());
        }

        if (fileRepository.findFileByHash(hash).isPresent()) {
            log.warn("Файл с данным хэшом уже существует.");
            return;
        }

        File createdFile = File.builder()
                .fileName(fileName)
                .fileType(file.getContentType())
                .fileData(fileData)
                .hash(hash)
                .size(file.getSize())
                .build();

        fileRepository.save(createdFile);
        log.info("Файл создан = {}", createdFile);
    }

    private String checksum(MultipartFile file) throws NoSuchAlgorithmException, IOException {
        byte[] data = file.getBytes();
        byte[] hash = MessageDigest.getInstance("SHA-256").digest(data);
        return new BigInteger(1, hash).toString(16);
    }

    public File getFile(String fileName) {
        log.info("Поиск файла по названию = {}", fileName);
        return fileRepository.findFileByFileName(fileName)
                .orElseThrow(() -> new FileNotFoundException("Файл с именем: { " + fileName + " } не найден"));
    }

    @Transactional
    public void renameFile(String fileName, UpdateRequestDto fileBody) {
        File fileToRename = fileRepository.findFileByFileName(fileName)
                .orElseThrow(() -> new FileNotFoundException("Файл с именем: { " + fileName + " } не найден"));
        fileToRename.setFileName(fileBody.getFilename());
        fileRepository.save(fileToRename);
        log.info("Файл переименован = {}", fileName);
    }

    @Transactional
    public void deleteFile(String fileName) {
        log.info("Удаление файла с именем = {}", fileName);
        File fileFromStorage = fileRepository.findFileByFileName(fileName)
                .orElseThrow(() -> new FileNotFoundException("Файл с именем: { " + fileName + " } не найден"));

        fileRepository.deleteById(fileFromStorage.getId());
    }

    public List<File> getAllFiles(int limit) {
        log.info("Поиск файлов. лимит = {}", limit);
        return fileRepository.findFilesWithLimit(limit);
    }

    private static FileDto mapFile(File file) {
        return FileDto.builder()
                .filename(file.getFileName())
                .size(file.getSize())
                .build();
    }
}

