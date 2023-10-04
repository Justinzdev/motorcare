package com.example.myproject.model;

public class MarkerInfo {
    private int bp_count_use;
    private int bp_id;

    public MarkerInfo(int bp_count_use, int bp_id) {
        this.bp_count_use = bp_count_use;
        this.bp_id = bp_id;
    }

    public int getBPCountUse() {
        return bp_count_use;
    }
    public int getBPID() { return bp_id; }
}
