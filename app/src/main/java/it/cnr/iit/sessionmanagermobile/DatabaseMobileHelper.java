package it.cnr.iit.sessionmanagermobile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import it.cnr.iit.sessionmanagerdalutilities.*;

/**
 * Created by fabio on 11/04/16.
 */
public class DatabaseMobileHelper extends OrmLiteSqliteOpenHelper {

    private Dao<Session, String> sessionDao;
    private Dao<OnGoingAttribute, Integer> attributesDao;

    private RuntimeExceptionDao<Session, String> sessionRuntimeDao;
    private RuntimeExceptionDao<OnGoingAttribute, Integer> attributesRuntimeDao;

    public DatabaseMobileHelper(Context context) {
        super(context, "session_manager.db", null, 1);
    }

    public DatabaseMobileHelper(Context context, String databaseName) {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource conn) {
        try {
            Log.d(getClass().getSimpleName(), "oncreate helper");
            TableUtils.createTableIfNotExists(conn, Session.class);
            TableUtils.createTableIfNotExists(conn, OnGoingAttribute.class);
        } catch(SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource conn, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(conn, OnGoingAttribute.class, true);
            TableUtils.dropTable(conn, Session.class, true);
            onCreate(db, conn);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Dao<Session, String> getSessionDao() throws SQLException {
        if(sessionDao == null) {
            sessionDao = getDao(Session.class);
        }
        return sessionDao;
    }

    public Dao<OnGoingAttribute, Integer> getAttributesDao() throws SQLException {
        if(attributesDao == null) {
            attributesDao = getDao(OnGoingAttribute.class);
        }
        return attributesDao;
    }

    public RuntimeExceptionDao<Session, String> getSessionRuntimeDao() {
        if(sessionRuntimeDao == null) {
            sessionRuntimeDao = getRuntimeExceptionDao(Session.class);
        }
        return sessionRuntimeDao;
    }

    public RuntimeExceptionDao<OnGoingAttribute, Integer> getAttributesRuntimeDao() {
        if(attributesRuntimeDao == null) {
            attributesRuntimeDao = getRuntimeExceptionDao(OnGoingAttribute.class);
        }
        return attributesRuntimeDao;
    }

    @Override
    public void close() {
        super.close();
        sessionDao = null;
        attributesDao = null;
    }
}
