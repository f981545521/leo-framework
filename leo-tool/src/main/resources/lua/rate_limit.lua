-- 固定窗口限流
local key = KEYS[1]
local window = tonumber(ARGV[1])
local max = tonumber(ARGV[2])

-- 原子性计数  
local count = redis.call('INCR', key)
if count == 1 then
    redis.call('EXPIRE', key, window)  -- 首次设置过期时间‌:ml-citation{ref="3,5" data="citationList"}  
end

-- 判断是否超限  
return count <= max and 1 or 0  
