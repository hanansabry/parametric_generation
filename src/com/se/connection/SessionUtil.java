package com.se.connection;

import java.sql.Connection;

import org.hibernate.Session;

public class SessionUtil
{
	// --------------------------------
	public static Session getSession()
	{
		final Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		return session;
	}

	// --------------------------------
	@SuppressWarnings("finally")
	@Deprecated
	public static Session getCurrentSession()
	{
		Session session = null;
		try
		{
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			if(session == null)
			{
				session = HibernateUtil.getSessionFactory().openSession();
			}
		}catch(final Exception e)
		{
			e.printStackTrace();
			session = HibernateUtil.getSessionFactory().openSession();
		}finally
		{
			if(session != null)
			{
				session.beginTransaction();
			}
			return session;
		}

	}

	// --------------------------------
	public static Session getSession(final Connection con)
	{
		final Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		return session;
	}

	// --------------------------------
	public static void closeSession(final Session session)
	{
		try
		{
			session.close();
		}catch(final Exception e)
		{
			e.printStackTrace();
		}

	}
}
