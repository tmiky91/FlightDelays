package it.polito.tdp.extflightdelays.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBConnect {

	private static final String jdbcURL = "jdbc:mysql://localhost/extflightdelays?user=root&serverTimezone=Europe/Rome";
	private static HikariDataSource ds = null;

	public static Connection getConnection() {

		if (ds == null) {
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl(jdbcURL);
			config.setUsername("root");
			//config.setPassword("root");
			
			//configurazione mysql
			config.addDataSourceProperty("cachePrepStmts", "true");
			config.addDataSourceProperty("preprStmtChacheSize", "250");
			config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
			ds = new HikariDataSource(config);
		}
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			System.err.println("Errore connessione al DB");
			throw new RuntimeException(e);
		}

	}

}
