-- KEYS‌:ml-citation{ref="1" data="citationList"}：限流键名（如 "rate_limit:/api"）  
-- ARGV‌:ml-citation{ref="1" data="citationList"}：时间窗口长度（秒）  
-- ARGV‌:ml-citation{ref="2" data="citationList"}：允许的最大请求数  
-- ARGV‌:ml-citation{ref="3" data="citationList"}：当前时间戳（毫秒级，建议客户端生成）  

local key = KEYS[1]
local window = tonumber(ARGV[1]) * 1000  -- 转换为毫秒
local max = tonumber(ARGV[2])
local now = tonumber(ARGV[3])

-- 清理窗口外的旧请求记录  
redis.call('ZREMRANGEBYSCORE', key, 0, now - window)

-- 统计当前窗口内的请求总数  
local current = redis.call('ZCARD', key)

if current >= max then
    return 0  -- 触发限流  
else
    -- 添加唯一请求标识（时间戳+随机数防止覆盖）  
    redis.call('ZADD', key, now, now .. ':' .. math.random(1000000))
    -- 设置键过期时间（窗口长度 + 冗余 1 秒）  
    redis.call('PEXPIRE', key, window + 1000)
    return 1  -- 允许请求  
end  
