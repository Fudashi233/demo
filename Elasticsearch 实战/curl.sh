# Elasticsearch节点是否启动
http://localhost:9200/

# 向【get-together】索引，【group】类型新增一个文档
curl -H "Content-Type: application/json" -X PUT 'localhost:9200/get-together/group/1?pretty' -d '{
    "name":"Elasticsearch Denver",
    "orginzer":"Lee"
}'

{
  "_index" : "get-together", # 索引
  "_type" : "group", # 类型
  "_id" : "1",
  "_version" : 1, # 版本
  "result" : "created",
  "_shards" : {
    "total" : 2,
    "successful" : 1,
    "failed" : 0
  },
  "_seq_no" : 0,
  "_primary_term" : 1
}

curl -H "Content-Type: application/json" -s -XPOST "localhost:9200/get-together/group/1" -d'{
  "name": "Denver Clojure",
  "organizer": ["Daniel", "Lee"],
  "description": "Group of Clojure enthusiasts from Denver who want to hack on code together and learn more about Clojure",
  "created_on": "2012-06-15",
  "tags": ["clojure", "denver", "functional programming", "jvm", "java"],
  "members": ["Lee", "Daniel", "Mike"],
  "location_group": "Denver, Colorado, USA"
}'

curl -H "Content-Type: application/json" -s -XPOST "localhost:9200/get-together/group/2" -d'{
  "name": "Elasticsearch Denver",
  "organizer": "Lee",
  "description": "Get together to learn more about using Elasticsearch, the applications and neat things you can do with ES!",
  "created_on": "2013-03-15",
  "tags": ["denver", "elasticsearch", "big data", "lucene", "solr"],
  "members": ["Lee", "Mike"],
  "location_group": "Denver, Colorado, USA"
}'

curl -H "Content-Type: application/json" -s -XPOST "localhost:9200/get-together/group/3" -d'{
  "name": "Elasticsearch San Francisco",
  "organizer": "Mik",
  "description": "Elasticsearch group for ES users of all knowledge levels",
  "created_on": "2012-08-07",
  "tags": ["elasticsearch", "big data", "lucene", "open source"],
  "members": ["Lee", "Igor"],
  "location_group": "San Francisco, California, USA"
}'

curl -H "Content-Type: application/json" -s -XPOST "localhost:9200/get-together/group/4" -d'{
  "name": "Boulder/Denver big data get-together",
  "organizer": "Andy",
  "description": "Come learn and share your experience with nosql & big data technologies, no experience required",
  "created_on": "2010-04-02",
  "tags": ["big data", "data visualization", "open source", "cloud computing", "hadoop"],
  "members": ["Greg", "Bill"],
  "location_group": "Boulder, Colorado, USA"
}'

curl -H "Content-Type: application/json" -s -XPOST "localhost:9200/get-together/group/5" -d'{
  "name": "Enterprise search London get-together",
  "organizer": "Tyler",
  "description": "Enterprise search get-togethers are an opportunity to get together with other people doing search.",
  "created_on": "2009-11-25",
  "tags": ["enterprise search", "apache lucene", "solr", "open source", "text analytics"],
  "members": ["Clint", "James"],
  "location_group": "London, England, UK"
}'

# 创建索引
curl -H "Content-Type: application/json" -X PUT 'localhost:9200/new-index'

# 查询所有索引
curl 'localhost:9200/_cat/indices?v'

health status index        uuid                   pri rep docs.count docs.deleted store.size pri.store.size
yellow open   blog         4m40ndjWSgKTr66iKmLpCw   5   1          0            0      1.2kb          1.2kb
yellow open   get-together YK2kZB6HQHG9Brteb-GOmw   5   1          1            0      5.1kb          5.1kb
yellow open   new-index    I7KFngejRc-p8eWnSZWaew   5   1          0            0      1.1kb          1.1kb

# 获取某个类型的映射（字段定义）
curl -X GET 'localhost:9200/get-together/_mapping/group?pretty'

{
  "get-together" : {
    "mappings" : {
      "group" : {
        "properties" : {
          "name" : {
            "type" : "text",
            "fields" : {
              "keyword" : {
                "type" : "keyword",
                "ignore_above" : 256
              }
            }
          },
          "orginzer" : {
            "type" : "text",
            "fields" : {
              "keyword" : {
                "type" : "keyword",
                "ignore_above" : 256
              }
            }
          }
        }
      }
    }
  }
}

# 查询某个索引下（get-together）、某个类型下（group）、某个字段下（name、location_group），包含elasticsearch内容的文档
curl -X GET 'localhost:9200/get-together/group/_search?q=elasticsearch&_source=name,location_group&size=1&pretty'
curl -X GET 'localhost:9200/get-together/group/_search?q=elasticsearch&_source=name,location_group&pretty'
curl -X GET 'localhost:9200/get-together/group/_search?q=elasticsearch&_source=name&pretty'
curl -X GET 'localhost:9200/get-together/group/_search?q=elasticsearch&pretty'
curl -X GET 'localhost:9200/get-together/group/_search?pretty'
curl -X GET 'localhost:9200/get-together/group/2?pretty'

{
  "took" : 5, # 检索时间
  "timed_out" : false, # 是否超时
  "_shards" : { # 分片信息
    "total" : 5, # 总计分片数
    "successful" : 5, # 成功返回的分片数
    "skipped" : 0, 
    "failed" : 0 # 失败的分片数
  },
  "hits" : {
    "total" : 2, # 匹配的文档数
    "max_score" : 0.87138504, # 最高分
    "hits" : [
      {
        "_index" : "get-together",
        "_type" : "group",
        "_id" : "2", # 文本id
        "_score" : 0.87138504,
        "_source" : { # 被检索到的字段与文本内容
          "name" : "Elasticsearch Denver"
        }
      }
    ]
  }
}

# query_string 查询
curl -X GET 'localhost:9200/get-together/group/_search?pretty'

curl -H "Content-Type: application/json" -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "query_string":{
      "query":"elasticsearch"
    }
  }
}'


curl -H "Content-Type: application/json" -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "query_string":{
      "query":"Francisco",
      "default_field":"name"
    }
  }
}'


curl -H "Content-Type: application/json" -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "query_string":{
      "query":"Elasticsearch Francisco",
      "default_field":"name"
    }
  }
}'

curl -H "Content-Type: application/json" -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "query_string":{
      "query":"Elasticsearch Francisco",
      "default_field":"name",
      "default_operator":"AND"
    }
  }
}'

curl -H "Content-Type: application/json" -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "query_string":{
      "query":"name:elasticsearch AND name:san AND name:francisco"
    }
  }
}'

# term 查询
curl -H 'Content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "term":{
      "name":"elasticsearch"
    }
  }
}'

curl -H 'Content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty&size=1' -d '{
  "query":{
    "term":{
      "name":"elasticsearch"
    }
  }
}'

# bool 查询
curl -H 'Content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "bool":{
      "filter":{
        "term":{
          "name":"elasticsearch"
        }
      }
    }
  }
}'

# organizers 聚集，需事先设置fielddata为true
curl -H 'Content-type:application/json' -X PUT 'localhost:9200/get-together/_mapping/group?pretty' -d '{
  "properties":{
    "organizer":{
      "type":"text",
      "fielddata":true
    }
  }
}'

curl -H 'Content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "aggregations":{
    "organizers":{
      "terms":{
        "field":"organizer"
      }
    }
  }
}'

{
    "took":31,
    "timed_out":false,
    "_shards":{
        "total":5,
        "successful":5,
        "skipped":0,
        "failed":0
    },
    "hits":{
        "total":5,
        "max_score":1,
        "hits":[
            {
                "_index":"get-together",
                "_type":"group",
                "_id":"5",
                "_score":1,
                "_source":{
                    "name":"Enterprise search London get-together",
                    "organizer":"Tyler",
                    "description":"Enterprise search get-togethers are an opportunity to get together with other people doing search.",
                    "created_on":"2009-11-25",
                    "tags":[
                        "enterprise search",
                        "apache lucene",
                        "solr",
                        "open source",
                        "text analytics"
                    ],
                    "members":[
                        "Clint",
                        "James"
                    ],
                    "location_group":"London, England, UK"
                }
            },
            {
                "_index":"get-together",
                "_type":"group",
                "_id":"2",
                "_score":1,
                "_source":{
                    "name":"Elasticsearch Denver",
                    "organizer":"Lee",
                    "description":"Get together to learn more about using Elasticsearch, the applications and neat things you can do with ES!",
                    "created_on":"2013-03-15",
                    "tags":[
                        "denver",
                        "elasticsearch",
                        "big data",
                        "lucene",
                        "solr"
                    ],
                    "members":[
                        "Lee",
                        "Mike"
                    ],
                    "location_group":"Denver, Colorado, USA"
                }
            },
            {
                "_index":"get-together",
                "_type":"group",
                "_id":"4",
                "_score":1,
                "_source":{
                    "name":"Boulder/Denver big data get-together",
                    "organizer":"Andy",
                    "description":"Come learn and share your experience with nosql & big data technologies, no experience required",
                    "created_on":"2010-04-02",
                    "tags":[
                        "big data",
                        "data visualization",
                        "open source",
                        "cloud computing",
                        "hadoop"
                    ],
                    "members":[
                        "Greg",
                        "Bill"
                    ],
                    "location_group":"Boulder, Colorado, USA"
                }
            },
            {
                "_index":"get-together",
                "_type":"group",
                "_id":"1",
                "_score":1,
                "_source":{
                    "name":"Denver Clojure",
                    "organizer":[
                        "Daniel",
                        "Lee"
                    ],
                    "description":"Group of Clojure enthusiasts from Denver who want to hack on code together and learn more about Clojure",
                    "created_on":"2012-06-15",
                    "tags":[
                        "clojure",
                        "denver",
                        "functional programming",
                        "jvm",
                        "java"
                    ],
                    "members":[
                        "Lee",
                        "Daniel",
                        "Mike"
                    ],
                    "location_group":"Denver, Colorado, USA"
                }
            },
            {
                "_index":"get-together",
                "_type":"group",
                "_id":"3",
                "_score":1,
                "_source":{
                    "name":"Elasticsearch San Francisco",
                    "organizer":"Mik",
                    "description":"Elasticsearch group for ES users of all knowledge levels",
                    "created_on":"2012-08-07",
                    "tags":[
                        "elasticsearch",
                        "big data",
                        "lucene",
                        "open source"
                    ],
                    "members":[
                        "Lee",
                        "Igor"
                    ],
                    "location_group":"San Francisco, California, USA"
                }
            }
        ]
    },
    "aggregations":{
        "organizers":{
            "doc_count_error_upper_bound":0,
            "sum_other_doc_count":0,
            "buckets":[
                {
                    "key":"lee",
                    "doc_count":2
                },
                {
                    "key":"andy",
                    "doc_count":1
                },
                {
                    "key":"daniel",
                    "doc_count":1
                },
                {
                    "key":"mik",
                    "doc_count":1
                },
                {
                    "key":"tyler",
                    "doc_count":1
                }
            ]
        }
    }
}

# 通过id来获取文档
curl -X GET 'localhost:9200/get-together/group/1?pretty'
{
  "_index" : "get-together",
  "_type" : "group",
  "_id" : "1",
  "_version" : 1,
  "found" : true,
  "_source" : {
    "name" : "Denver Clojure",
    "organizer" : [
      "Daniel",
      "Lee"
    ],
    "description" : "Group of Clojure enthusiasts from Denver who want to hack on code together and learn more about Clojure",
    "created_on" : "2012-06-15",
    "tags" : [
      "clojure",
      "denver",
      "functional programming",
      "jvm",
      "java"
    ],
    "members" : [
      "Lee",
      "Daniel",
      "Mike"
    ],
    "location_group" : "Denver, Colorado, USA"
  }
}

curl -X GET 'localhost:9200/get-together/group/7?pretty'
{
  "_index" : "get-together",
  "_type" : "group",
  "_id" : "7",
  "found" : false
}