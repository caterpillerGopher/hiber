package com.hibernate.demo;



import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.hibernate.demo.model.Contact;

public class Application {
	
	//Session factory
    private static final SessionFactory sessionFactory = buildSesssionFactory();
	
	public static void main(String[] args) throws InterruptedException {
		
		System.out.println("Session factory");
		Contact contact = new Contact.ContactBuilder("Bob", "Marley").withEmail("bob.nik@gmail.com").withPhone(5859789733L).build();
		
		//Open a Session
		System.out.println("Open a Session");
		Session session = sessionFactory.openSession();
		
		//Begin a Transaction
		System.out.println("Begin a Transaction");
		 session.beginTransaction();
		 
		
		//Use the session to save the contact
		System.out.println("Use the session to save the contact");
		session.save(contact);
		//Commit the transaction
		System.out.println("Commit the transaction");
		session.getTransaction().commit();
		// Close the session
		System.out.println("Close the session");
		session.close();
	}

	private static SessionFactory buildSesssionFactory() {
		// Create a StandardServiceRegistry
		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass(com.hibernate.demo.model.Contact.class);
		configuration.setProperty("hibernate.temp.use_jdbc_metadata_defaults","false");
		configuration.configure();
		
		final ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
		return new org.hibernate.boot.MetadataSources(registry).buildMetadata().buildSessionFactory();
		
		
	}
	
	static boolean isLocked(String fileName) {
	    try {
	        RandomAccessFile f = new RandomAccessFile(fileName, "r");
	        try {
	            FileLock lock = f.getChannel().tryLock(0, Long.MAX_VALUE, true);
	            if (lock != null) {
	                lock.release();
	                return false;
	            }
	        } finally {
	            f.close();
	        }
	    } catch (IOException e) {
	        // ignore
	    }
	    return true;
	}

}
