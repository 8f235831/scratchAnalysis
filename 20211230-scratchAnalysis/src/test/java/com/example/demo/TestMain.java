package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.example.demo.util.Analysis;

public class TestMain
{
    public static final void main(String[] args)
    {
        System.out.println(JSON.toJSONString(Analysis.analyse(json)));
    }

    private static String json = "";
}
