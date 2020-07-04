package com.sxnsh.exam.domain;

import lombok.Data;

import java.util.List;

@Data
public class TableDomain<T> {
    public String code;
    public String msg;
    public long count;
    public List<T> data;
}
