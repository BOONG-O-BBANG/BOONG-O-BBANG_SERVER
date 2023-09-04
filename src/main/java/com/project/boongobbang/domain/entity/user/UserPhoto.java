package com.project.boongobbang.domain.entity.user;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    private String photoUrl;

    @OneToOne
    @JoinColumn(name = "photo_id")
    private User photoUser;

    public UserPhoto(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
