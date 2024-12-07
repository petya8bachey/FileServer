package com.petya8bachey.file_server;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface FileRepository extends JpaRepository<File, String> {
}
