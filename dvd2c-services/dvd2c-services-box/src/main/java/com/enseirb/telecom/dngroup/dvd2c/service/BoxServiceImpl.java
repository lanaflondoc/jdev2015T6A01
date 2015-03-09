package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import jersey.repackaged.com.google.common.base.Throwables;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.ApplicationContext;
import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.enseirb.telecom.dngroup.dvd2c.db.BoxRepositoryObject;
import com.enseirb.telecom.dngroup.dvd2c.db.CrudRepository;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.SuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestBoxService;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestBoxServiceImpl;

public class BoxServiceImpl implements BoxService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BoxServiceImpl.class);
	CrudRepository<BoxRepositoryObject, String> boxDatabase;
	RequestBoxService requetBoxService = new RequestBoxServiceImpl(
			CliConfSingleton.centralURL
					+ "/api/app/box/");

	public BoxServiceImpl(
			CrudRepository<BoxRepositoryObject, String> boxDatabase) {

		this.boxDatabase = boxDatabase;
	}

	@Override
	public boolean boxExistOnServer(String boxID) {
		boolean exist = boxExistOnLocal(boxID);
		try {
			Box boxGet = requetBoxService.get(boxID);
			if ((boxGet == null))
				exist = false;
			else if (boxID.equals(boxGet.getBoxID()))
				exist = true;
			else{
				exist = false;
			}
		} catch (IOException e) {
			LOGGER.error("Can not connect on the server : {}",boxID, e);
		} catch (NoSuchBoxException e) {
			exist = false;
		}
		return exist;
	}

	public boolean boxExistOnLocal(String boxID) {
		return boxDatabase.exists(boxID);
	}

	public Box getBoxOnLocal(String boxID) throws NoSuchBoxException {
		
		Box box = null;
		box = boxDatabase.findOne(boxID).toBox();
		if (box == null) {
			LOGGER.debug("No Box Found : {}",boxID);
			throw new NoSuchBoxException();
		}
		LOGGER.debug("Box Found : {}",box.getBoxID());
		return box;
	}

	public Box createBoxOnServer(Box box) {

		
		try {
			requetBoxService.createBoxORH(box);
		} catch (IOException e) {
			LOGGER.error("Error during creating a box on server : ",
					box.getBoxID(), e);
		} catch (SuchBoxException e) {
			LOGGER.debug("Box already existing : ", box.getBoxID());
		}
		return createBoxOnLocal(box);

	}

	private Box createBoxOnLocal(Box box) {

		Box b = boxDatabase.save(new BoxRepositoryObject(box)).toBox();
		return b;

	}

	public void saveBoxOnServer(Box box) {

		try {

			requetBoxService.updateBoxORH(box);
			saveBoxOnLocal(box);
		} catch (IOException e) {
			LOGGER.error("can't save Box On Server : {}", box.getBoxID(), e);
			throw Throwables.propagate(e);
		} catch (NoSuchBoxException e) {
			LOGGER.error("Box not found: {}", box.getBoxID(), e);
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		
	}

	public void saveBoxOnLocal(Box box) {
		boxDatabase.save(new BoxRepositoryObject(box));
	}

	public void deleteBoxOnServer(String boxID) {

		try {
			requetBoxService.deleteBoxORH(boxID);
		} catch (IOException e) {
			LOGGER.error("can't delete box : {}", boxID, e);
		} catch (NoSuchBoxException e) {
			LOGGER.error("box not found : {}", boxID, e);
		}

		deleteBoxOnLocal(boxID);
	}

	public void deleteBoxOnLocal(String boxID) {
		this.boxDatabase.delete(boxID);

	}

	@Override
	public List<Box> getBoxListFromIP(String ip) {
		List<Box> listBox = getBoxesFromIP(ip);
		return listBox;
	}

	@Override
	public List<Box> getAllBox() {
		Iterable<BoxRepositoryObject> boxIterable = boxDatabase.findAll();
		List<Box> listBox = new ArrayList<Box>();
		if (boxIterable == null)
			return listBox;
		else {
			Iterator<BoxRepositoryObject> itr = boxIterable.iterator();
			while (itr.hasNext()) {
				BoxRepositoryObject box = itr.next();
				listBox.add(box.toBox());
			}
			return listBox;
		}
	}

	@Override
	public List<Box> getBoxesFromIP(String ip) {

		Iterable<BoxRepositoryObject> boxIterable = boxDatabase.findAll();
		List<Box> listBox = new ArrayList<Box>();

		if (boxIterable == null)
			return listBox;
		else {
			Iterator<BoxRepositoryObject> itr = boxIterable.iterator();
			while (itr.hasNext()) {
				BoxRepositoryObject box = itr.next();

				if (box.getIp().equals(ip)) {
					listBox.add(box.toBox());
				}
			}
			return listBox;
		}

	}


	


}