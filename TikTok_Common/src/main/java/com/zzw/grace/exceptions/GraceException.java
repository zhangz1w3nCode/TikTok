package com.zzw.grace.exceptions;

import com.zzw.grace.result.GraceJSONResult;
import com.zzw.grace.result.ResponseStatusEnum;

/**
 * 优雅的处理异常，统一封装
 */
public class GraceException {

    public static GraceJSONResult display(ResponseStatusEnum responseStatusEnum) {
        throw new MyCustomException(responseStatusEnum);
    }

}
