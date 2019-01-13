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
curl -X GET 'localhost:9200/_cat/indices?v'

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
curl -X GET 'localhost:9200/get-together/group/_search?q=name:elasticsearch&pretty'

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


# 获取某个索引的映射
curl -H 'Content-type:application/json' -X PUT 'localhost:9200/get-together0/new-events/1?pretty' -d '{
  "name":"Late night with elasticsearch",
  "date":"2018-10-25"
}'

curl -X GET 'localhost:9200/get-together0/_mapping/new-events?pretty'
curl -X GET 'localhost:9200/get-together0/new-events/_mapping?pretty'

# 定义新的映射
curl -H 'content-type:application/json' -X PUT 'localhost:9200/get-together0/_mapping/new-events?pretty' -d '{
  "new-events":{
    "properties":{
      "host":{
        "type":"text"
      }
    }
  }
}'

curl -H 'content-type:application/json' -X PUT 'localhost:9200/employees/' -d '
{
    "mappings":{
        "employee":{
             "properties": {
                 "intro":{"type":"text"}
             }
        }
    }
}'

curl -H 'content-type:application/json' -X PUT 'localhost:9200/employees/employee/_mapping?pretty' -d '
{
  "employee":{
        "properties": {
            "name":{
              "type":"keyword"
          }
        }
  }
}'


curl -H 'content-type:application/json' -X PUT 'localhost:9200/employees/employee/_mapping?pretty' -d '{
  "employee":{
    "properties":{
      "birthday":{
        "type":"date",
        "format":"YYYY MMM DD"
      }
    }
  }
}'

curl -H 'content-type:application/json' -X PUT 'localhost:9200/employees/employee/3?pretty' -d '{
  "name":"123456",
  "birthday":"Oct 25 2018"
}'

curl -X GET 'localhost:9200/employees/_mapping?pretty'
curl -X GET 'localhost:9200/employees/employee/_mapping?pretty'

curl -H 'content-type:application/json' -X PUT 'localhost:9200/employees/employee/1?pretty' -d '{
  "name":"hello world",
  "intro":"ni hao"
}'

curl -H 'content-type:application/json' -X PUT 'localhost:9200/employees/employee/2?pretty' -d '{
  "name":"Enterprise search London get-together",
  "intro":"enterprise search"
}'


curl -X GET 'localhost:9200/employees/employee/_search?q=name:hello&pretty';
curl -X GET 'localhost:9200/employees/employee/_search?q=intro:ni&pretty';
curl -X GET 'localhost:9200/employees/employee/_search?q=enterprise&pretty';
curl -X GET 'localhost:9200/get-together/group/_search?q=enterprise&pretty'

# 获取某个索引的所有数据
curl -X GET 'localhost:9200/get-together/_search?pretty'
curl -X GET 'localhost:9200/get-together0/_search?pretty'
curl -X GET 'localhost:9200/blog/_search?pretty'
curl -X GET 'localhost:9200/new-index/_search?pretty'


# 根据id删除某个文档
curl -X DELETE 'localhost:9200/get-together/group/5'

{
    "_index":"get-together",
    "_type":"group",
    "_id":"5",
    "_version":2,
    "result":"deleted",
    "_shards":{
        "total":2,
        "successful":1,
        "failed":0
    },
    "_seq_no":1,
    "_primary_term":2
}

{
  "_index" : "get-together",
  "_type" : "group",
  "_id" : "5",
  "_version" : 3,
  "result" : "not_found",
  "_shards" : {
    "total" : 2,
    "successful" : 1,
    "failed" : 0
  },
  "_seq_no" : 2,
  "_primary_term" : 2
}

# 根据查询条件删除文档

# 删除类型
curl -X DELETE 'localhost:9200/get-together/group'

# 删除索引
curl -X DELETE 'lcoalhost:9200/blog?pretty'

# 关闭/开启索引
curl -X POST 'localhost:9200/get-together/_close?pretty'
curl -X POST 'localhost:9200/get-together/_open?pretty'

# 只查看name字段
curl -X GET 'localhost:9200/get-together/group/1?pretty&_source=name'

curl -X GET 'localhost:9200/_search?q=_index:get-together'

curl -X GET 'localhost:9200/get-together/_mapping/with_index'

curl -X GET 'localhost:9200/get-together/group/2'

# 更新文档
curl -H 'content-type:application/json' -X POST 'localhost:9200/get-together/group/2/_update' -d '{
  "doc":{
    "Organizer":"Roy"
  }
}'

curl -H 'content-type:application/json' -X POST 'localhost:9200/get-together/group/2/_update' -d '{
  "doc":{
    "organizer":"roy"
  },
  "upsert":{
    "name":"Elasticsearch Denver",
    "organizer":"roy"
  }
}'

# 更新文档：索引文档的方式覆盖文档
curl -H 'content-type:application/json' -X PUT 'localhost:9200/get-together/group/2?version=5&pretty' -d '{
  "Organizer":"fulei04"
}'

# 搜索请求的基本模块，分页：form&size
curl -X GET 'localhost:9200/get-together/group/_search?pretty&size=2'
curl -X GET 'localhost:9200/get-together/group/_search?pretty&from=0&size=1'
curl -X GET 'localhost:9200/get-together/group/_search?pretty&from=1&size=1'

# 搜索请求的基本模块，排序：sort
curl -X GET 'localhost:9200/get-together/group/_search?pretty&sort=created_on'
curl -X GET 'localhost:9200/get-together/group/_search?pretty&sort=created_on:desc'
curl -X GET 'localhost:9200/get-together/group/_search?pretty&sort=created_on:asc'

# 搜索请求的基本模块，限制恢复的source：_source
curl -X GET 'localhost:9200/get-together/group/_search?pretty&_source=name,created_on'
curl -X GET 'localhost:9200/get-together/group/_search?pretty&_source=name'
curl -X GET 'localhost:9200/get-together/group/_search?pretty&q=name:elasticsearch'

# match_all：查询全部文档
curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "match_all":{}
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "bool":{
      "must":{
        "match_all":{
        }
      }
    }
  }
}'

# match：匹配查询
curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "match":{
      "name":"Enterprise"
    }
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "bool":{
      "must":{
        "match":{
          "name":"Enterprise"
        }
      }
    }
  }
}'


[match] query does not support multiple fields, found [description] and [name]
curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "match":{
      "description":"elasticsearch",
      "name":"elasticsearch"
    }
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "match":{
      "name":"Elasticsearch Denver"
    }
  },
  "_source":"name"
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "match":{
      "name":{
        "query":"Elasticsearch Denver",
        "operator":"AND"
      }
    }
  },
  "_source":"name"
}'


# match_phrase
curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "match_phrase":{
      "name":{
        "query":"Enterprise search"
      }
    }
  },
  "_source":"name"
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "match_phrase":{
      "description":{
        "query":"Come required",
        "slop":200
      }
    }
  },
  "_source":"description"
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "match_phrase":{
      "name":{
        "query":"Enterprise London",
        "slop":5
      }
    }
  },
  "_source":"name"
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "match_phrase":{
      "name":{
        "query":"search Enterprise"
      }
    }
  },
  "_source":"name"
}'

# phrase_prefix
curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "match_phrase_prefix":{
      "name":"Elasticsearch den",
    }
  },
  "_source":"name"
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "match_phrase_prefix":{
      "name":{
         "query":"E",
         "max_expansions": 1
      }
    }
  },
  "_source":"name"
}'

# multi_match
curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "multi_match":{
      "query":"Elasticsearch hadoop",
      "fields":["name","description","tags"]
    }
  },
  "_source":["name","description","tags"]
}'


# query_string
curl -X GET 'localhost:9200/get-together/group/_search?pretty&q=nosql'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "query_string":{
      "query":"nosql"
    }
  }
}'

curl -X GET 'localhost:9200/get-together/group/_search?pretty&q=description:nosql'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "query_string":{
      "query":"nosql",
      "default_field":"description"
    }
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "query_string":{
      "query":"elasticsearch san francisco",
      "default_field":"name",
      "default_operator":"AND"
    }
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "query_string":{
      "query":"name:elasticsearch AND name:san AND name:francisco"
    }
  }
}'

# term
curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "term":{
      "name":"Elasticsearch"
    }
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "match":{
      "name":"Elasticsearch"
    }
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "bool":{
      "must":{
        "match_all":{}
      }
      "filter":{
        "term":{
          "tags":"elasticsearch"
        }
      }
    }
  }
}'

# terms
minimum_should_match 已废弃，可以用should替代

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "terms":{
      "tags":["jvm","java","hadoop"]
    }
  },
  "_source":["tags"]
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "bool":{
      "minimum_should_match":1,
      "should":[
        {
          "term":{
            "tags":"jvm"
          }
        },
        {
          "term":{
            "tags":"java"
          }
        },
        {
          "term":{
            "tags":"hadoop"
          }
        }
      ]
    }
  },
  "_source":["tags"]
}'

# bool 查询：mush、must_not、should查询
curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "bool":{
      "must":[
        {
          "term":{
            "tags":"java"
          }
        }
      ]
    }
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "bool":{
      "must":[
        {
          "term":{
            "members":"lee"
          }
        }
      ]
    }
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "bool":{
      "should":[
        {
          "term":{
            "members":"lee"
          }
        },
        {
          "term":{
            "members":"mike"
          }
        }
      ]
    }
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "bool":{
      "must_not":[
        {
          "range":{
            "created_on":{
                "lt":"2010"
            }
          }
        }
      ]
    }
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "bool":{
      "must":[
        {
          "term":{
            "members":"lee"
          }
        }
      ],
      "should":[
        {
          "term":{
            "members":"igor"
          }
        },
        {
          "term":{
            "members":"mike"
          }
        }
      ],
      "must_not":[
        {
          "range":{
            "created_on":{
              "lt":"2012-08"
            }
          }
        }
      ],
      "minimum_should_match":"50%"
    }
  }
}'


curl -H 'Content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "bool":{
      "filter":{
        "match":{
        "name":"elasticsearch"
        }
      }
    }
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "bool":{
        "filter":{
          "bool":{
            "should":[
              {
                "term":{
                  "members":"igor"
                }
              },
              {
                "term":{
                  "members":"mike"
                }
              }
            ],
            "minimum_should_match":1
          }
        }
      }
    }
  }
}'

# wildcard 查询
curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "wildcard":{
      "description":"grou*"
    }
  }
}'

curl -X GET 'localhost:9200/get-together/group/_search?pretty'

# 字段存在性
curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "bool":{
      "filter":{
        "exists":{
          "field":"members"
        }
      }
    }
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "bool":{
      "filter":{
        "exists":{
          "field":"Organizer"
        }
      }
    }
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "bool":{
      "must":{
        "exists":{
          "field":"members"
        }
      }
    }
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "bool":{
      "must_not":{
        "exists":{
          "field":"members"
        }
      }
    }
  }
}'

# 将任何查询转变为过滤器
curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "bool":{
      "filter":{
        "query":{
          "query_string":{
            "query":"name:denver"
          }
        }
      }
    }
  }
}'


curl -X GET 'localhost:9200/get-together/group/_search'

curl -X GET 'localhost:9200/get-together/_mapping?pretty'

curl -X GET 'localhost:9200/get-together/group/_search?pretty&from=0&size=1'
curl -X GET 'localhost:9200/get-together/group/_search?pretty&from=1&size=1'

# increase decrease
curl -X GET 'localhost:9200/get-together/group/_search?pretty&sort=created_on:asc'
curl -X GET 'localhost:9200/get-together/group/_search?pretty&sort=created_on:desc'

curl -X GET 'localhost:9200/get-together/group/_search?pretty&_source=Organizer'



curl -X GET 'localhost:9200/get-together/group/_search?pretty&q=elasticse'
curl -X GET 'localhost:9200/get-together/group/_search?pretty&q=elasticsearch'
curl -X GET 'localhost:9200/get-together/group/_search?pretty&q=description:elasticsearch'


curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query" : {
    "match_all" : {}
  },
  "from":1,
  "size":1
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query" : {
    "match_all" : {}
  },
  "_source":["name","date"]
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query" : {
    "match_all" : {}
  },
  "_source":{
    "include":["location_*","date"],
    "exclude":["location_geo*"]
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query" : {
    "match_all" : {}
  },
  "_source":{
    "include":["location_*","date"],
    "exclude":["location_geo*"]
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query" : {
    "match_all" : {}
  },
  "sort":[
    {"created_on":"asc"},
    "_score"
  ]
}'


curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "match" :{
      "tags":"hadoop"
    }
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "match" :{
      "tags":"Hadoop"
    }
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "term" :{
      "tags":"hadoop"
    }
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "query_string" :{
      "query":"nosql"
    }
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "query_string" :{
      "query":"elasticsearch"
    }
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "query_string" :{
      "query":"description:elasticsearch"
    }
  }
}'


curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "query_string" :{
      "default_field":"description",
      "query":"elasticsearch"
    }
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "query_string" :{
      "query":"-Organizer:fulei04"
    }
  }
}'


curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "query_string" :{
      "query":"elasticsearch AND -organizer:roy"
    }
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "terms" :{
      "tags":["big","data"]
    }
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "bool":{
      "must":{
        "match_all":{}
      },
      "filter":{
        "term" :{
          "tags":"hadoop"
        }
      }
    }
  }
}'


curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "match" :{
      "name":{
        "query":"elasticsearch denver",
        "operator":"or"
      }
    }
  },
  "_source":"name"
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "match_phrase":{
      "description":{
        "query":"Come required",
        "slop":5
      }
    }
  },
  "_source":"description"
}'


curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "match_phrase_prefix":{
      "name":{
         "query":"s",
         "max_expansions": 2
      }
    }
  },
  "_source":"name"
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "match_phrase":{
      "description":{
        "query":"Come required",
        "slop":5
      }
    }
  },
  "_source":"description"
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "multi_match":{
      "query":"elasticsearch",
      "fields":["name","description"]
    }
  },
  "_source":["name","description"]
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "bool":{
      "must":[
        {
          "term":{
            "members":"lee"
          }
        }
      ]
    }
  },
  "_source":["tags"]
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "bool":{
      "should":[
        {
          "term":{
            "members":"lee"
          }
        },
        {
          "term":{
            "members":"daniel"
          }
        }
      ],
      "minimum_should_match":2
    }
  },
  "_source":["members"]
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "bool":{
      "must_not":[
        {
          "term":{
            "members":"lee"
          }
        }
      ]
    }
  },
  "_source":["members"]
}'


curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "bool":{
      "must":{
        "match_all":{}
      },
      "filter":{
        "exists":{
          "field":"members"
        }
      }
    }
  }
}'

curl -H 'content-type:application/json' -X POST 'localhost:9200/get-together/group/3/_update' -d '{
  "doc":{
    "created_on":null
  }
}'

curl -X GET 'localhost:9200/get-together/group/_search?pretty'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "bool":{
      "must":{
        "match_all":{}
      },
      "filter":{
        "bool":{
          "must":{
            "exists":{
              "field":"members"
            }
          }
        }
      }
    }
  }
}'

curl -H 'content-type:application/json' -X GET 'localhost:9200/get-together/group/_search?pretty' -d '{
  "query":{
    "bool":{
      "must":{
        "match_all":{}
      },
      "filter":{
        "bool":{
          "must":{
            "exists":{
              "field":"members"
            }
          }
        }
      }
    }
  }
}'