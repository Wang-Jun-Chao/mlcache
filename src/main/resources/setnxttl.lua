-- 首先尝试使用setnx设置值
local result = redis.call('setnx', KEYS[1], ARGV[1]);
-- 如果成功，则设置key的失效时间
if result == 1 then
    local pexpire = redis.call('pexpire', KEYS[1], tonumber(ARGV[2]));
    if pexpire == 1 then
        -- 设置成功的话就返回空值，与后面返回的失效时间区别开
        return true;
    else
        -- 设置失败，删除原来的key，避免key长期有效
        redis.call('del', KEYS[1])
        return false
    end
else
    -- key操作失败，直接返回：0
    return false
end
