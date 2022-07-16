package apt.project.QueryProcessor;

import redis.clients.jedis.Jedis;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.min;

class Suggestions {
    private final Jedis jedis;

    public Suggestions() {
        //Connecting to Redis server on localhost
        jedis = new Jedis("localhost");
        jedis.select(1);
        jedis.set("query_count", "0");
    }


    public void insertQuery(String query)
    {
        query = query.toLowerCase();

        String index = jedis.hget("suggest:invertedQueries", query);
        if(index == null)
        {
            String query_count = jedis.get("query_count");
            jedis.hset("suggest:queries", Map.of(query_count, query));
            jedis.hset("suggest:invertedQueries", Map.of(query, query_count));

            index = query_count;
            jedis.incr("query_count");
        }

        for(int i = 0; i < query.length(); i++) {
            String prefix = query.substring(0, i);
            jedis.zincrby("suggest:words:"+prefix, 1, index);
        }
    }

    public List<String> retrieveSuggestions(String prefix, int top)
    {
        List<String> suggestions = new LinkedList<>();
        prefix=prefix.toLowerCase();

        long cardinality = jedis.zcard("suggest:words:"+prefix);

        top = min((int)cardinality, top);

        List<String> indices = jedis.zrevrange("suggest:words:"+prefix, 0, top-1);

        for (String index : indices)
        {
            suggestions.add(jedis.hget("suggest:queries", index));
        }

        return suggestions;
    }
}