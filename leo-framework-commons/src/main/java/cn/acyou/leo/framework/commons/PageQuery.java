package cn.acyou.leo.framework.commons;

import cn.acyou.leo.framework.constant.CommonErrorEnum;
import cn.acyou.leo.framework.exception.ServiceException;
import cn.acyou.leo.framework.model.PageData;
import cn.acyou.leo.framework.model.PageSo;
import cn.acyou.leo.framework.util.BeanCopyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.CaseFormat;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询工具
 *
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
     * Page&lt;ProductEo&gt; users = productEsRepository.findAll(PageRequest.of(1, 20));
     * PageData&lt;ProductEo&gt; pageData = PageQuery.convert(user);
     * </pre>
     *
     * @param <T> 数据类型
     * @param springPage spring data的分页对象
     * @return 分页数据
     */
    public static <T> PageData<T> convert(Page<T> springPage) {
        int pageNum = springPage.getNumber() != 0 ? springPage.getNumber() : 1;
        PageData<T> resultData = new PageData<>(pageNum, springPage.getSize(), springPage.getTotalElements());
        resultData.setList(springPage.getContent());
        return resultData;
    }

    /**
     * 使用 PageHelper 时转换为 PageData
     *
     * <p>example:</p>
     * <pre>
     * PageHelper.startPage(req.getPageNum(), req.getPageSize());
     * List&lt;MarketingProductVo&gt; marketingProductList = marketingProductMapper.selectMarketingProduct(req);
     * PageData&lt;MarketingProductVo&gt; PageData = PageData.convert(new PageInfo&lt;&gt;(marketingProductList));
     * </pre>
     *
     * @param pageInfo pageHelper 分页对象
     * @param <T>      数据类型
     * @return 分页数据
     */
    public static <T> PageData<T> convert(PageInfo<T> pageInfo) {
        //这里没有数据的时候pageNum是0
        int pageNum = pageInfo.getPageNum() != 0 ? pageInfo.getPageNum() : 1;
        PageData<T> resultData = new PageData<>(pageNum, pageInfo.getPageSize(), pageInfo.getTotal());
        resultData.setList(pageInfo.getList());
        return resultData;
    }

    /**
     * 使用 PageHelper 时转换为 PageData（包含类型转换）
     *
     * @param <T> 目标类型
     * @param <E> pageInfo类型
     * @param pageInfo 分页信息
     * @param tarClass 目标类型Class
     * @return 分页数据
     */
    public static <E, T> PageData<T> convert(PageInfo<E> pageInfo, Class<T> tarClass) {
        //这里没有数据的时候pageNum是0
        int pageNum = pageInfo.getPageNum() != 0 ? pageInfo.getPageNum() : 1;
        PageData<T> resultData = new PageData<>(pageNum, pageInfo.getPageSize(), pageInfo.getTotal());
        resultData.setList(BeanCopyUtil.copyList(pageInfo.getList(), tarClass));
        return resultData;
    }

    /**
     * 提供方法：使用pageHelper时 转 PageData
     *
     * <p>example:</p>
     * <pre>
     * PageHelper.startPage(req.getPageNum(), req.getPageSize());
     * List&lt;MarketingProductVo&gt; marketingProductList = marketingProductMapper.selectMarketingProduct(req);
     * PageData&lt;MarketingProductVo&gt; PageData = PageQuery.convert(marketingProductList);
     * </pre>
     *
     * @param <T>      具体类型
     * @param dataList dataList 数据List
     * @return 分页数据
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
     *  PageData&lt;Student&gt; page1 =  PageData.startPage(pageNum, pageSize)
     *                 .selectMapper(studentService.selectAll());
     *  PageData&lt;StudentVo&gt; page2 =  PageData.startPage(pageNum, pageSize)
     *                 .selectMapper(studentService.selectAll(), StudentVo.class);
     * </pre>
     *
     * @param pageNum  页码，从1开始
     * @param pageSize 页面大小
     * @return 开启查询
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
     * @return 开启查询
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
     * @return 开启查询
     */
    public static PageQuery startPage(Integer pageNum, Integer pageSize, String orderBy, Boolean count) {
        com.github.pagehelper.Page<Object> startedPage = PageHelper.startPage(pageNum, pageSize, orderBy);
        if (count != null) {
            startedPage.setCount(count);
        }
        return new PageQuery();
    }

    /**
     * 参考： {@link #startPage(Integer, Integer)}
     *
     * @param pageSo 分页参数
     * @return 开启查询
     */
    public static PageQuery startPage(PageSo pageSo) {
        return startPage(pageSo.getPageNum(), pageSo.getPageSize(), convertOrderBy(pageSo), pageSo.getIncludeCountQuery());
    }

    /**
     * 参考： {@link #startPage(Integer, Integer)}
     *
     * @param pageSo       分页参数
     * @param pageSizeZero true     时支持pageSize=0查全部
     *                     false    时pageSize=0没有结果
     *                     null     时用按照默认{false}配置
     * @return 开启查询
     */
    public static PageQuery startPage(PageSo pageSo, Boolean pageSizeZero) {
        return startPage(pageSo, true, pageSizeZero);
    }

    /**
     * 参考： {@link #startPage(Integer, Integer)}
     *
     * @param pageSo       分页参数
     * @param countQuery   true     时支持包含count查询
     *                     false    时不包含count查询
     *                     null     时用按照默认{false}配置
     * @param pageSizeZero true     时支持pageSize=0查全部
     *                     false    时pageSize=0没有结果
     *                     null     时用按照默认{false}配置
     * @return 开启查询
     */
    public static PageQuery startPage(PageSo pageSo, boolean countQuery, Boolean pageSizeZero) {
        PageHelper.startPage(pageSo.getPageNum(), pageSo.getPageSize(), countQuery, null, pageSizeZero);
        PageHelper.orderBy(convertOrderBy(pageSo));
        return new PageQuery();
    }

    /**
     * 执行查询
     *
     * @param queryList 查询结果
     * @param <T>      泛型
     * @return 分页数据
     */
    public <T> PageData<T> selectMapper(List<T> queryList) {
        PageInfo<T> pageInfo = new PageInfo<>(queryList);
        return convert(pageInfo);
    }

    /**
     * 执行查询（包含类型转换）
     *
     * @param <T>      目标类型
     * @param <E>      结果类型
     * @param queryList 查询结果
     * @param tarClass  模板类型
     * @return 分页数据
     */
    public <E, T> PageData<T> selectMapper(List<E> queryList, Class<T> tarClass) {
        PageInfo<E> pageInfo = new PageInfo<>(queryList);
        return convert(pageInfo, tarClass);
    }

    /**
     * 返回空分页
     * @return 空分页数据
     */
    public static <T> PageData<T> empty() {
        return new PageData<>(1, 10, 0L);
    }

    /**
     * 返回空分页 （根据 分页大小）
     * @param pageSize 分页大小
     * @return 空分页数据
     */
    public static <T> PageData<T> empty(int pageSize) {
        return new PageData<>(1, pageSize, 0L);
    }


    /**
     * 转换为OrderBy 条件
     *
     * <p>示例: createTime-desc,roleCode-asc</p>
     *
     * 注意：
     * 1. 如果这里存在排序，则会覆盖Mapper.xml中的orderBy以及MP中的orderBy
     * 2. 排序字段要是SQL中的返回字段，在Mapper.xml中使用别名来区分不同表：ct.name as card_name
     *
     *
     * @param pageSo 分页参数
     * @return order by sql
     */
    public static String convertOrderBy(PageSo pageSo) {
        List<String> orderBySqlList = new ArrayList<>();
        String sortsStr = pageSo.getSorts();
        //非法的sort参数
        boolean illegalOrderBy = false;
        if (StringUtils.hasText(sortsStr)) {
            String[] orderItems = sortsStr.split(",");
            for (String orderItem : orderItems) {
                String[] split = orderItem.split("-");
                if (split.length % 2 == 0) {
                    String key = split[0];
                    String type = split[1];
                    if (StringUtils.hasText(key) && StringUtils.hasText(type)&& isOrderByType(type)) {
                        //key 转为下划线命名
                        orderBySqlList.add(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, key) + " " + type.toLowerCase());
                    } else {
                        illegalOrderBy = true;
                    }
                } else {
                    illegalOrderBy = true;
                }
            }
        }
        if (illegalOrderBy) {
            throw new ServiceException(CommonErrorEnum.E_INVALID_SORT_PARAMETER);
        }
        if (!orderBySqlList.isEmpty()) {
            return StringUtils.collectionToDelimitedString(orderBySqlList, ", ");
        }
        return null;
    }

    /**
     * 是排序类型
     *
     * @param type 类型
     * @return boolean
     */
    private static boolean isOrderByType(String type) {
        return OrderBySymbols.asc.name().equalsIgnoreCase(type) || OrderBySymbols.desc.name().equalsIgnoreCase(type);
    }

    /**
     * order by符号
     */
    private enum OrderBySymbols {
        /**
         * 正序
         */
        asc,
        /**
         * 倒序
         */
        desc
    }

}
