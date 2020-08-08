package com.github.zhd4.fgps.models.sqlite.locations;

public interface ITable {
    void create();
    void clear();
    void close();
}
