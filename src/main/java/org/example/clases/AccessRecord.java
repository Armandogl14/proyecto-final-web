package org.example.clases;

import java.time.LocalDateTime;

public class AccessRecord {
    private LocalDateTime accessTime;
    private String browser;
    private String ipAddress;
    private String clientDomain;
    private String operatingSystemPlatform;
    private URL url;

    public AccessRecord(LocalDateTime accessTime, String browser, String ipAddress, String clientDomain, String operatingSystemPlatform, URL url) {
        this.accessTime = accessTime;
        this.browser = browser;
        this.ipAddress = ipAddress;
        this.clientDomain = clientDomain;
        this.operatingSystemPlatform = operatingSystemPlatform;
        this.url = url;
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
