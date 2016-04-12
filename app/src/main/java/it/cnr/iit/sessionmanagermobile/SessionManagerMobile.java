package it.cnr.iit.sessionmanagermobile;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.j256.ormlite.android.apptools.OrmLiteBaseService;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import it.cnr.iit.sessionmanagerdalutilities.*;

public class SessionManagerMobile extends OrmLiteBaseService<DatabaseMobileHelper> implements SessionManager {

    String databaseFileName;

    public SessionManagerMobile() {
        databaseFileName = "android:sqlite:session_manager.db";
    }

    @Override
    public Boolean start() {
        return true;
    }

    @Override
    public Boolean stop() {
        return true;
    }

    @Override
    public Boolean createEntryForSubject(String sessionId, String policyOnGoing, String policyPost, String originalRequest, List<String> onGoingAttributes, String status, String pepURI, String subjectId) {
        return createEntry(sessionId, policyOnGoing, policyPost, originalRequest, onGoingAttributes, status, pepURI, subjectId, null);
    }

    @Override
    public Boolean createEntryForObject(String sessionId, String policyOnGoing, String policyPost, String originalRequest, List<String> onGoingAttributes, String status, String pepURI, String objectId) {
        return createEntry(sessionId, policyOnGoing, policyPost, originalRequest, onGoingAttributes, status, pepURI, null, objectId);
    }

    private Boolean createEntry(String sessionId, String policyOnGoing, String policyPost, String originalRequest, List<String> onGoingAttributes, String status, String pepURI, String subjectId, String objectId) {
        RuntimeExceptionDao<Session, String> sessionDao = getHelper().getSessionRuntimeDao();
        Session s = new Session(sessionId, policyOnGoing, policyPost, originalRequest, status, pepURI);
        if(sessionDao.idExists(sessionId)) {
            return false;
        }
        sessionDao.create(s);
        Session sessionResult = sessionDao.queryForId(sessionId);
        ForeignCollection<OnGoingAttribute> attributes = sessionResult.getOnGoingAttributes();
        for (String attr : onGoingAttributes) {
            OnGoingAttribute a = new OnGoingAttribute(attr, subjectId, objectId);
            attributes.add(a);
        }
        return true;
    }

    @Override
    public Boolean updateEntry(String sessionId, String status) {
        RuntimeExceptionDao<Session, String> sessionDao = getHelper().getSessionRuntimeDao();
        Session s = sessionDao.queryForId(sessionId);
        s.setStatus(status);
        sessionDao.update(s);
        return true;
    }

    @Override
    public Boolean deleteEntry(String sessionId) {
        RuntimeExceptionDao<Session, String> sessionDao = getHelper().getSessionRuntimeDao();
        sessionDao.queryForId(sessionId).getOnGoingAttributes().clear();
        sessionDao.deleteById(sessionId);
        return true;
    }

    @Override
    public List<Session> getSessionsForSubjectAttributes(String attributeName) {
        // select * from on_going_attributes where name == 'attributeName' and subject_id is not null and object_id is null
        try {
            // select * from on_going_attributes where name == 'attributeName' and subject_id is null and object_id is not null
            RuntimeExceptionDao<OnGoingAttribute, Integer> attributesDao = getHelper().getAttributesRuntimeDao();
            QueryBuilder<OnGoingAttribute, Integer> qbAttributes = attributesDao.queryBuilder();
            List<OnGoingAttribute> attributes = qbAttributes.where()
                    .isNotNull(OnGoingAttribute.SUBJECTID_FIELD_NAME).and()
                    .isNull(OnGoingAttribute.OBJECTID_FIELD_NAME).and()
                    .eq(OnGoingAttribute.ATTRIBUTENAME_FIELD_NAME, attributeName)
                    .query();

            List<Session> sessions = new LinkedList<>();
            for (OnGoingAttribute attr : attributes) {
                sessions.add(attr.getSession());
            }
            return sessions;

        } catch (SQLException ex) {

        }
        return null;
    }

    @Override
    public List<Session> getSessionsForObjectAttributes(String attributeName) {
        try {
            // select * from on_going_attributes where name == 'attributeName' and subject_id is null and object_id is not null
            RuntimeExceptionDao<OnGoingAttribute, Integer> attributesDao = getHelper().getAttributesRuntimeDao();
            QueryBuilder<OnGoingAttribute, Integer> qbAttributes = attributesDao.queryBuilder();
            List<OnGoingAttribute> attributes = qbAttributes.where()
                    .isNull(OnGoingAttribute.SUBJECTID_FIELD_NAME).and()
                    .isNotNull(OnGoingAttribute.OBJECTID_FIELD_NAME).and()
                    .eq(OnGoingAttribute.ATTRIBUTENAME_FIELD_NAME, attributeName)
                    .query();

            List<Session> sessions = new LinkedList<>();
            for (OnGoingAttribute attr : attributes) {
                sessions.add(attr.getSession());
            }
            return sessions;

        } catch (SQLException ex) {

        }
        return null;
    }

    @Override
    public Session getSessionForId(String sessionId) {
        RuntimeExceptionDao<Session, String> sessionDao = getHelper().getSessionRuntimeDao();
        return sessionDao.queryForId(sessionId);
    }

    @Override
    public List<Session> getSessionsForStatus(String status) {
        try {
            RuntimeExceptionDao<Session, String> sessionDao = getHelper().getSessionRuntimeDao();
            QueryBuilder<Session, String> qbSessions = sessionDao.queryBuilder();
            return qbSessions.where().eq(Session.STATUS_FIELD_NAME, status).query();
        } catch (SQLException ex) {

        }
        return null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
