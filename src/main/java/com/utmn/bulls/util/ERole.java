package com.utmn.bulls.util;

public enum ERole {
    User(1), Expert(2), Admin(3);

    private final int level;
    private ERole(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
