/*
 * Copyright © 2018 organization baomidou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baomidou.dynamic.datasource.creator;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.dbcp2.Dbcp2Config;
import lombok.Data;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

import static com.baomidou.dynamic.datasource.support.DdConstants.DBCP2_DATASOURCE;

/**
 * Druid数据源创建器
 *
 * @author TaoYu
 * @since 2020/1/21
 */
@Data
public class Dbcp2DataSourceCreator implements DataSourceCreator {

    private static Boolean dbcp2Exists = false;

    static {
        try {
            Class.forName(DBCP2_DATASOURCE);
            dbcp2Exists = true;
        } catch (ClassNotFoundException ignored) {
        }
    }

    private Dbcp2Config gConfig;

    public Dbcp2DataSourceCreator(Dbcp2Config gConfig) {
        this.gConfig = gConfig;
    }

    @Override
    public DataSource createDataSource(DataSourceProperty dataSourceProperty) {
        Dbcp2Config config = dataSourceProperty.getDbcp2();
        BasicDataSource dataSource = config.toDbcpDataSource(gConfig);
        dataSource.setUsername(dataSourceProperty.getUsername());
        dataSource.setPassword(dataSourceProperty.getPassword());
        dataSource.setUrl(dataSourceProperty.getUrl());
        String driverClassName = dataSourceProperty.getDriverClassName();
        if (!StringUtils.isEmpty(driverClassName)) {
            dataSource.setDriverClassName(driverClassName);
        }
        return dataSource;
    }

    @Override
    public boolean support(DataSourceProperty dataSourceProperty) {
        Class<? extends DataSource> type = dataSourceProperty.getType();
        return (type == null && dbcp2Exists) || (type != null && DBCP2_DATASOURCE.equals(type.getName()));
    }
}
