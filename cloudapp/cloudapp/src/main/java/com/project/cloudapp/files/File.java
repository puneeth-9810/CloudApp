package com.project.cloudapp.files;

import com.project.cloudapp.people.People;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue
    private Integer id;
    private String s3_url;
    private String description;
    private double size;
    @ManyToOne
    @JoinColumn(name = "people_id")
    private People people;

}
