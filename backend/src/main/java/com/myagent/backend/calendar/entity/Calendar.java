package com.myagent.backend.calendar.entity;

import com.myagent.backend.common.entity.BaseTimeEntity;
import com.myagent.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="calendars")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Calendar extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 32)
    private String inviteCode;

    @Builder
    public Calendar(User owner, String name, String inviteCode) {
        this.owner = owner;
        this.name = name;
        this.inviteCode = inviteCode;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
