-- 秒杀LUA
-- 参数校验
if #KEYS ~= 3 or #ARGV ~= 4 then
    return {-10, "参数数量错误"}
end

local product_key = KEYS[1]         -- 商品库存键
local user_record_key = KEYS[2]     -- 用户购买记录键
local blacklist_key = KEYS[3]       -- 黑名单键
local user_id = ARGV[1]             -- 用户ID
local quantity = tonumber(ARGV[2])  -- 购买数量
local max_stock = tonumber(ARGV[3]) -- 最大库存阈值
local user_limit = tonumber(ARGV[4])-- 用户限购次数

-- 检查用户是否在黑名单中
if redis.call('SISMEMBER', blacklist_key, user_id) == 1 then
    return {-1, "用户被限制购买"}
end

-- 获取当前库存
local current_stock = tonumber(redis.call('GET', product_key) or 0)
if current_stock < quantity then
    return {-2, "库存不足"}
end

-- 检查用户购买次数
local user_count = tonumber(redis.call('HGET', user_record_key, user_id) or 0)
if user_count + quantity > user_limit then
    return {-3, "超过用户限购次数"}
end

-- 执行原子操作
redis.call('DECRBY', product_key, quantity)  -- 扣减库存‌:ml-citation{ref="1" data="citationList"}
redis.call('HINCRBY', user_record_key, user_id, quantity)  -- 记录用户购买次数‌:ml-citation{ref="5" data="citationList"}
redis.call('EXPIRE', user_record_key, 86400)  -- 设置购买记录过期时间‌:ml-citation{ref="6" data="citationList"}

-- 触发库存告警逻辑（低于阈值时记录日志）
if current_stock - quantity <= max_stock then
    redis.call('LPUSH', 'stock_alert_queue', product_key)  -- 异步处理库存告警‌:ml-citation{ref="2" data="citationList"}
end

return {1, "抢购成功"}
