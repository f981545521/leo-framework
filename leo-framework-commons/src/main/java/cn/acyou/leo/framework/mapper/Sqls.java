package cn.acyou.leo.framework.mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 参考 {@link tk.mybatis.mapper.util.Sqls}
 * <p>
 * 增加方法
 *
 * @author fangyou
 * @version [1.0.0, 2021-10-27 17:42]
 */
public class Sqls {

    private final Criteria criteria;

    private Sqls() {
        this.criteria = new Criteria();
    }

    public static Sqls custom() {
        return new Sqls();
    }

    public Criteria getCriteria() {
        return criteria;
    }

    /**
     * no condition
     */
    public Sqls andIsNull(String property) {
        this.criteria.criterions.add(new Criterion(property, "is null", "and"));
        return this;
    }

    public Sqls andIsNotNull(String property) {
        this.criteria.criterions.add(new Criterion(property, "is not null", "and"));
        return this;
    }

    public Sqls andEqualTo(String property, Object value) {
        this.criteria.criterions.add(new Criterion(property, value, "=", "and"));
        return this;
    }

    public Sqls andNotEqualTo(String property, Object value) {
        this.criteria.criterions.add(new Criterion(property, value, "<>", "and"));
        return this;
    }

    public Sqls andGreaterThan(String property, Object value) {
        this.criteria.criterions.add(new Criterion(property, value, ">", "and"));
        return this;
    }

    public Sqls andGreaterThanOrEqualTo(String property, Object value) {
        this.criteria.criterions.add(new Criterion(property, value, ">=", "and"));
        return this;
    }


    public Sqls andLessThan(String property, Object value) {
        this.criteria.criterions.add(new Criterion(property, value, "<", "and"));
        return this;
    }

    public Sqls andLessThanOrEqualTo(String property, Object value) {
        this.criteria.criterions.add(new Criterion(property, value, "<=", "and"));
        return this;
    }

    public Sqls andIn(String property, Iterable<?> values) {
        this.criteria.criterions.add(new Criterion(property, values, "in", "and"));
        return this;
    }

    public Sqls andNotIn(String property, Iterable<?> values) {
        this.criteria.criterions.add(new Criterion(property, values, "not in", "and"));
        return this;
    }

    public Sqls andBetween(String property, Object value1, Object value2) {
        this.criteria.criterions.add(new Criterion(property, value1, value2, "between", "and"));
        return this;
    }

    public Sqls andNotBetween(String property, Object value1, Object value2) {
        this.criteria.criterions.add(new Criterion(property, value1, value2, "not between", "and"));
        return this;
    }

    public Sqls andLike(String property, String value) {
        this.criteria.criterions.add(new Criterion(property, value, "like", "and"));
        return this;
    }

    public Sqls andNotLike(String property, String value) {
        this.criteria.criterions.add(new Criterion(property, value, "not like", "and"));
        return this;
    }


    public Sqls orIsNull(String property) {
        this.criteria.criterions.add(new Criterion(property, "is null", "or"));
        return this;
    }

    public Sqls orIsNotNull(String property) {
        this.criteria.criterions.add(new Criterion(property, "is not null", "or"));
        return this;
    }


    public Sqls orEqualTo(String property, Object value) {
        this.criteria.criterions.add(new Criterion(property, value, "=", "or"));
        return this;
    }

    public Sqls orNotEqualTo(String property, Object value) {
        this.criteria.criterions.add(new Criterion(property, value, "<>", "or"));
        return this;
    }

    public Sqls orGreaterThan(String property, Object value) {
        this.criteria.criterions.add(new Criterion(property, value, ">", "or"));
        return this;
    }

    public Sqls orGreaterThanOrEqualTo(String property, Object value) {
        this.criteria.criterions.add(new Criterion(property, value, ">=", "or"));
        return this;
    }

    public Sqls orLessThan(String property, Object value) {
        this.criteria.criterions.add(new Criterion(property, value, "<", "or"));
        return this;
    }

    public Sqls orLessThanOrEqualTo(String property, Object value) {
        this.criteria.criterions.add(new Criterion(property, value, "<=", "or"));
        return this;
    }

    public Sqls orIn(String property, Iterable<?> values) {
        this.criteria.criterions.add(new Criterion(property, values, "in", "or"));
        return this;
    }

    public Sqls orNotIn(String property, Iterable<?> values) {
        this.criteria.criterions.add(new Criterion(property, values, "not in", "or"));
        return this;
    }

    public Sqls orBetween(String property, Object value1, Object value2) {
        this.criteria.criterions.add(new Criterion(property, value1, value2, "between", "or"));
        return this;
    }

    public Sqls orNotBetween(String property, Object value1, Object value2) {
        this.criteria.criterions.add(new Criterion(property, value1, value2, "not between", "or"));
        return this;
    }

    public Sqls orLike(String property, String value) {
        this.criteria.criterions.add(new Criterion(property, value, "like", "or"));
        return this;
    }

    public Sqls orNotLike(String property, String value) {
        this.criteria.criterions.add(new Criterion(property, value, "not like", "or"));
        return this;
    }


    /* add condition*/
    public Sqls andIsNull(boolean condition, String property) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, "is null", "and"));
        }
        return this;
    }

    public Sqls andIsNotNull(boolean condition, String property) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, "is not null", "and"));
        }
        return this;
    }

    public Sqls andEqualTo(boolean condition, String property, Object value) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, value, "=", "and"));
        }
        return this;
    }

    public Sqls andNotEqualTo(boolean condition, String property, Object value) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, value, "<>", "and"));
        }
        return this;
    }

    public Sqls andGreaterThan(boolean condition, String property, Object value) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, value, ">", "and"));
        }
        return this;
    }

    public Sqls andGreaterThanOrEqualTo(boolean condition, String property, Object value) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, value, ">=", "and"));
        }
        return this;
    }

    public Sqls andLessThan(boolean condition, String property, Object value) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, value, "<", "and"));
        }
        return this;
    }

    public Sqls andLessThanOrEqualTo(boolean condition, String property, Object value) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, value, "<=", "and"));
        }
        return this;
    }

    public Sqls andIn(boolean condition, String property, Iterable<?> values) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, values, "in", "and"));
        }
        return this;
    }

    public Sqls andNotIn(boolean condition, String property, Iterable<?> values) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, values, "not in", "and"));
        }
        return this;
    }

    public Sqls andBetween(boolean condition, String property, Object value1, Object value2) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, value1, value2, "between", "and"));
        }
        return this;
    }

    public Sqls andNotBetween(boolean condition, String property, Object value1, Object value2) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, value1, value2, "not between", "and"));
        }
        return this;
    }

    public Sqls andLike(boolean condition, String property, String value) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, value, "like", "and"));
        }
        return this;
    }

    public Sqls andNotLike(boolean condition, String property, String value) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, value, "not like", "and"));
        }
        return this;
    }


    public Sqls orIsNull(boolean condition, String property) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, "is null", "or"));
        }
        return this;
    }

    public Sqls orIsNotNull(boolean condition, String property) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, "is not null", "or"));
        }
        return this;
    }


    public Sqls orEqualTo(boolean condition, String property, Object value) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, value, "=", "or"));
        }
        return this;
    }

    public Sqls orNotEqualTo(boolean condition, String property, Object value) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, value, "<>", "or"));
        }
        return this;
    }

    public Sqls orGreaterThan(boolean condition, String property, Object value) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, value, ">", "or"));
        }
        return this;
    }

    public Sqls orGreaterThanOrEqualTo(boolean condition, String property, Object value) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, value, ">=", "or"));
        }
        return this;
    }

    public Sqls orLessThan(boolean condition, String property, Object value) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, value, "<", "or"));
        }
        return this;
    }

    public Sqls orLessThanOrEqualTo(boolean condition, String property, Object value) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, value, "<=", "or"));
        }
        return this;
    }

    public Sqls orIn(boolean condition, String property, Iterable<?> values) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, values, "in", "or"));
        }
        return this;
    }

    public Sqls orNotIn(boolean condition, String property, Iterable<?> values) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, values, "not in", "or"));
        }
        return this;
    }

    public Sqls orBetween(boolean condition, String property, Object value1, Object value2) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, value1, value2, "between", "or"));
        }
        return this;
    }

    public Sqls orNotBetween(boolean condition, String property, Object value1, Object value2) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, value1, value2, "not between", "or"));
        }
        return this;
    }

    public Sqls orLike(boolean condition, String property, String value) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, value, "like", "or"));
        }
        return this;
    }

    public Sqls orNotLike(boolean condition, String property, String value) {
        if (condition) {
            this.criteria.criterions.add(new Criterion(property, value, "not like", "or"));
        }
        return this;
    }

    public static class Criteria {
        private String andOr;
        private final List<Criterion> criterions;

        public Criteria() {
            this.criterions = new ArrayList<>(2);
        }

        public List<Criterion> getCriterions() {
            return criterions;
        }

        public String getAndOr() {
            return andOr;
        }

        public void setAndOr(String andOr) {
            this.andOr = andOr;
        }
    }

    public static class Criterion {
        private final String property;
        private Object value;
        private Object secondValue;
        private final String condition;
        private final String andOr;

        public Criterion(String property, String condition, String andOr) {
            this.property = property;
            this.condition = condition;
            this.andOr = andOr;
        }


        public Criterion(String property, Object value, String condition, String andOr) {
            this.property = property;
            this.value = value;
            this.condition = condition;
            this.andOr = andOr;
        }

        public Criterion(String property, Object value1, Object value2, String condition, String andOr) {
            this.property = property;
            this.value = value1;
            this.secondValue = value2;
            this.condition = condition;
            this.andOr = andOr;
        }

        public String getProperty() {
            return property;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public Object[] getValues() {
            if (value != null) {
                if (secondValue != null) {
                    return new Object[]{value, secondValue};
                } else {
                    return new Object[]{value};
                }
            } else {
                return new Object[]{};
            }
        }

        public String getCondition() {
            return condition;
        }

        public String getAndOr() {
            return andOr;
        }
    }
}
