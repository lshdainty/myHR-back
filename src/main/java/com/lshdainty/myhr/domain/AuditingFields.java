package com.lshdainty.myhr.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public class AuditingFields {
    @CreatedDate
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "create_by")
    private Long createBy;

    @Column(name = "creaet_ip")
    private String createIP;

//    @LastModifiedDate
    @Column(name = "delete_date")
    private LocalDateTime deleteDate;

    @Column(name = "delete_by")
    private Long deleteBy;

    @Column(name = "delete_ip")
    private String deleteIP;

    public void setCreated(LocalDateTime date, Long no, String ip) {
        this.createDate = date;
        this.createBy = no;
        this.createIP = ip;
    }

    public void setCreated(Long no, String ip) {
        this.createBy = no;
        this.createIP = ip;
    }

    public void setDeleted(LocalDateTime date, Long no, String ip) {
        this.deleteDate = date;
        this.deleteBy = no;
        this.deleteIP = ip;
    }
}
