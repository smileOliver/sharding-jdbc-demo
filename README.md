# sharding-jdbc-demo（集成多数据源）
**框架：springboot+mybatis-plus+sharding-jdbc**
JDK1.8
# 数据库准备
1、创建三个数据库，两个用来做分库分表，一个做主库，因为我们后面要实现的是动态多数据源的分库分表实战
2、在两个数据库中分别创建表，要注意分表后缀
```
#两个数据库分表创建
CREATE TABLE user_config
(
    `id`           bigint(20)    NOT NULL AUTO_INCREMENT,
    `user_id`      bigint(20)    NOT NULL COMMENT '用户id',
    `name`         varchar(256)  NOT NULL COMMENT '配置名',
    `content`      longtext      NOT NULL COMMENT '配置项',
    `config_desc`  varchar(1024) NOT NULL COMMENT '配置描述',
    `gmt_create`   datetime               DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户配置表';
  
  
#两个数据库分表创建三个表 user_0,user_1,user_2
CREATE TABLE `user`  
(
    `id`           bigint(20) NOT NULL,
    `name`         varchar(64)         DEFAULT NULL COMMENT '名称',
    `city_id`      int                 DEFAULT NULL COMMENT '城市',
    `sex`          tinyint(1)          DEFAULT NULL COMMENT '性别',
    `phone`        varchar(32)         DEFAULT NULL COMMENT '电话',
    `email`        varchar(32)         DEFAULT NULL COMMENT '邮箱',
    `password`     varchar(32)         DEFAULT NULL COMMENT '密码',
    `gmt_create`   datetime            DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_name_idx` (`name`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户表';

#两个数据库分表创建四个表 t_order_0,t_order_1,t_order_2,t_order_3
CREATE TABLE `t_order`   
(
    `order_id`     bigint(20)  NOT NULL,
    `order_type`   int         NOT NULL COMMENT '订单类型',
    `user_id`      bigint(20)  NOT NULL COMMENT '用户id',
    `user_name`    varchar(64) NOT NULL COMMENT '用户名',
    `gmt_create`   datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`order_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='订单表';
  
  
#两个数据库分表创建四个表 order_item_0,order_item_1,order_item_2,order_item_3
CREATE TABLE `order_item`
(
    `order_id`     bigint(20)     NOT NULL COMMENT '订单编码',
    `type`         int            NOT NULL COMMENT '订单类型',
    `user_id`      bigint(20)     NOT NULL COMMENT '用户id',
    `user_name`    varchar(64)    NOT NULL COMMENT '用户名',
    `goods_id`     bigint(20)     NOT NULL COMMENT '商品id',
    `goods_name`   varchar(1024)  NOT NULL COMMENT '商品名称',
    `goods_count`  int(11)        NOT NULL DEFAULT '0' COMMENT '商品个数',
    `goods_price`  decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '商品价格',
    `gmt_create`   datetime                DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='订单表';
  
  
#在主库中创建
CREATE TABLE `warehouse`
(
    `id`                bigint(20)  NOT NULL,
    `warehouse_name`    varchar(64) NOT NULL COMMENT '仓库名',
    `warehouse_code`    varchar(64) NOT NULL COMMENT '仓库编码',
    `warehouse_address` varchar(512)         default '' COMMENT '仓库地址',
    `gmt_create`        datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`      datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `warehouse_code_idx` (`warehouse_code`) USING BTREE,
    KEY `warehouse_warehouse_name_IDX` (`warehouse_name`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='仓库信息表';
```
# 1、引入依赖
采用的是sharding-jdbc 4.1.1版本

```
<dependency>
     <groupId>org.apache.shardingsphere</groupId>
     <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
     <version>4.1.1</version>
 </dependency>
 ```
 项目中使用到了baomidou团队开源的mybatis-plus，其团队还开源了一个多数据源的组件：dynamic-datasource-spring-boot-starter，集成后，使用@DS注解就可以切换数据源.
 ```
 <dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>${mybatis-plus.version}</version>
</dependency>
<!-- https://mvnrepository.com/artifact/com.baomidou/dynamic-datasource-spring-boot-starter -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
    <version>${dynamic-datasource.version}</version>
</dependency>
 ```
 # 2、多数据源配置
 核心思路是将sharding jdbc数据源，加入到多数据源中。
 ```
 /**
 * 动态数据源配置：
 *
 *使用{@link com.baomidou.dynamic.datasource.annotation.DS}注解，切换数据源
 *
 *<code>@DS(DataSourceConfiguration.SHARDING_DATA_SOURCE_NAME)</code>
 **/
@Configuration
@AutoConfigureBefore({DynamicDataSourceAutoConfiguration.class,
        SpringBootConfiguration.class})
public class DataSourceConfig {

    /**
     * 分表数据源名称
     */
    public static final String SHARDING_DATA_SOURCE_NAME = "data_sharding";
    /**
     * shardingjdbc有四种数据源，需要根据业务注入不同的数据源
     *
     * <p>1. 未使用分片, 脱敏的名称(默认): shardingDataSource;
     * <p>2. 主从数据源: masterSlaveDataSource;
     * <p>3. 脱敏数据源：encryptDataSource;
     * <p>4. 影子数据源：shadowDataSource
     */
    @Lazy
    @Resource(name = "shardingDataSource")
    AbstractDataSourceAdapter shardingDataSource;
    /**
     * 动态数据源配置项
     */
    @Autowired
    private DynamicDataSourceProperties dynamicDataSourceProperties;

    @Bean
    public DynamicDataSourceProvider dynamicDataSourceProvider() {
        Map<String, DataSourceProperty> datasourceMap = dynamicDataSourceProperties.getDatasource();
        return new AbstractDataSourceProvider() {
            @Override
            public Map<String, DataSource> loadDataSources() {
                Map<String, DataSource> dataSourceMap = createDataSourceMap(datasourceMap);
                // 将 shardingjdbc 管理的数据源也交给动态数据源管理
                dataSourceMap.put(SHARDING_DATA_SOURCE_NAME, shardingDataSource);
                return dataSourceMap;
            }
        };
    }

    /**
     * 将动态数据源设置为首选的
     * 当spring存在多个数据源时, 自动注入的是首选的对象
     * 设置为主要的数据源之后，就可以支持shardingjdbc原生的配置方式了
     *
     * @return
     */
    @Primary
    @Bean
    public DataSource dataSource(DynamicDataSourceProvider dynamicDataSourceProvider) {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.setPrimary(dynamicDataSourceProperties.getPrimary());
        dataSource.setStrict(dynamicDataSourceProperties.getStrict());
        dataSource.setStrategy(dynamicDataSourceProperties.getStrategy());
        dataSource.setProvider(dynamicDataSourceProvider);
        dataSource.setP6spy(dynamicDataSourceProperties.getP6spy());
        dataSource.setSeata(dynamicDataSourceProperties.getSeata());
        return dataSource;
    }
}
 ```
 >>
sharding jdbc有四种数据源：
1. 未使用分片, 脱敏的名称(默认): shardingDataSource;
2. 主从数据源: masterSlaveDataSource;
3. 脱敏数据源：encryptDataSource;
4. 影子数据源：shadowDataSource
需要需要根据不同的场景，注入不同的数据源，本次以分表举例，所以将shardingDataSource放到了多数据源（dataSourceMap）中。

# 3、数据源配置
在application.yml中配置，这里设置了默认数据源---使用了no_sharding找个数据库
```
spring:
  datasource:
    # 动态数据源配置
    dynamic:
      datasource:
        master:
          type: com.alibaba.druid.pool.DruidDataSource
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://localhost:3306/no_sharding?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
          username: root
          password: wk123456
      primary: master
  profiles:
    active: sharding-jdbc

logging:
  level:
    com:
      github:
        dudiao:
          sharding: debug
#mybatis-plus配置
mybatis-plus:
  mapper-locations: classpath*:/mapper/*Mapper.xml
  configuration:
    map-underscore-to-camel-case: true
    # use-generated-keys: true
    default-fetch-size: 100
    default-statement-timeout: 30
 ```
 然后新建application-sharding-jdbc.yml进行分库分表配置
 ```
 spring:
  #分库分表配置
  shardingsphere:
    datasource:
      #多个用英文逗号隔开
      names: ds0,ds1
      ds0:
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://localhost:3306/sharding-jdbc1?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
        username: root
        password: wk123456
      ds1:
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://localhost:3306/sharding-jdbc2?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
        username: root
        password: wk123456
    #配置分库分表规则
    sharding:
      tables:
        #用户表 逻辑表名
        user:
          #真实表 ds0.user_0
          #${begin..end}表示范围区间
          #${[unit1, unit2, unit_x]}表示枚举值
          actualDataNodes: ds$->{0..1}.user_$->{0..2}
          #对应的逻辑表
          logicTable: user
          #分库策略
          databaseStrategy:
            #行表达式策略 需符合groovy语法
            inline:
              #分片列名称
              shardingColumn: id
              #分片算法行表达式
              algorithmExpression: ds$->{id % 2}
          #分表规则
          tableStrategy:
            inline:
              algorithmExpression: user_$->{id % 3}
              shardingColumn: id
          #分布式id
          keyGenerator:
            column: id
            type: SNOWFLAKE
        t_order:
          actualDataNodes: ds$->{0..1}.t_order_$->{0..3}
          databaseStrategy:
            inline:
              algorithmExpression: ds$->{user_id % 2}
              shardingColumn: user_id
          logicTable: t_order
          tableStrategy:
            #标准分片算法 SQL 语句中有>，>=, <=，<，=，IN 和 BETWEEN AND 操作符，都可以应用此分片策略
            standard:
              #精确分片算法类名称
              preciseAlgorithmClassName: com.harmony.shardingjdbc.sharding.Algorithm.PreciseTableShardingAlgorithm
              shardingColumn: order_id
              #范围分片算法类名称，用于BETWEEN，可选
              #rangeAlgorithmClassName: com.harmony.shardingjdbc.sharding.Algorithm.RangeTableShardingAlgorithm
              #Hint分片策略
              #hint:
              #Hint分片算法类名称
              #algorithmClassName: com.harmony.shardingjdbc.sharding.Algorithm.HintTableShardingAlgorithm
              #complex:
              #分片列名称，多个列以逗号分隔
              #shardingColumns: user_id,order_id
              #复合分片算法类名称。该类需实现ComplexKeysShardingAlgorithm接口并提供无参数的
              #algorithmClassName: com.harmony.shardingjdbc.sharding.Algorithm.RangeTableShardingAlgorithm


          keyGenerator:
            column: order_id
            type: SNOWFLAKE
        order_item:
          actualDataNodes: ds$->{0..1}.order_item_$->{0..3}
          databaseStrategy:
            inline:
              algorithmExpression: ds$->{user_id % 2}
              shardingColumn: user_id
          logicTable: order_item
          tableStrategy:
            inline:
              algorithmExpression: order_item_$->{order_id % 4}
              shardingColumn: order_id
      #绑定关系表
      binding-tables:
        - t_order,order_item
      #广播表
      broadcastTables:
        - user_config
      #默认使用雪花算法生成分布式ID
      default-key-generator:
        column: id
        type: SNOWFLAKE
    props:
      #日志显示SQL
      sql.show: true
 ```
 # 4、使用
 若该表需要分片则在对应mapper添加上注解 @DS(DataSourceConfig.SHARDING_DATA_SOURCE_NAME)
 ```
 /**
 * 若该表需要分片则添加上注解 @DS(DataSourceConfig.SHARDING_DATA_SOURCE_NAME)
 *
 * @author wangkuan
 */
@DS(DataSourceConfig.SHARDING_DATA_SOURCE_NAME)
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
}
 ```
 然后基本的CRUD、分页操作自己测试验证下吧
 # 5、算法
 ## 行表达式分片策略
 行表达式分片策略（InlineShardingStrategy），在配置中使用 Groovy 表达式，提供对 SQL语句中的 = 和 IN 的分片操作支持，它只支持单分片健。
行表达式分片策略适用于做简单的分片算法，无需自定义分片算法，省去了繁琐的代码开发，是几种分片策略中最为简单的。
它的配置相当简洁，这种分片策略利用inline.algorithm-expression书写表达式。
比如：ds-$->{order_id % 2} 表示对 order_id 做取模计算，$ 是个通配符用来承接取模结果，最终计算出分库ds-0 ··· ds-n，整体来说比较简单。

## 标准分片策略
使用场景：SQL 语句中有>，>=, <=，<，=，IN 和 BETWEEN AND 操作符，都可以应用此分片策略。
标准分片策略（StandardShardingStrategy），它只支持对单个分片健（字段）为依据的分库分表，并提供了两种分片算法 PreciseShardingAlgorithm（精准分片）和 RangeShardingAlgorithm（范围分片）。
在使用标准分片策略时，精准分片算法是必须实现的算法，用于 SQL 含有 = 和 IN 的分片处理；范围分片算法是非必选的，用于处理含有 BETWEEN AND 的分片处理。
>一旦我们没配置范围分片算法，而 SQL 中又用到 BETWEEN AND 或者 like等，那么 SQL 将按全库、表路由的方式逐一执行，查询性能会很差需要特别注意。
### 精准分片算法
实现自定义精准分库、分表算法的方式大致相同，都要实现 PreciseShardingAlgorithm 接口，并重写 doSharding() 方法，只是配置稍有不同，而且它只是个空方法，得我们自行处理分库、分表逻辑。其他分片策略亦如此。
通过对分片健 order_id 取模的方式计算出 SQL 该路由到哪个库，计算出的分片库信息会存放在分片上下文中，方便后续分表中使用。
```
@Service
public class PreciseTableShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    /**
     * availableTargetNames 所有分片库的集合
     * shardingValue 为分片属性，其中 logicTableName 为逻辑表，columnName 分片健（字段），value 为从 SQL 中解析出的分片健的值
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        for (String table : availableTargetNames) {
            if (table.endsWith(String.valueOf((shardingValue.getValue()) % availableTargetNames.size()))) {
                return table;
            }
        }
        throw new UnsupportedOperationException();
    }
}
```
### 范围分片算法
使用场景：当我们 SQL中的分片健字段用到 BETWEEN AND操作符会使用到此算法，会根据 SQL中给出的分片健值范围值处理分库、分表逻辑。
`SELECT * FROM t_order where  order_id BETWEEN 1 AND 100;`
```
@Service
public class RangeTableShardingAlgorithm implements RangeShardingAlgorithm<Integer> {
    @Override
    public Collection<String> doSharding(Collection<String> tableNames, RangeShardingValue<Integer> shardingValue) {

        Set<String> result = new LinkedHashSet<>();
        // between and 的起始值
        int lower = shardingValue.getValueRange().lowerEndpoint();
        int upper = shardingValue.getValueRange().upperEndpoint();
        // 循环范围计算分库逻辑
        for (int i = lower; i <= upper; i++) {
            for (String table : tableNames) {
                if (table.endsWith(String.valueOf( i % tableNames.size() ))) {
                    result.add(table);
                }
            }
        }
        return result;
    }
}

```
>Collection<String> 在分库、分表时分别代表分片库名和表名集合，RangeShardingValue 这里取值方式稍有不同， lowerEndpoint 表示起始值， upperEndpoint 表示截止值。
## 复合分片策略
使用场景：SQL 语句中有>，>=, <=，<，=，IN 和 BETWEEN AND 等操作符，不同的是复合分片策略支持对多个分片健操作。
下面我们实现同时以 order_id、user_id 两个字段作为分片健，自定义复合分片策略。
>SELECT * FROM t_order where  user_id =0  and order_id = 1;
我们先修改一下原配置，complex.shardingColumn 切换成 complex.shardingColumns 复数，分片健上再加一个 user_id ，分片策略名变更为 complex ，complex.algorithmClassName 替换成我们自定义的复合分片算法。
  ```
  complex:
  #分片列名称，多个列以逗号分隔
  shardingColumns: user_id,order_id
  #复合分片算法类名称。该类需实现ComplexKeysShardingAlgorithm接口并提供无参数的
  algorithmClassName: com.harmony.shardingjdbc.sharding.Algorithm.RangeTableShardingAlgorithm
  ```
  复合分片算法需要实现ComplexKeysShardingAlgorithm，然后根据传递的值自定义自己的分片规则
  ```
  Collection<String> 用法还是老样子，由于支持多分片健 ComplexKeysShardingValue 分片属性内用一个分片健为 key，分片健值为 value 的 map来存储分片键属性。
  ```
  ```
  @Service
public class ComplexTableShardingAlgorithm implements ComplexKeysShardingAlgorithm<Long> {

    @Override
    public Collection<String> doSharding(Collection<String> tableNames, ComplexKeysShardingValue<Long> shardingValue) {
        // 得到每个分片健对应的值
        Collection<Long> orderIdValues = this.getShardingValue(shardingValue, "order_id");
        Collection<Long> userIdValues = this.getShardingValue(shardingValue, "user_id");

        List<String> shardingSuffix = new ArrayList<>();
        // 对两个分片健同时取模的方式分库
        for (Long userId : userIdValues) {
            for (Long orderId : orderIdValues) {
                String suffix = userId % 2 + "_" + orderId % 2;
                for (String table : tableNames) {
                    if (table.endsWith(suffix)) {
                        shardingSuffix.add(table);
                    }
                }
            }
        }
        return shardingSuffix;
    }

    private Collection<Long> getShardingValue(ComplexKeysShardingValue<Long> shardingValues, final String key) {
        Collection<Long> valueSet = new ArrayList<>();
        Map<String, Collection<Long>> columnNameAndShardingValuesMap = shardingValues.getColumnNameAndShardingValuesMap();
        if (columnNameAndShardingValuesMap.containsKey(key)) {
            valueSet.addAll(columnNameAndShardingValuesMap.get(key));
        }
        return valueSet;
    }
}
  ```
  ## Hint分片策略
  Hint分片策略（HintShardingStrategy）相比于上面几种分片策略稍有不同，这种分片策略无需配置分片健，分片健值也不再从 SQL中解析，而是由外部指定分片信息，让 SQL在指定的分库、分表中执行。ShardingSphere 通过 Hint API实现指定操作，实际上就是把分片规则tablerule 、databaserule由集中配置变成了个性化配置。
举个例子，如果我们希望订单表t_order用 user_id 做分片健进行分库分表，但是 t_order 表中却没有 user_id 这个字段，这时可以通过 Hint API 在外部手动指定分片健或分片库。
>//Hint分片算法必须使用HintManager工具类
>HintManager hintManager = HintManager.getInstance();
>hintManager.addTableShardingValue("t_order", 1);
```
@Service
public class HintTableShardingAlgorithm implements HintShardingAlgorithm<Integer> {

    @Override
    public Collection<String> doSharding(Collection<String> tableNames, HintShardingValue<Integer> shardingValue) {
        List<String> shardingResult = Lists.newArrayList();
        for (String table : tableNames) {
            // hint分片算法的ShardingValue有两种具体类型:
            // ListShardingValue和RangeShardingValue
            // 使用哪种取决于HintManager.addDatabaseShardingValue(String, String, ShardingOperator,...),ShardingOperator的类型
            for (Integer value : shardingValue.getValues()) {
                if (table.endsWith(String.valueOf(value % 2))) {
                    shardingResult.add(table);
                }
            }
        }
        return shardingResult;
    }
}
```

 # FAQ
注意：这里演示使用的创建时间、修改时间都是LocalDateTime类型，但是mybatis会调用BaseTypeHandler，会导致org.apache.ibatis.type.LocalDateTimeTypeHandler.getNullableResult(LocalDateTimeTypeHandler.java:38)抛出异常，所以对需要加一个LocalDateTime的转换类注册到TypeHandlerRegistry中
```
/**
 * localDateTime转换类
 * @author wangkuan
 * @describe
 * @date 2020-03-05
 */
public class LocalDateTimeTypeHandler extends BaseTypeHandler<LocalDateTime> {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType)
            throws SQLException {
        if (parameter != null) {
            ps.setString(i, dateTimeFormatter.format(parameter));
        }
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String target = rs.getString(columnName);
        if (StringUtil.isEmpty(target)) {
            return null;
        }
        return LocalDateTime.parse(target, dateTimeFormatter);
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String target = rs.getString(columnIndex);
        if (StringUtil.isEmpty(target)) {
            return null;
        }
        return LocalDateTime.parse(target, dateTimeFormatter);
    }

    @Override
    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String target = cs.getString(columnIndex);
        if (StringUtil.isEmpty(target)) {
            return null;
        }
        return LocalDateTime.parse(target, dateTimeFormatter);
    }
}
```

```
@Slf4j
@Configuration
@ConditionalOnBean(DataSource.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class MybatisPlusConfiguration {

    /**
     * 解决MybatisPlus LocalDateTime转换问题
     */
    public MybatisPlusConfiguration(MybatisPlusProperties mybatisPlusProperties) {
        MybatisConfiguration configuration = mybatisPlusProperties.getConfiguration();
        GlobalConfig globalConfig = mybatisPlusProperties.getGlobalConfig();
        GlobalConfigUtils.setGlobalConfig(configuration, globalConfig);
        configuration.getTypeHandlerRegistry().register(LocalDateTime.class, new LocalDateTimeTypeHandler());
        configuration.getTypeHandlerRegistry().register(LocalDate.class, new LocalDateTypeHandler());
        configuration.getTypeHandlerRegistry().register(LocalTime.class, new LocalTimeTypeHandler());
        GlobalConfigUtils.setGlobalConfig(configuration, globalConfig);
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //分页
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * sql 日志
     *
     * @return SqlLogInterceptor
     */
    @Bean
    @ConditionalOnProperty(value = "project.mybatis-plus.sql-log", havingValue = "true", matchIfMissing = true)
    public SqlLogInterceptor sqlLogInterceptor() {
        return new SqlLogInterceptor();
    }

}
```
 

 
 
 
 
 
