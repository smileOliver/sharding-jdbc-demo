package com.harmony.shardingjdbc.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.orchestration.center.config.CenterConfiguration;
import org.apache.shardingsphere.orchestration.center.config.OrchestrationConfiguration;
import org.apache.shardingsphere.orchestration.center.instance.NacosCenterRepository;
import org.apache.shardingsphere.orchestration.center.instance.NacosPropertyKey;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.apache.shardingsphere.shardingjdbc.orchestration.api.OrchestrationShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@NacosPropertySource(dataId = DataSourceConfig.DATA_ID, autoRefreshed = true)
public class DataSourceConfig {

    public static final String DATA_ID = "com.harmony.shardingjdbc.config.DataSourceConfig";
    public static final String NACOS_NAME_SPACE = "SHARDING_JDBC";
    @Value("${datasource0.url}")
    private String url0;
    @Value("${datasource0.username}")
    private String username0;
    @Value("${datasource0.password}")
    private String password0;
    @Value("${datasource0.driver-class-name}")
    private String driverClassName0;
    @Value("${datasource1.url}")
    private String url1;
    @Value("${datasource1.username}")
    private String username1;
    @Value("${datasource1.password}")
    private String password1;
    @Value("${datasource1.driver-class-name}")
    private String driverClassName1;
    @Value("${spring.datasource.druid.filters}")
    private String filters;
    @Value("${nacos.config.server-addr}")
    private String nacosConfigServers;

    /**
     * 采用雪花算法获取分布式id
     *
     * @return
     */
    private static KeyGeneratorConfiguration getKeyGeneratorConfiguration() {
        KeyGeneratorConfiguration result = new KeyGeneratorConfiguration("SNOWFLAKE", "id");
        return result;
    }

    /**
     * 采用雪花算法获取订单分布式id
     *
     * @return
     */
    private static KeyGeneratorConfiguration getOrderKeyGeneratorConfiguration() {
        KeyGeneratorConfiguration result = new KeyGeneratorConfiguration("SNOWFLAKE", "order_id");
        return result;
    }

    @Bean("dataSource")
    public DataSource dataSource() throws SQLException {
        Properties shardingProperties = new Properties();
        //开启SQL显示
        shardingProperties.setProperty("sql.show", "true");
        // 创建数据源
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(createDataSourceMap(), createShardingRule(), shardingProperties);
        //配置中心方式获取数据源
        //DataSource dataSource = orchestrationCreateDataSource(shardingProperties);
        return dataSource;

    }

    /**
     * 分库分表规则配置
     *
     * @return
     */
    public ShardingRuleConfiguration createShardingRule() {
        // Sharding全局配置
        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
        //配置全局表
        shardingRuleConfiguration.getBroadcastTables().add("user_config");
        //配置绑定关系表
        shardingRuleConfiguration.getBindingTableGroups().add("t_order,order_item");
        //默认id生成策略
        shardingRuleConfiguration.setDefaultKeyGeneratorConfig(getKeyGeneratorConfiguration());
        //默认数据库
        shardingRuleConfiguration.setDefaultDataSourceName("ds0");

        //分库分表规则
        shardingRuleConfiguration.getTableRuleConfigs().add(getUserTableRuleConfiguration());
        shardingRuleConfiguration.getTableRuleConfigs().add(getOrderTableRule());
        shardingRuleConfiguration.getTableRuleConfigs().add(getOrderItemTableRule());
        return shardingRuleConfiguration;
    }

    /**
     * 注册中心方式创建数据源
     *
     * @param props
     * @return
     * @throws SQLException
     */
    public DataSource orchestrationCreateDataSource(final Properties props) throws SQLException {
        Properties nacosProperties = new Properties();
        nacosProperties.setProperty("group", NacosPropertyKey.GROUP.getDefaultValue());
        nacosProperties.setProperty("timeout", "3000");
        nacosProperties.setProperty("overwrite", Boolean.TRUE.toString());
        CenterConfiguration nacosResult = new CenterConfiguration("nacos", nacosProperties);
        nacosResult.setServerLists(nacosConfigServers);
        nacosResult.setNamespace(NacosPropertyKey.GROUP.getDefaultValue());
        nacosResult.setOrchestrationType("config_center");

        NacosCenterRepository nacosCenterRepository = new NacosCenterRepository();
        nacosCenterRepository.setProperties(nacosProperties);
        nacosCenterRepository.init(nacosResult);

        Properties zookeeperProperties = new Properties();
        zookeeperProperties.setProperty("overwrite", Boolean.TRUE.toString());
        CenterConfiguration zookeeperResult = new CenterConfiguration("zookeeper", zookeeperProperties);
        zookeeperResult.setServerLists("127.0.0.1:2181");
        zookeeperResult.setNamespace("sharding_jdbc");
        zookeeperResult.setOrchestrationType("registry_center");
        Map<String, CenterConfiguration> configurationMap = new HashMap<>();
        configurationMap.put("orchestration-sharding-data-source", nacosResult);
        configurationMap.put("orchestration-zookeeper-sharding-data-source", zookeeperResult);
        // 配置治理
        OrchestrationConfiguration orchConfig = new OrchestrationConfiguration(configurationMap);
        return OrchestrationShardingDataSourceFactory.createDataSource(createDataSourceMap(), createShardingRule(), props, orchConfig);
    }

    /**
     * 创建数据源
     *
     * @return
     * @throws SQLException
     */
    public Map<String, DataSource> createDataSourceMap() throws SQLException {

        DruidDataSource dataSource0 = new DruidDataSource();
        dataSource0.setDriverClassName(this.driverClassName0);
        dataSource0.setUrl(this.url0);
        dataSource0.setUsername(this.username0);
        dataSource0.setPassword(this.password0);
        dataSource0.setFilters(this.filters);

        DruidDataSource dataSource1 = new DruidDataSource();
        dataSource1.setDriverClassName(this.driverClassName1);
        dataSource1.setUrl(this.url1);
        dataSource1.setUsername(this.username1);
        dataSource1.setPassword(this.password1);
        dataSource1.setFilters(this.filters);

        //分库设置
        final Map<String, DataSource> dataSourceMap = new HashMap<>();
        //添加两个数据库database0和database1
        dataSourceMap.put("ds0", dataSource0);
        dataSourceMap.put("ds1", dataSource1);
        return dataSourceMap;
    }

    /**
     * 用户表的分库分表规则  采用行表达式
     *
     * @return
     */
    TableRuleConfiguration getUserTableRuleConfiguration() {
        TableRuleConfiguration userTableRule = new TableRuleConfiguration("user", "ds${0..1}.user_${0..2}");
        userTableRule.setKeyGeneratorConfig(getKeyGeneratorConfiguration());
        userTableRule.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("id", "ds${id % 2}"));
        userTableRule.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("id", "user_${id % 3}"));
        return userTableRule;
    }

    /**
     * 订单表分库分表规则
     *
     * @return
     */
    TableRuleConfiguration getOrderTableRule() {
        TableRuleConfiguration orderTableRule = new TableRuleConfiguration("t_order", "ds${0..1}.t_order_${0..3}");
        orderTableRule.setKeyGeneratorConfig(getOrderKeyGeneratorConfiguration());
        orderTableRule.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "ds${user_id % 2}"));
        orderTableRule.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("order_id", "t_order_${order_id % 4}"));
        return orderTableRule;
    }

    /**
     * 获取订单信息表的分库分表规则
     *
     * @return
     */
    TableRuleConfiguration getOrderItemTableRule() {
        TableRuleConfiguration taleRule = new TableRuleConfiguration("order_item", "ds${0..1}.order_item_${0..3}");
        taleRule.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "ds${user_id % 2}"));
        taleRule.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("order_id", "order_item_${order_id % 4}"));
        return taleRule;
    }

    /**
     * 订单表分片规则，等同于new InlineShardingStrategyConfiguration("user_id","order_${order_id % 4}")
     * 这里只是演示，也就是可以根据需要自己定义精准的分片规则
     */
    static class OrderTableShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

        @Override
        public String doSharding(Collection<String> tableNames, PreciseShardingValue<Long> shardingValue) {
            for (String table : tableNames) {
                if (table.endsWith(String.valueOf(shardingValue.getValue() % 4))) {
                    return table;
                }
            }
            return "";
        }
    }


}


