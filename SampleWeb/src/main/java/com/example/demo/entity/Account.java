package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.enums.Gender;
import com.example.demo.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "accounts")
public class Account implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACCESS_GRANTED; // デフォルトを ACCESS_GRANTED に設定


    @Column(name = "profile_image", length = 512)
    private String profileImage;

    @Transient  // エンティティのプロパティではないため、@Transientを付けます。
    private MultipartFile profileImageFile;

    @Column(name = "furigana", length = 255)
    private String furigana;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "age")
    private Integer age;

    @Column(name = "introduction", length = 1500)
    private String introduction;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Column(name = "role", nullable = false)
    private String role; // "ROLE_ADMIN", "ROLE_USER" などを保存

    @Column(name = "adminstatus")
    private boolean adminstatus;

    // Getter and Setter methods

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getFurigana() {
        return furigana;
    }

    public void setFurigana(String furigana) {
        this.furigana = furigana;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAdminstatus() {
        return adminstatus;
    }

    public void setAdminstatus(boolean adminstatus) {
        this.adminstatus = adminstatus;
    }

    // UserDetails インターフェースの実装

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // ここでアカウントの有効期限を制御可能
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // ここでアカウントのロック状態を制御可能
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // ここで資格情報の有効期限を制御可能
    }

    @Override
    public boolean isEnabled() {
        return !isDeleted; // アカウントが削除されているかどうかを判断
    }
    
 // 作成日、更新日、削除日フィールド
    private LocalDateTime createdDate; // 作成日
    private LocalDateTime updatedDate; // 更新日
    private LocalDateTime deletedDate; // 削除日

    // 作成時に自動で作成日を設定
    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    // 更新時に自動で更新日を設定
    @PreUpdate
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }

    // 削除日時を手動で設定するメソッド（例: 論理削除を行う場合）
    public void setDeletedDate() {
        this.deletedDate = LocalDateTime.now();
    }

    // GetterとSetter
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public LocalDateTime getDeletedDate() {
        return deletedDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public void setDeletedDate(LocalDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }
    
    @Column(name = "likes", nullable = false)
    private int likes = 0;  // デフォルト値は0に設定

    // GetterとSetter
    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

}
