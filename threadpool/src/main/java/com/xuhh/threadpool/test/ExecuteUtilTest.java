package com.xuhh.threadpool.test;

import com.xuhh.threadpool.comsumer.SomeManager;
import com.xuhh.threadpool.util.ExecuteUtil;
import org.apache.commons.lang3.RandomUtils;
import org.jeasy.random.EasyRandom;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;


@RunWith(PowerMockRunner.class)
public class ExecuteUtilTest {

    private EasyRandom easyRandom = new EasyRandom();

    @Mock
    private SomeManager someManager;

    // 测试数据
    private List<String> mockDataList;

    private int total = 30;

    @Before
    public void init() {
        // 构造30条数据
        mockDataList = easyRandom.objects(String.class, 30).collect(Collectors.toList());

    }

    @Test
    public void test_a_run_partition() {
        // mock aRun
        PowerMockito.doNothing().when(someManager).aRun(anyLong(), any());

        // 每批 10 个
        ExecuteUtil.partitionRun(mockDataList, 10, (eachList) -> someManager.aRun(1L, eachList));

        //验证执行了 3 次
        Mockito.verify(someManager, new Times(3)).aRun(anyLong(), any());
    }


    @Test
    public void test_call_return_list_partition() {
        // mock  每次调用返回条数(注意每次调用都是这2个)
        int eachReturnSize = 2;
        PowerMockito
                .doReturn(easyRandom.objects(String.class, eachReturnSize).collect(Collectors.toList()))
                .when(someManager)
                .aListMethod(anyLong(), any());

        // 分批执行
        int size = 4;
        List<Integer> resultList = ExecuteUtil.partitionCall2List(mockDataList, size, (eachList) -> someManager.aListMethod(2L, eachList));

        //验证执行次数
        int invocations = 8;
        Mockito.verify(someManager, new Times(invocations)).aListMethod(anyLong(), any());

        // 正好几轮
        int turns;
        if (total % size == 0) {
            turns = total / size;
        } else {
            turns = total / size + 1;
        }
        Assert.assertEquals(turns * eachReturnSize, resultList.size());
    }


    @Test
    public void test_call_return_map_partition() {
        // mock  每次调用返回条数
        // 注意：
        // 如果仅调用doReturn一次，那么每次返回都是key相同的Map，
        // 如果需要不覆盖，则doReturn次数和 invocations 相同）
        int eachReturnSize = 3;
        PowerMockito
                .doReturn(mockMap(eachReturnSize))
                .doReturn(mockMap(eachReturnSize))
                .when(someManager).aMapMethod(anyLong(), any());

        // 每批
        int size = 16;
        Map<String, Integer> resultMap = ExecuteUtil.partitionCall2Map(mockDataList, size, (eachList) -> someManager.aMapMethod(2L, eachList));

        //验证执行次数
        int invocations = 2;
        Mockito.verify(someManager, new Times(invocations)).aMapMethod(anyLong(), any());

        // 正好几轮
        int turns;
        if (total % size == 0) {
            turns = total / size;
        } else {
            turns = total / size + 1;
        }
        Assert.assertEquals(turns * eachReturnSize, resultMap.size());
    }

    private Map<String, Integer> mockMap(int size) {
        Map<String, Integer> result = new HashMap<>(size);
        for (int i = 0; i < size; i++) {

// 极力保证key不重复
            result.put(easyRandom.nextObject(String.class) + RandomUtils.nextInt(), easyRandom.nextInt());
        }
        return result;
    }


}
