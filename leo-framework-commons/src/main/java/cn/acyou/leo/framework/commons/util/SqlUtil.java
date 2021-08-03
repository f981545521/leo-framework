package cn.acyou.leo.framework.commons.util;

import cn.acyou.leo.framework.commons.constant.CommonErrorEnum;
import cn.acyou.leo.framework.commons.exception.ServiceException;
import cn.acyou.leo.framework.model.PageSo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/9]
 **/
public class SqlUtil {

    /**
     * 转换为OrderBy 条件
     * example: createTime-desc,roleCode-asc
     * @param pageSo 页面
     * @return {@link String}
     */
    public static String convertOrderBy(PageSo pageSo){
        List<String> orderBySqlList = new ArrayList<>();
        Map<String, String> supportFieldMap = pageSo.supportField();
        String sortsStr = pageSo.getSorts();
        //非法的sort参数
        boolean illegalOrderBy = false;
        if (StringUtils.isNotEmpty(sortsStr) && MapUtils.isNotEmpty(supportFieldMap)){
            Set<String> supportKeys = supportFieldMap.keySet();
            String[] orderItems = sortsStr.split(",");
            if (orderItems.length % 2 == 0) {
                for (String orderItem: orderItems){
                    String[] split = orderItem.split("-");
                    if (split.length % 2 == 0){
                        String key = split[0];
                        String type = split[1];
                        if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(type) &&
                                supportKeys.contains(key) && isOrderByType(type)){
                            orderBySqlList.add(supportFieldMap.get(key) + " " + type.toLowerCase());
                        }else {
                            illegalOrderBy = true;
                        }
                    }else {
                        illegalOrderBy = true;
                    }
                }
            }else {
                illegalOrderBy = true;
            }
        }
        if (illegalOrderBy){
            throw new ServiceException(CommonErrorEnum.E_INVALID_SORT_PARAMETER);
        }
        if (CollectionUtils.isNotEmpty(orderBySqlList)){
            return StringUtils.join(orderBySqlList, ", ");
        }
        return null;
    }

    /**
     * 是排序类型
     *
     * @param type 类型
     * @return boolean
     */
    public static boolean isOrderByType(String type){
        return OrderBySymbols.asc.name().equalsIgnoreCase(type) || OrderBySymbols.desc.name().equalsIgnoreCase(type);
    }

    public enum OrderBySymbols {
        /** 正序 */
        asc,
        /** 倒序 */
        desc
    }

    public enum CompareSymbols {
        /** 等于 */
        eq,
        /** 不等于 */
        ne,
        /** 小于 less than */
        lt,
        /** 大于 greater than */
        gt,
    }

}
