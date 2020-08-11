package com.xuhh.threadpool.comsumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SomeManager {

    public void aRun(Long id, List<String> data) {

    }

    public List<Integer> aListMethod(Long id, List<String> data) {
        return new ArrayList<>(0);
    }

    public Map<String, Integer> aMapMethod(Long id, List<String> data) {
        return new HashMap<>(0);
    }
}
