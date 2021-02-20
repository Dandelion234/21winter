package com.example.twthomeworkjava;

import java.util.List;

public class JsonRootBean {
    private List<Recent> recent;
    public void setRecent(List<Recent> recent) {
        this.recent = recent;
    }
    public List<Recent> getRecent() {
        return recent;
    }

}
