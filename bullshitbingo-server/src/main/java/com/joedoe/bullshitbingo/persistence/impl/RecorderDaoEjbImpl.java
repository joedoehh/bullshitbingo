package com.joedoe.bullshitbingo.persistence.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;
import com.ibm.websphere.objectgrid.ClientClusterContext;
import com.ibm.websphere.objectgrid.ObjectGrid;
import com.ibm.websphere.objectgrid.ObjectGridManager;
import com.ibm.websphere.objectgrid.ObjectGridManagerFactory;
import com.ibm.websphere.objectgrid.ObjectMap;
import com.ibm.websphere.objectgrid.Session;
import com.ibm.websphere.objectgrid.security.config.ClientSecurityConfiguration;
import com.ibm.websphere.objectgrid.security.config.ClientSecurityConfigurationFactory;
import com.ibm.websphere.objectgrid.security.plugins.builtins.UserPasswordCredentialGenerator;
import com.joedoe.bullshitbingo.common.config.BullshitBingoConfiguration;
import com.joedoe.bullshitbingo.common.config.DataCacheConfig;
import com.joedoe.bullshitbingo.model.RecordingBo;
import com.joedoe.bullshitbingo.persistence.api.RecorderDao;

@Stateless
public class RecorderDaoEjbImpl implements RecorderDao {

	private final static String MAP_NAME = "RECORDING_MAP";

	private ObjectGrid objectGrid;
	
	public RecorderDaoEjbImpl() {
	}

	@Override
	public @NotNull List<RecordingBo> findAll() {
		Session session = null;
		try {
			session = objectGrid.getSession();
			Collection values = getRecordingMap(session).getJavaMap().values();
			List<RecordingBo> returnValue = new ArrayList<>();
			for (Object object : values) 
				returnValue.add((RecordingBo) object);
			return returnValue;
		} catch (Exception e) {
			throw new RuntimeException("Error accessing object grid " + objectGrid, e);
		} finally {
			if (session != null)
					session.close();
		}		
	}

	@Override
	public @NotNull List<RecordingBo> findAfterTimestamp(
			@NotNull Date lastRecordingTimestamp) {
		Preconditions.checkNotNull(lastRecordingTimestamp);
		List<RecordingBo> returnValue = new ArrayList<>();
		for (RecordingBo recording : findAll()) 
			if (recording.getTimestampOfRecordingAsDate().after(lastRecordingTimestamp))
				returnValue.add(recording);
		return returnValue;
	}

	@Override
	public RecordingBo findById(@NotNull String id) {
		return null;
	}

	@Override
	public RecordingBo create(@NotNull RecordingBo recordingBo) {
		return null;
	}

	@Override
	public void deleteAll() {
		for (RecordingBo bo : findAll())
			delete(bo);
	}

	@Override
	public void delete(@NotNull String id) {
		Preconditions.checkNotNull(id);
		delete(findById(id));
	}

	private void delete(RecordingBo recordingBo) {
	}

	private ObjectMap getRecordingMap(Session session) {
		try {
			ObjectMap returnValue = session.getMap(MAP_NAME);
			returnValue.setTimeToLive(0);
			return returnValue;
		} catch (Exception e) {
			throw new RuntimeException("error getting map " + MAP_NAME
					+ " for grid "
					+ BullshitBingoConfiguration.getDataCacheConfig(), e);
		}
	}

	@PostConstruct
	private void initClientGridSession() {
		DataCacheConfig dataCacheConfig = BullshitBingoConfiguration
				.getDataCacheConfig();
		try {
			System.out.println("initClientGridSession() " + dataCacheConfig);
			ObjectGridManager ogm = ObjectGridManagerFactory
					.getObjectGridManager();
			ClientSecurityConfiguration csc = ClientSecurityConfigurationFactory
					.getClientSecurityConfiguration();
			csc.setCredentialGenerator(new UserPasswordCredentialGenerator(
					dataCacheConfig.username, dataCacheConfig.password));
			csc.setSecurityEnabled(true);
			System.out.println("initClientGridSession() csc set ");
			ClientClusterContext ccc = ogm.connect(
					dataCacheConfig.catalogEndPoint, csc, null);
			System.out.println("initClientGridSession() connected");			
			objectGrid = ogm
					.getObjectGrid(ccc, dataCacheConfig.gridName);
		} catch (Exception e) {
			System.out.println("initClientGridSession() error ");
			e.printStackTrace();
			throw new RuntimeException(
					"error connecting and getting session for "
							+ dataCacheConfig, e);
		}
	}



}
