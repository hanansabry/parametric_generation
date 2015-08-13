package com.se.connection;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil
{

	private static SessionFactory sessionFactory;

	static
	{
		try
		{
			// Create the SessionFactory from hibernate.cfg.xml
			final Configuration config = new Configuration()
					.configure("com/se/connection/hibernate.cfg.xml");
			Properties properties = new Properties();
			properties.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
			properties.setProperty("hibernate.connection.driver_class","oracle.jdbc.driver.OracleDriver");
			properties.setProperty("hibernate.connection.url","jdbc:oracle:thin:@develop-test:1521:xlp");
			// properties.setProperty("hibernate.connection.useUnicode", "true");
			// properties.setProperty("hibernate.connection.characterEncoding", "UTF-32");
			properties.setProperty("hibernate.connection.defaultNChar", "true");
			properties.setProperty("hibernate.connection.username", "importer");
			properties.setProperty("hibernate.connection.password", "impwd");
			properties.setProperty("hibernate.default_schema", "importer");

			// properties.setProperty("hibernate.connection.autocommit", "true");
			config.mergeProperties(properties);
			sessionFactory = config.buildSessionFactory();

			// Set Listeners			

			// System.setProperty("oracle.jdbc.defaultNChar", "true");
		}catch(final Throwable ex)
		{
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			ex.printStackTrace();
//			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}

	public static void buildSessionFactory()
	{
		try
		{
			System.out.println("~~~~ reBuild Sesssion Factory ~~~~~~ ");
			// Create the SessionFactory from hibernate.cfg.xml
			final Configuration config = new Configuration()
					.configure("automation/db/hibernate.cfg.xml");
			Properties properties = new Properties();
			properties.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
			properties.setProperty("hibernate.connection.driver_class",
					"oracle.jdbc.driver.OracleDriver");
			properties.setProperty("hibernate.connection.url",
					"jdbc:oracle:thin:@automation:1521:xlp");
			// properties.setProperty("hibernate.connection.useUnicode", "true");
			// properties.setProperty("hibernate.connection.characterEncoding", "UTF-32");
			properties.setProperty("hibernate.connection.defaultNChar", "true");
			properties.setProperty("hibernate.connection.username", "automation2");
			properties.setProperty("hibernate.connection.password", "automation2");
			properties.setProperty("hibernate.default_schema", "automation2");

			// properties.setProperty("hibernate.connection.autocommit", "true");
			config.mergeProperties(properties);
			sessionFactory = config.buildSessionFactory();


			// System.setProperty("oracle.jdbc.defaultNChar", "true");
		}catch(final Throwable ex)
		{
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
	}

}
