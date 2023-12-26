package netology.diplom.filestorage.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileType;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    @Column(columnDefinition = "bytea", name = "file_data")
    private byte[] fileData;

    private String hash;

    private Long size;
}
