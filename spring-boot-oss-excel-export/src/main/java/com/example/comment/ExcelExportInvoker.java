package com.example.comment;

import com.example.common.BasePageModel;

import java.util.List;

/**
 * 通用分页-导出组件
 */
public interface ExcelExportInvoker {

    /**
     * invoke方法
     *
     * @param pageModel 分页参数基类
     */
    List<?> invoke(BasePageModel pageModel);
}
