package netology.diplom.filestorage.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import netology.diplom.filestorage.dto.FileDto;
import netology.diplom.filestorage.dto.UpdateRequestDto;
import netology.diplom.filestorage.entity.File;
import netology.diplom.filestorage.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class FileControllerTest {

    private static final String AUTH_TOKEN = "auth-token";
    private static final String VALUE_TOKEN = "Bearer auth-token";
    private static final String FILE_NAME = "filename";
    private static final String MY_FILE_NAME = "fileName.txt";
    private static final String URL_FILE = "/file";

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private FileService fileService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new FileController(fileService)).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void test_addFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                MY_FILE_NAME,
                MediaType.TEXT_PLAIN_VALUE,
                "Cloud_storage".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_FILE)
                        .file(multipartFile)
                        .param(FILE_NAME, "file")
                        .header(AUTH_TOKEN, VALUE_TOKEN))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void test_getFile() throws Exception {
        File fileDto = File.builder()
                .fileName(MY_FILE_NAME)
                .fileData("Cloud_storage".getBytes())
                .fileType(MediaType.TEXT_PLAIN_VALUE).build();

        Mockito.when(fileService.getFile(MY_FILE_NAME)).thenReturn(fileDto);

        mockMvc.perform(get(URL_FILE)
                        .param(FILE_NAME, MY_FILE_NAME)
                        .header(AUTH_TOKEN, VALUE_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE));
    }

    @Test
    void test_renameFile() throws Exception {
        UpdateRequestDto newName = new UpdateRequestDto("newFileName.txt");

        mockMvc.perform(put(URL_FILE)
                        .param(FILE_NAME, MY_FILE_NAME)
                        .header(AUTH_TOKEN, VALUE_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newName)))
                .andExpect(status().isOk());
    }

    @Test
    void test_deleteFile() throws Exception {
        mockMvc.perform(delete(URL_FILE)
                        .param(FILE_NAME, MY_FILE_NAME)
                        .header(AUTH_TOKEN, VALUE_TOKEN))
                .andExpect(status().isOk());
    }

    @Test
    void test_getAllFiles() throws Exception {
        List<File> listFile = List.of(
                File.builder().size(1111L).fileName("file1.txt").build(),
                File.builder().size(2222L).fileName("file2.txt").build(),
                File.builder().size(3333L).fileName("file3.txt").build());
        Mockito.when(fileService.getAllFiles(3)).thenReturn(listFile);

        var result = mockMvc.perform(get("/list")
                        .header(AUTH_TOKEN, VALUE_TOKEN)
                        .param("limit", "3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertFalse(result.isBlank());
    }
}
