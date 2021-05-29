package kr.milibrary.domain;

import java.util.Map;

public class Mail {
    private String subject;
    private String toAddr;
    private String fromAddr;
    private Map<String, Object> variables;

    public Mail(String subject, String toAddr, String fromAddr, Map<String, Object> variables) {
        this.subject = subject;
        this.toAddr = toAddr;
        this.fromAddr = fromAddr;
        this.variables = variables;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getToAddr() {
        return toAddr;
    }

    public void setToAddr(String toAddr) {
        this.toAddr = toAddr;
    }

    public String getFromAddr() {
        return fromAddr;
    }

    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }
}
