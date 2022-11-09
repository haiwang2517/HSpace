package com.ken.hspace.resource.utils;

import java.util.HashMap;
import java.util.Map;

public class SingleResultBundle {

    public static Object failed(String errorMessage) {
        Map<String ,Object> result = new HashMap<>();
        result.put("message", errorMessage);
        return result;
    }
}
