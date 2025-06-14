package com.project.cloudapp.files;

import com.project.cloudapp.people.People;
import com.project.cloudapp.people.PeopleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final FileRepo fileRepo;
    private final PeopleRepo peopleRepo;
    private final S3Client s3Client;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String description,
            @RequestParam("email") String email
    ) throws IOException {
        File uploaded = fileService.uploadFile(file, description, email);
        return ResponseEntity.ok(uploaded);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllFiles(@RequestParam String email) {
        People user = peopleRepo.findByEmail(email).orElseThrow();
        List<File> files = fileRepo.findAllByPeople(user);
        return ResponseEntity.ok(files);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable Integer id) {
        File file = fileRepo.findById(id).orElseThrow();
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket("cloudapp9810")
                .key(file.getS3_url().split(".com/")[1]) // Extract key from full URL
                .build());
        fileRepo.delete(file);
        return ResponseEntity.ok("File deleted successfully");
    }

}
