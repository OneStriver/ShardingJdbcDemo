package com.sunkaisens.shard.config;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.BindingTableRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.sunkaisens.shard.strategy.ModuloTableShardingAlgorithm;

/**
 * 说明：数据源配置
 */
@Configuration
@MapperScan(basePackages = DataSourceConfig.PACKAGE, sqlSessionTemplateRef = "sqlSessionTemplate") //扫描 Mapper 接口并容器管理
public class DataSourceConfig {
	
	@Autowired
	private GlobalParamConfig globalParamConfig;
	
    static final String PACKAGE = "com.sunkaisens.shard.mapper";					//master 目录
    static final String MAPPER_LOCATION = "classpath:mybatis/*/*.xml";				//扫描的 xml 目录
    static final String CONFIG_LOCATION = "classpath:mybatis/mybatis-config.xml"; 	//自定义的mybatis config 文件位置
    static final String TYPE_ALIASES_PACKAGE = "com.sunkaisens.shard.entity"; 		//扫描的 实体类 目录
    private List<TableRule> tableRuleList = new ArrayList<TableRule>();
    
    //主数据源
    @Value("${spring.datasource.master.driver-class-name}")
    private String masterDriverClass;
    @Value("${spring.datasource.master.url}")
    private String masterUrl;
    @Value("${spring.datasource.master.username}")
    private String masterUsername;
    @Value("${spring.datasource.master.password}")
    private String masterPassword;
    //备数据源
    @Value("${spring.datasource.slave.driver-class-name}")
    private String slaveDriverClass;
    @Value("${spring.datasource.slave.url}")
    private String slaveUrl;
    @Value("${spring.datasource.slave.username}")
    private String slaveUsername;
    @Value("${spring.datasource.slave.password}")
    private String slavePassword;
 
    @Bean(name = "masterDataSource")
    public DataSource masterDataSource() {
    	DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(masterDriverClass);
        dataSource.setUrl(masterUrl);
        dataSource.setUsername(masterUsername);
        dataSource.setPassword(masterPassword);
        return dataSource;
    }
    
    @Bean(name = "slaveDataSource")
    public DataSource slaveDataSource() {
    	DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(slaveDriverClass);
        dataSource.setUrl(slaveUrl);
        dataSource.setUsername(slaveUsername);
        dataSource.setPassword(slavePassword);
        return dataSource;
    }
    
    @Bean(name="dataSourceRule")
    public DataSourceRule dataSourceRule(@Qualifier("masterDataSource") DataSource masterDataSource,
    									@Qualifier("slaveDataSource") DataSource slaveDataSource){
        Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>(); //设置分库映射
        dataSourceMap.put("masterDataSource", masterDataSource);
        //dataSourceMap.put("slaveDataSource", slaveDataSource);
        //设置默认库，两个库以上时必须设置默认库。默认库的数据源名称必须是dataSourceMap的key之一
        return new DataSourceRule(dataSourceMap, "masterDataSource"); 
    }
    
    @Bean(name="shardingRule")
    public ShardingRule shardingRule(DataSourceRule dataSourceRule){
        //绑定表策略，在查询时会使用主表策略计算路由的数据源，因此需要约定绑定表策略的表的规则需要一致，可以一定程度提高效率
        List<BindingTableRule> bindingTableRules = new ArrayList<BindingTableRule>();
        bindingTableRules.add(new BindingTableRule(getAllTableRules(dataSourceRule)));
        return ShardingRule.builder()
                .dataSourceRule(dataSourceRule)
                .tableRules(getAllTableRules(dataSourceRule))
                .bindingTableRules(bindingTableRules)
                .build();
    }
    
    private List<TableRule> getAllTableRules(DataSourceRule dataSourceRule){
    	ModuloTableShardingAlgorithm moduloTableShardingAlgorithm = new ModuloTableShardingAlgorithm();
    	tableRuleList.clear();
    	//========User表分表========
    	String user_allTableName = globalParamConfig.getUserAllTableName();
    	List<String> user_allTableNameList = Arrays.asList(user_allTableName.split(","));
    	//获取没有数字的表名
    	String user_firstTableName = user_allTableNameList.get(0);
    	String user_rawTableName = user_firstTableName.substring(0,user_firstTableName.lastIndexOf("_"));
        //具体分库分表策略
        TableRule user_orderTableRule = TableRule.builder(user_rawTableName)
                .actualTables(user_allTableNameList)
                .tableShardingStrategy(new TableShardingStrategy("id", moduloTableShardingAlgorithm))
                .dataSourceRule(dataSourceRule)
                .build();
        tableRuleList.add(user_orderTableRule);
        //========Role表分表========
        String role_allTableName = globalParamConfig.getRoleAllTableName();
    	List<String> role_allTableNameList = Arrays.asList(role_allTableName.split(","));
    	//获取没有数字的表名
    	String role_firstTableName = role_allTableNameList.get(0);
    	String role_rawTableName = role_firstTableName.substring(0,role_firstTableName.lastIndexOf("_"));
        //具体分库分表策略
        TableRule role_orderTableRule = TableRule.builder(role_rawTableName)
                .actualTables(role_allTableNameList)
                .tableShardingStrategy(new TableShardingStrategy("id", moduloTableShardingAlgorithm))
                .dataSourceRule(dataSourceRule)
                .build();
        tableRuleList.add(role_orderTableRule);
    	return tableRuleList;
    }
    
    @Bean(name="dataSource")
    public DataSource shardingDataSource(ShardingRule shardingRule) throws SQLException {
        return ShardingDataSourceFactory.createDataSource(shardingRule);
    }
    
    @Bean(name = "transactitonManager")
    public DataSourceTransactionManager transactitonManager(@Qualifier("dataSource") DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
 
    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource)throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(DataSourceConfig.MAPPER_LOCATION));
        sessionFactory.setConfigLocation(new DefaultResourceLoader().getResource(DataSourceConfig.CONFIG_LOCATION));
        sessionFactory.setTypeAliasesPackage(DataSourceConfig.TYPE_ALIASES_PACKAGE);
        return sessionFactory.getObject();
    }
    
    @Bean(name = "sqlSessionTemplate")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
