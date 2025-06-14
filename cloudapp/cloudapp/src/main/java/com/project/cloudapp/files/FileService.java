package com.project.cloudapp.files;

import com.project.cloudapp.people.People;
import com.project.cloudapp.people.PeopleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final S3Client s3Client;
    private final FileRepo fileRepo;
    private final PeopleRepo peopleRepo;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    private final double MAX_USER_QUOTA_MB = 500.0; // Example quota

    public File uploadFile(MultipartFile file, String description, String userEmail) throws IOException {
        People user = peopleRepo.findByEmail(userEmail).orElseThrow();

        double currentSize = fileRepo.findAllByPeople(user).stream()
                .mapToDouble(File::getSize)
                .sum();

        double fileSizeMB = file.getSize() / (1024.0 * 1024.0);

        if (currentSize + fileSizeMB > MAX_USER_QUOTA_MB) {
            throw new IllegalStateException("Storage quota exceeded.");
        }

        String key = "uploads/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );

        File newFile = File.builder()
                .description(description)
                .s3_url("https://" + bucketName + ".s3.amazonaws.com/" + key)
                .size(fileSizeMB)
                .people(user)
                .build();

        return fileRepo.save(newFile);
    }
}
