package cn.acyou.leo.framework.commons;

import cn.acyou.leo.framework.model.PageData;
import cn.acyou.leo.framework.model.PageSo;
import cn.acyou.leo.framework.util.BeanCopyUtil;
import cn.acyou.leo.framework.util.SqlUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author fangyou
 * @version [1.0.0, 2021-08-03 16:57]
 */
public class PageQuery {

    /**
     * 转换
     * 使用 springframework.data.domain.Page 时转换为 PageData
     *
     * <p>example:</p>
     * <pre>
     *        Page&lt;ProductEo&gt; users = productEsRepository.findAll(PageRequest.of(1, 20));
     *        PageData&lt;ProductEo&gt; pageData = PageQuery.convert(user);
     * </pre>
     *
     * @param <T> 类型
     * @param springPage spring data的page
     * @return PageData
     */
    public static <T> PageData<T> convert(Page<T> springPage) {
        Integer pageNum = springPage.getNumber() != 0 ? springPage.getNumber() : 1;
        PageData<T> resultData = new PageData<>(pageNum, springPage.getSize());
        resultData.setTotal(springPage.getTotalElements());
        resultData.setList(springPage.getContent());
        resultData.setTotalPage(springPage.getTotalPages());
        return resultData;
    }

    /**
     * 使用 PageHelper 时转换为 PageData
     *
     * <p>example:</p>
     * <pre>
     *     PageHelper.startPage(req.getPageNum(), req.getPageSize());
     *     List&lt;MarketingProductVo&gt; marketingProductList = marketingProductMapper.selectMarketingProduct(req);
     *     PageData&lt;MarketingProductVo&gt; PageData = PageData.convert(new PageInfo&lt;&gt;(marketingProductList));
     * </pre>
     *
     * @param pageInfo pageHelper 分页对象
     * @param <T>      具体类型
     * @return PageData
     */
    public static <T> PageData<T> convert(PageInfo<T> pageInfo) {
        //这里没有数据的时候pageNum是0
        Integer pageNum = pageInfo.getPageNum() != 0 ? pageInfo.getPageNum() : 1;
        PageData<T> resultData = new PageData<>(pageNum, pageInfo.getPageSize());
        resultData.setTotal(pageInfo.getTotal());
        resultData.setList(pageInfo.getList());
        resultData.setTotalPage(pageInfo.getPages());
        return resultData;
    }

    /**
     * 使用 PageHelper 时转换为 PageData（包含类型转换）
     *
     * @param <T> 目标类型
     * @param <E> pageInfo类型
     * @param pageInfo 页面信息
     * @param tarClass 目标类型Class
     * @return PageData
     */
    public static <E, T> PageData<T> convert(PageInfo<E> pageInfo, Class<T> tarClass) {
        //这里没有数据的时候pageNum是0
        Integer pageNum = pageInfo.getPageNum() != 0 ? pageInfo.getPageNum() : 1;
        PageData<T> resultData = new PageData<>(pageNum, pageInfo.getPageSize());
        resultData.setTotal(pageInfo.getTotal());
        resultData.setList(BeanCopyUtil.copyList(pageInfo.getList(), tarClass));
        resultData.setTotalPage(pageInfo.getPages());
        return resultData;
    }

    /**
     * 提供方法：使用pageHelper时 转 PageData
     *
     * <p>example:</p>
     * <pre>
     *      PageHelper.startPage(req.getPageNum(), req.getPageSize());
     *      List&lt;MarketingProductVo&gt; marketingProductList = marketingProductMapper.selectMarketingProduct(req);
     *      PageData&lt;MarketingProductVo&gt; PageData = PageData.convert(marketingProductList);
     * </pre>
     *
     * @param <T>      具体类型
     * @param dataList dataList 数据List
     * @return PageData
     */
    public static <T> PageData<T> convert(List<T> dataList) {
        PageInfo<T> pageInfo = new PageInfo<>(dataList);
        return convert(pageInfo);
    }

    /**
     * 开启分页
     *
     * 配合：{@link #selectMapper 使用}
     * <p>example:</p>
     *
     * <pre>
     *    PageData&lt;Student&gt; convert2 =  PageData.startPage(pageNum, pageSize).selectMapper(studentService.selectAll());
     *    PageData&lt;StudentVo&gt; convertType =  PageData.startPage(pageNum, pageSize).selectMapper(studentService.selectAll(), StudentVo.class);
     * </pre>
     *
     * @param pageNum  页码，从1开始
     * @param pageSize 页面大小
     * @return PageData
     */
    public static PageQuery startPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageQuery();
    }

    /**
     * 参考： {@link #startPage(Integer, Integer)}
     *
     * @param pageNum      页码，从1开始
     * @param pageSize     页面大小
     * @param pageSizeZero true     时支持pageSize=0查全部
     *                     false    时pageSize=0没有结果
     *                     null     时用按照默认{false}配置
     * @return PageData
     */
    public static PageQuery startPage(Integer pageNum, Integer pageSize, Boolean pageSizeZero) {
        PageHelper.startPage(pageNum, pageSize, true, null, pageSizeZero);
        return new PageQuery();
    }

    /**
     * 参考： {@link #startPage(Integer, Integer)}
     *
     * @param pageNum  页码，从1开始
     * @param pageSize 页面大小
     * @param orderBy  排序
     * @return PageData
     */
    public static PageQuery startPage(Integer pageNum, Integer pageSize, String orderBy) {
        PageHelper.startPage(pageNum, pageSize, orderBy);
        return new PageQuery();
    }

    /**
     * 参考： {@link #startPage(Integer, Integer)}
     *
     * @param pageSo 分页参数
     * @return PageData
     */
    public static PageQuery startPage(PageSo pageSo) {
        return startPage(pageSo.getPageNum(), pageSo.getPageSize(), SqlUtil.convertOrderBy(pageSo));
    }

    /**
     * 参考： {@link #startPage(Integer, Integer)}
     *
     * @param pageSo       分页参数
     * @param pageSizeZero true     时支持pageSize=0查全部
     *                     false    时pageSize=0没有结果
     *                     null     时用按照默认{false}配置
     * @return PageData
     */
    public static PageQuery startPage(PageSo pageSo, Boolean pageSizeZero) {
        PageHelper.startPage(pageSo.getPageNum(), pageSo.getPageSize(), true, null, pageSizeZero);
        PageHelper.orderBy(SqlUtil.convertOrderBy(pageSo));
        return new PageQuery();
    }

    /**
     * 分页查询
     *
     * @param queryList 查询结果
     * @param <T>      泛型
     * @return PageData
     */
    public <T> PageData<T> selectMapper(List<T> queryList) {
        PageInfo<T> pageInfo = new PageInfo<>(queryList);
        return convert(pageInfo);
    }

    /**
     * 分页查询（包含类型转换）
     *
     * @param <T>      目标类型
     * @param <E>      结果类型
     * @param queryList 查询结果
     * @param tarClass  模板类型
     * @return PageData
     */
    public <E, T> PageData<T> selectMapper(List<E> queryList, Class<T> tarClass) {
        PageInfo<E> pageInfo = new PageInfo<>(queryList);
        return convert(pageInfo, tarClass);
    }

}
