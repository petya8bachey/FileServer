package com.petya8bachey.file_server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FileServiceTests {

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileService fileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveFile() {
        File file = new File("file1", "content");

        fileService.saveFile(file);
        verify(fileRepository, times(1)).save(file);
    }

    @Test
    void testGetFile_FileExists() throws ExecutionException, InterruptedException {
        File file = new File("file1", "content");
        when(fileRepository.findById("file1")).thenReturn(Optional.of(file));

        Future<File> result = fileService.getFile("file1");
        File fetchedFile = result.get();

        assertNotNull(fetchedFile);
        assertEquals("file1", fetchedFile.getName());
        verify(fileRepository, times(1)).findById("file1");
    }

    @Test
    void testGetFile_FileNotFound() throws ExecutionException, InterruptedException {
        when(fileRepository.findById("file1")).thenReturn(Optional.empty());

        Future<File> result = fileService.getFile("file1");
        File fetchedFile = result.get();

        assertNull(fetchedFile);
        verify(fileRepository, times(1)).findById("file1");
    }

    @Test
    void testDeleteFile_FileExists() {
        File file = new File("file1", "content");
        when(fileRepository.findById("file1")).thenReturn(Optional.of(file));

        fileService.deleteFile("file1");
        verify(fileRepository, times(1)).deleteById("file1");
    }

    @Test
    void testDeleteFile_FileNotFound() {
        when(fileRepository.findById("file1")).thenReturn(Optional.empty());

        fileService.deleteFile("file1");
        verify(fileRepository, times(1)).findById("file1");
        verify(fileRepository, never()).deleteById("file1");
    }

    @Test
    void testSetContentToFile_FileExists() throws ExecutionException, InterruptedException {
        File file = new File("file1", "content");
        when(fileRepository.findById("file1")).thenReturn(Optional.of(file));

        fileService.setContentToFile("file1", "new content");

        Future<Void> result = fileService.setContentToFile("file1", "new content");
        result.get();

        verify(fileRepository, times(1)).save(file);
        assertEquals("new content", file.getContent());
    }

    @Test
    void testSetContentToFile_FileNotFound() throws ExecutionException, InterruptedException {
        when(fileRepository.findById("file1")).thenReturn(Optional.empty());

        Future<Void> result = fileService.setContentToFile("file1", "new content");
        result.get();
        verify(fileRepository, times(1)).findById("file1");
        verify(fileRepository, never()).save(any());
    }

    @Test
    void testDeleteFile_FileNotFound_WithArgumentCaptor() {
        File file = new File("file1", "content");
        when(fileRepository.findById("file1")).thenReturn(Optional.empty());

        fileService.deleteFile("file1");

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(fileRepository, never()).deleteById(captor.capture());
        assertNull(captor.getValue());
    }
}
