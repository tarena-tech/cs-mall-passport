package com.tarena.passport.protocol.result;

import com.github.pagehelper.PageInfo;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * <p>数据分页类</p>
 *
 * @param <T> 列表数据
 */
@Data
public class JsonPage<T> implements Serializable {

    private Integer page;
    private Integer pageSize;
    private Integer totalPage;
    private Long total;
    private List<T> list;

    /**
     * 将PageHelper分页后的list转为分页信息
     */
    public static <T>JsonPage<T> restPage(PageInfo<T> pageInfo) {
        JsonPage<T> result = new JsonPage<T>();
        result.setTotalPage(pageInfo.getPages());
        result.setPage(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotal(pageInfo.getTotal());
        result.setList(pageInfo.getList());
        return result;
    }





}
