package netology.diplom.filestorage.repository;

import netology.diplom.filestorage.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findFileByFileName(String fileName);

    Optional<File> findFileByHash(String hash);

    @Query(value = "select * from file f limit ?1", nativeQuery = true)
    List<File> findFilesWithLimit(int limit);
}
