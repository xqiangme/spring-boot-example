-- 定义计数变量
local count
-- 获取调用脚本时传入的第一个key值（用作限流的 key）
count = redis.call('get',KEYS[1])
-- 限流最大值比较，若超过最大值，则直接返回
if count and tonumber(count) > tonumber(ARGV[1]) then
return count;
end
-- incr 命令 执行计算器累加
count = redis.call('incr',KEYS[1])
-- 从第一次调用开始限流,并设置失效时间
if tonumber(count) == 1 then
redis.call('expire',KEYS[1],ARGV[2])
end
return count;