package netology.diplom.filestorage.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileDto {

    private String filename;
    private Long size;
    private String fileType;
    private byte[] fileData;
    private String hash;

}
