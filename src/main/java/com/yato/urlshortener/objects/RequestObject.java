package com.yato.urlshortener.objects;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Data
@Entity
@Table(name="url_mappings")
public class RequestObject {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long  uuid;

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
