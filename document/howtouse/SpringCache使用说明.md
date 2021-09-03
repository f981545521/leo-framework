## SpringCahce
SpringCache本身是一个缓存体系的抽象实现，并没有具体的缓存能力，要使用SpringCache还需要配合具体的缓存实现来完成。

### 缓存注解

- `@EnableCaching`：开启缓存功能
- `@Cacheable`：定义缓存，用于触发缓存
- `@CachePut`：定义更新缓存，触发缓存更新
- `@CacheEvict`：定义清除缓存，触发缓存清除
- `@Caching`：组合定义多种缓存功能

#### 1. Cacheable
该注解用于标注于方法之上用于标识该方法的返回结果需要被缓存起来，
标注于类之上标识该类中所有方法均需要将结果缓存起来。

该注解标注的方法每次被调用前都会触发缓存校验，校验指定参数的缓存是否已存在（已发生过相同参数的调用），若存在，直接返回缓存结果，否则执行方法内容，最后将方法执行结果保存到缓存中。
```
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Cacheable {
    // 用于指定缓存名称，与cacheNames()方法效果一致
    @AliasFor("cacheNames")
    String[] value() default {};
    // 用于指定缓存名称，与value()方法效果一致
    @AliasFor("value")
    String[] cacheNames() default {};
    // 用于使用SPEL手动指定缓存键的组合方式，默认情况使用所有的参数来组合成键，除非自定义了keyGenerator。
    // 使用SPEL表达式可以根据上下文环境来获取到指定的数据：
    // #root.method：用于获取当前方法的Method实例
    // #root.target：用于获取当前方法的target实例
    // #root.caches：用于获取当前方法关联的缓存
    // #root.methodName：用于获取当前方法的名称
    // #root.targetClass：用于获取目标类类型
    // #root.args[1]：获取当前方法的第二个参数，等同于：#p1和#a1和#argumentName
    String key() default "";
    // 自定义键生成器，定义了该方法之后，上面的key方法自动失效，这个键生成器是：
    // org.springframework.cache.interceptor.KeyGenerator，这是一个函数式接口，
    // 只有一个generate方法，我们可以通过自定义的逻辑来实现自定义的key生成策略。
    String keyGenerator() default "";
    // 用于设置自定义的cacheManager(缓存管理器),可以自动生成一个cacheResolver
    // （缓存解析器），这一下面的cacheResolver()方法设置互斥
    String cacheManager() default "";
    // 用于设置一个自定义的缓存解析器
    String cacheResolver() default "";
    // 用于设置执行缓存的条件，如果条件不满足，方法返回的结果就不会被缓存，默认无条件全部缓存。
    // 同样使用SPEL来定义条件，可以使用的获取方式同key方法。
    String condition() default "";
    // 这个用于禁止缓存功能，如果设置的条件满足，就不执行缓存结果，与上面的condition不同之处在于，
    // 该方法执行在当前方法调用结束，结果出来之后，因此，它除了可以使用上面condition所能使用的SPEL
    // 表达式之外，还可以使用#result来获取方法的执行结果，亦即可以根据结果的不同来决定是否缓存。
    String unless() default "";
    // 设置是否对多个针对同一key执行缓存加载的操作的线程进行同步，默认不同步。这个功能需要明确确定所
    // 使用的缓存工具支持该功能，否则不要滥用。
    boolean sync() default false;
}
```
#### 2. CachePut
该注解用于更新缓存，无论结果是否已经缓存，都会在方法执行结束插入缓存，相当于更新缓存。
```
参考@Cacheable
```

#### 3. CacheEvict
该注解主要用于删除缓存操作。
```
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheEvict {
    // 同上
    @AliasFor("cacheNames")
    String[] value() default {};
    // 同上
    @AliasFor("value")
    String[] cacheNames() default {};
    // 同上
    String key() default "";
    // 同上
    String keyGenerator() default "";
    // 同上
    String cacheManager() default "";
    // 同上
    String cacheResolver() default "";
    // 同上
    String condition() default "";
    // 这个设置用于指定当前缓存名称名下的所有缓存是否全部删除，默认false。
    boolean allEntries() default false;
    // 这个用于指定删除缓存的操作是否在方法调用之前完成，默认为false，表示先调用方法，在执行缓存删除。
    boolean beforeInvocation() default false;
}
```
#### 4. Cacheing
```
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Caching {
    // 用于指定多个缓存设置操作
    Cacheable[] cacheable() default {};
    // 用于指定多个缓存更新操作
    CachePut[] put() default {};
    // 用于指定多个缓存失效操作
    CacheEvict[] evict() default {};
}
```
Example Use:
```
    @Caching(
        evict = {
            @CacheEvict(value = "animalById", key = "#id"),
            @CacheEvict(value = "animals", allEntries = true, beforeInvocation = true)
        }
    )
```
### 缓存失效的情况
1. 在本类中通过this调用。
2. 检查是否通过了动态代理。

### 扩展
> 通过自定义缓存管理器实现，在缓存的同时自定义过期时间

Example Use:
```
    @Cacheable(value="sys:demo#100", key="#id")     100S过期
    @Cacheable(value="sys:demo#-1", key="#id")      永不过期
    @CachePut(value="sys:student#2000", key="#id")  2000S过期
```



#### 参考文档
[SpringBoot基础系列-SpringCache使用](https://www.jianshu.com/p/6db623355e11)