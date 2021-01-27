package com.yato.urlshortener.objects;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Data
@Entity
@Table(name="url_mappings")
public class RequestObject {

    @Id
    @Column(name="id")
    private String clientId;

    @Column(name="original_url")
    private String originalURL;

    @Column(name="short_url")
    private String shortURL;

    @Column(name="redirection_count")
    private Long redirectionCount;

    @Column(name="entry_addition_time")
    private Long entryAdditionTime;

    @Column(name="expiry_time")
    private Long expiryTime;

    public RequestObject(){}

    @Override
    public String toString() {
        return "RequestObject{" +
                "clientId='" + clientId + '\'' +
                ", originalURL='" + originalURL + '\'' +
                ", shortURL='" + shortURL + '\'' +
                ", redirectionCount=" + redirectionCount +
                ", entryAdditionTime=" + entryAdditionTime +
                ", expiryTime=" + expiryTime +
                '}';
    }
}
