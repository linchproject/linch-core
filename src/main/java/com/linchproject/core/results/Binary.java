package com.linchproject.core.results;

import com.linchproject.core.Result;

import java.io.InputStream;
import java.util.Date;

/**
 * @author Georg Schmidl
 */
public class Binary implements Result {

    private String fileName;

    private InputStream inputStream;

    private int length = -1;

    private boolean attachment = false;

    private String contentType;

    private Date lastModified;

    private boolean _public = false;

    public Binary() {
    }

    public Binary(String fileName, InputStream inputStream) {
        this.fileName = fileName;
        this.inputStream = inputStream;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isAttachment() {
        return attachment;
    }

    public void setAttachment(boolean attachment) {
        this.attachment = attachment;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public boolean isPublic() {
        return _public;
    }

    public void setPublic(boolean _public) {
        this._public = _public;
    }
}
