package org.example.clases;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;

import java.time.LocalDateTime;
@Entity("AccessRecord")
public class AccessRecord {
    @Id
    private int id;
    private LocalDateTime accessTime;
    private String browser;
    private String ipAddress;
    private String clientDomain;
    private String operatingSystemPlatform;
    @Reference
    private URL url;

    public AccessRecord(int id, LocalDateTime accessTime, String browser, String ipAddress, String clientDomain, String operatingSystemPlatform, URL url) {
        this.id = id;
        this.accessTime = accessTime;
        this.browser = browser;
        this.ipAddress = ipAddress;
        this.clientDomain = clientDomain;
        this.operatingSystemPlatform = operatingSystemPlatform;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(LocalDateTime accessTime) {
        this.accessTime = accessTime;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getClientDomain() {
        return clientDomain;
    }

    public void setClientDomain(String clientDomain) {
        this.clientDomain = clientDomain;
    }

    public String getOperatingSystemPlatform() {
        return operatingSystemPlatform;
    }

    public void setOperatingSystemPlatform(String operatingSystemPlatform) {
        this.operatingSystemPlatform = operatingSystemPlatform;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}
