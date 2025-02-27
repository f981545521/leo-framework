local current_year_month = tonumber(ARGV[1])
local score_value = tonumber(ARGV[2]) or 0
local cache_key = ARGV[3]
local year_month_str = tostring(current_year_month)
if score_value > 0 then
    redis.call('ZINCRBY', cache_key, score_value, year_month_str)
end
if score_value < 0 then
    local tmp = redis.call('ZSCORE', cache_key, year_month_str)
    if tmp == nil then
        return -1
    end
    local score = tonumber(tmp)
    if score <= 0 then
        return -1
    end
    local new_score = score + score_value
    if new_score >= 0 then
        redis.call('ZADD', cache_key, new_score, year_month_str)
    else
        redis.call('ZADD', cache_key, 0, year_month_str)
    end
end

local total_score = 0
local res = {}

local members_with_scores = redis.call('ZRANGE', cache_key, 0, -1, 'WITHSCORES')

for i = 1, #members_with_scores, 2 do
    local member = members_with_scores[i]
    local score = members_with_scores[i + 1]
    local key_year_month = tonumber(string.sub(member, -6))
    local year_diff = math.floor(current_year_month / 100) - math.floor(key_year_month / 100)
    local month_diff = (current_year_month % 100) - (key_year_month % 100)
    local total_month_diff = year_diff * 12 + month_diff
    if total_month_diff > 13 then
        redis.call('ZREM', cache_key, member)
        res[#res+1] = tostring(member)
    else
        local score_value = tonumber(score)
        if score_value then
            total_score = total_score + score_value
        end
    end
end

return {total_score, table.concat(res, ',')}