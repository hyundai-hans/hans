package com.handsome.mall.entity.primary;

import com.handsome.mall.entity.BaseEntity;
import com.handsome.mall.valueobject.Role;
import java.time.LocalDateTime;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Value;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_email", length = 255, nullable = false)
    private String email;

    @Column(name = "member_nickname", length = 20, nullable = false)
    private String nickname;

    @Column(name = "member_password", length = 255, nullable = false)
    private String password;

    @Lob
    @Column(name = "member_profile_img", nullable = false)
    private String profileImg;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", length = 10, nullable = false)
    private Role role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<PostLike> postLikes;


}
