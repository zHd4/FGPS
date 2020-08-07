package com.github.zhd4.fgps.sqlite;

interface ITable {
    void createTable();
    void clearTable();
    void closeTable();
}
