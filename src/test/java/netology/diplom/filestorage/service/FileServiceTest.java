package netology.diplom.filestorage.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import netology.diplom.filestorage.dto.UpdateRequestDto;
import netology.diplom.filestorage.repository.FileRepository;
import netology.diplom.filestorage.testcontainer.PostgresInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Sql(scripts = "classpath:sql/insert_file.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ContextConfiguration(initializers = PostgresInitializer.class)
public class FileServiceTest {

    @Autowired
    private FileService fileService;

    @Autowired
    private FileRepository fileRepository;

    @Test
    void test_addFile() {
        var file = new MockMultipartFile(
                "file",
                "file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "MockMultipartFile".getBytes()
        );

        fileService.addFile(file, "file");

        assertTrue(fileRepository.findFileByFileName("file").isPresent());
    }

    @Test
    void test_getFile() {
        var file = fileService.getFile("check_list.csv");

        assertNotNull(file.getFileData());
        assertNotNull(file.getFileType());
        assertNotNull(file.getFileName());
    }

    @Test
    void test_renameFile() {
        fileService.renameFile("check_list.csv", new UpdateRequestDto("filename"));

        assertTrue(fileRepository.findFileByFileName("filename").isPresent());
        assertTrue(fileRepository.findFileByFileName("check_list.csv").isEmpty());
    }

    @Test
    void test_deleteFile() {
        fileService.deleteFile("check_list.csv");

        assertTrue(fileRepository.findAll().isEmpty());
    }

    @Test
    void test_getAllFiles() {
        var files = fileService.getAllFiles(1);

        assertEquals(1, files.size());
    }
}
